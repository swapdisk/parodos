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
package com.redhat.parodos.assessment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.redhat.parodos.infrastructure.option.InfrastructureOption;
import com.redhat.parodos.workflows.WorkFlowTask;
import com.redhat.parodos.workflows.WorkFlowConstants;
import com.redhat.parodos.workflows.workflow.SequentialFlow;
import com.redhat.parodos.workflows.workflow.WorkFlow;

/**
 * Sample Assessment WorkFlow
 * 
 * @author Luke Shannon (Github: lshannon)
 *
 */
@Configuration
public class AssessmentConfig  {
	
	//There might be many AssessmentTasks, using names and qualifiers ensure the right assessments under up in the correct workflow
	@Bean(name= "simpleAssessment")
	WorkFlowTask simpleAssessment(@Qualifier("awesomeToolStack") InfrastructureOption awesomeToolsOption) {
		return new SimpleAssessment(awesomeToolsOption);
	}

	//There might be many AssessmentTasks, using names and qualifiers ensure the right assessments under up in the correct workflow
	@Bean(name="assessmentWorkFlow" + WorkFlowConstants.ASSESSMENT_WORKFLOW)
	WorkFlow assessmentWorkFlow(@Qualifier("simpleAssessment") WorkFlowTask simpleAssessment) {
		return SequentialFlow
				.Builder
				.aNewSequentialFlow()
				.named("MyAssessment_" + WorkFlowConstants.ASSESSMENT_WORKFLOW)
				.execute(simpleAssessment)
				.build();
	}
	
}