
/**
 *
 * RunDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Run Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Run;


@Transactional("entityManager")
@Repository
public class RunDaoImpl extends WaspDaoImpl<Run> implements edu.yu.einstein.wasp.dao.RunDao {

	/**
	 * RunDaoImpl() Constructor
	 *
	 *
	 */
	public RunDaoImpl() {
		super();
		this.entityClass = Run.class;
	}


	/**
	 * getRunByRunId(final int runId)
	 *
	 * @param final int runId
	 *
	 * @return run
	 */

	@Override
	@Transactional("entityManager")
	public Run getRunByRunId (final int runId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", runId);

		List<Run> results = this.findByMap(m);

		if (results.size() == 0) {
			Run rt = new Run();
			return rt;
		}
		return results.get(0);
	}

	  
	  @Override
	  public List<Run> getActiveRuns(){
		  Map<String, Integer> queryMap = new HashMap<String, Integer>();
		  queryMap.put("isActive", 1);
		  return this.findByMap(queryMap);
	  }

}

