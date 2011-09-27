
/**
 *
 * TaskServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Task;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl extends WaspServiceImpl<Task> implements TaskService {

	/**
	 * taskDao;
	 *
	 */
	private TaskDao taskDao;

	/**
	 * setTaskDao(TaskDao taskDao)
	 *
	 * @param taskDao
	 *
	 */
	@Autowired
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
		this.setWaspDao(taskDao);
	}

	/**
	 * getTaskDao();
	 *
	 * @return taskDao
	 *
	 */
	public TaskDao getTaskDao() {
		return this.taskDao;
	}


  public Task getTaskByTaskId (final int taskId) {
    return this.getTaskDao().getTaskByTaskId(taskId);
  }

  public Task getTaskByIName (final String iName) {
    return this.getTaskDao().getTaskByIName(iName);
  }

}

