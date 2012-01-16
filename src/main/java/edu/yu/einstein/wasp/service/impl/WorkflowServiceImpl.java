
/**
 *
 * WorkflowServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WorkflowService;

@Service
public class WorkflowServiceImpl extends WaspServiceImpl<Workflow> implements WorkflowService {

	/**
	 * workflowDao;
	 *
	 */
	private WorkflowDao workflowDao;

	/**
	 * setWorkflowDao(WorkflowDao workflowDao)
	 *
	 * @param workflowDao
	 *
	 */
	@Autowired
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
		this.setWaspDao(workflowDao);
	}

	/**
	 * getWorkflowDao();
	 *
	 * @return workflowDao
	 *
	 */
	public WorkflowDao getWorkflowDao() {
		return this.workflowDao;
	}


  public Workflow getWorkflowByWorkflowId (final int workflowId) {
    return this.getWorkflowDao().getWorkflowByWorkflowId(workflowId);
  }

  public Workflow getWorkflowByIName (final String iName) {
    return this.getWorkflowDao().getWorkflowByIName(iName);
  }

  public Workflow getWorkflowByName (final String name) {
    return this.getWorkflowDao().getWorkflowByName(name);
  }

}

