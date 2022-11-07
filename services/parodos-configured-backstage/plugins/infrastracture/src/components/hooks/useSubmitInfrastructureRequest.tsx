/*
 *  Copyright (c) 2022 Red Hat Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * @author Luke Shannon (Github: lshannon)
 */

import { useContext, useState } from 'react';
import axios from 'axios';
import ToastContext from '../context/toast';
import { getUrl } from '../util/getUrl';

const useSubmitInfrastructureRequest = ({
  selectedOrganizationState,
  selectedRepoState,
}) => {
  const toastContext = useContext(ToastContext);
  const [isLoadingState, setIsLoadingState] = useState(false);
  const [repositoriesState, setRepositoriesState] = useState([]);
  const url = getUrl();

  const submitRequest = async ({ migrationPlan }) => {
    try {
      setIsLoadingState(true);
      migrationPlan = {
        workFlowName: migrationPlan.workFlowName,
        requestDetails: {
          PROJECT_NAME: `${selectedOrganizationState}/${selectedRepoState}`,
          org: selectedOrganizationState,
          repo: selectedRepoState,
        },
      };
      console.log('migrationplan:', migrationPlan);
      const repoResponse = await axios.post(`${url}/api/tasks/`, migrationPlan);
      setRepositoriesState(repoResponse.data);
    } catch (error) {
      toastContext.handleOpenToast(
        `Oops! Something went wrong. Please try again`,
      );
    } finally {
      setIsLoadingState(false);
    }
  };

  return {
    submitRequest,
    isLoading: isLoadingState,
    repositories: repositoriesState,
  };
};

export default useSubmitInfrastructureRequest;
