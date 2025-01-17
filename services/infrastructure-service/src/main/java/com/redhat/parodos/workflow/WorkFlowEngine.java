/*
 * Copyright (c) 2022 Red Hat Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.parodos.workflow;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;
import com.redhat.parodos.security.SecurityUtils;
import com.redhat.parodos.workflow.execution.WorkFlowTransactionDto;
import com.redhat.parodos.workflow.execution.WorkTransactionService;
import com.redhat.parodos.workflows.WorkFlowConstants;
import com.redhat.parodos.workflows.engine.WorkFlowEngineBuilder;
import com.redhat.parodos.workflows.work.WorkContext;
import com.redhat.parodos.workflows.work.WorkReport;
import com.redhat.parodos.workflows.workflow.WorkFlow;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Executes a WorkFlow (AssessmentTask or InfrastructureTask)
 * 
 * @author Luke Shannon (Github: lshannon)
 * 
 *
 */
@Component
@Slf4j
public class WorkFlowEngine {
	
    private final WorkTransactionService existingInfrastructureService;
    private final SecurityUtils securityUtils;
	
    public WorkFlowEngine(WorkTransactionService existingInfrastructureService, SecurityUtils securityUtils) {
		this.existingInfrastructureService = existingInfrastructureService;
		this.securityUtils = securityUtils;
	}

	/**
     * 
     * Runs all the All Tasks that have be packaged into a WorkFlow with the provided Context
     * 
     * @param workContext common objects passed across Work units. Each task stores the result 
     * @param workFlow the list of steps that need to be done to create the InfrastructureOption
     * @return workReport indicating if the WorkFlow was successful it also contains the updated WorkContext
     * 
     * @author Luke Shannon (Github: lshannon)
     */
    public WorkReport executeWorkFlows(WorkContext workContext, WorkFlow workFlow) {
    	log.debug("Running the WorkFlow: {} with Context: {}", workFlow.getName(), workContext.toString());
    	WorkReport report = WorkFlowEngineBuilder.aNewWorkFlowEngine().build().run(workFlow, workContext);
    	String userName = getUserName();
    	report.getWorkContext().put(WorkFlowConstants.WORKFLOW_EXECUTION_ENTITY_REFERENCE, existingInfrastructureService.createWorkFlowTransactionEntity(
    			WorkFlowTransactionDto.builder()
    			.createdAt(OffsetDateTime.now())
    			.executedBy(userName)
    			.workflowType((String)workContext.get(WorkFlowConstants.WORKFLOW_TYPE))
    			.status(report.getStatus().toString())
    			.projectName((String)workContext.get(WorkFlowConstants.PROJECT_NAME))
    			.build()));
        return report;
    }

	private String getUserName() {
		String userName = securityUtils.getUsername();
    	if (userName == null) {
    		log.error("Setting the username to a default");
    		userName = "N/A";
    	}
		return userName;
	}
    
}
