package edu.yu.einstein.wasp.batch.poller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * Workflow Workflow Resource Category Poller
 * polls job db for task/run/resourcecategory combos that are not final
 *
 */

@Service
public class WorkflowResourceCategoryRunPoller {

	@Autowired
	StateService stateService;

	@Autowired
	TaskService taskService;

	@Autowired
	ResourceCategoryService resourceCategoryService;
 
	protected String resourceCategoryIName; 
	public void setResourceCategoryIName(String s) {
		this.resourceCategoryIName= s;
	}

	protected String taskIName; 
	public void setTaskIName(String s) {
		this.taskIName = s;
	}

 
	@Transactional
	public List<State> getStates() {
		ResourceCategory resourceCategory = resourceCategoryService.getResourceCategoryByIName(resourceCategoryIName); 
		Task task = taskService.getTaskByIName(taskIName); 

		List<State> rt = new ArrayList();
		List<State> allStates = task.getState();

		if (allStates == null) { return rt; }

		for (State state: allStates) {
			if (state.getStaterun().isEmpty()) { continue; }
			if (state.getStatus().equals("FINAL")) { continue; }

			// should be the same so just get first, 
			// TODO check for npe
			if (state.getStaterun().get(0).getRun().getResourceCategoryId() != resourceCategory.getResourceCategoryId()) {
				continue;
			}

			rt.add(state);
		}

		return rt; 
	}

}