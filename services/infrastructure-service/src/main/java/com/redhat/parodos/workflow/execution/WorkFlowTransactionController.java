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
package com.redhat.parodos.workflow.execution;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to check the status of an ExitingInfrastructure references
 * 
 * @author Luke Shannon (Github: lshannon)
 *
 */
@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/api/v1/workflows/transactions")
public class WorkFlowTransactionController {
    
    private WorkTransactionService workTransactionService;

    public WorkFlowTransactionController(WorkTransactionService workTransactionService) {
        this.workTransactionService = workTransactionService;
    }
    
   @GetMapping("/{id}")
   public ResponseEntity<WorkFlowTransactionDto> getTransactionRecord(@PathVariable String id) {
       return ResponseEntity.ok(workTransactionService.getWorkFlowTransactionEntity(id));
   }
   

}
