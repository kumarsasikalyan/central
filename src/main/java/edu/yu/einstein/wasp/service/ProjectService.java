
/**
 *
 * ProjectService.java 
 * @author echeng (table2type.pl)
 *  
 * the ProjectService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ProjectDao;
import edu.yu.einstein.wasp.model.Project;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ProjectService extends WaspService<Project> {

	/**
	 * setProjectDao(ProjectDao projectDao)
	 *
	 * @param projectDao
	 *
	 */
	public void setProjectDao(ProjectDao projectDao);

	/**
	 * getProjectDao();
	 *
	 * @return projectDao
	 *
	 */
	public ProjectDao getProjectDao();

  public Project getProjectByProjectId (final int projectId);

  public Project getProjectByNameLabId (final String name, final int labId);


}

