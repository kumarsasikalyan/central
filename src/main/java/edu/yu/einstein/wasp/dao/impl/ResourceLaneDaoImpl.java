
/**
 *
 * ResourceLaneDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLane Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceLane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceLaneDaoImpl extends WaspDaoImpl<ResourceLane> implements edu.yu.einstein.wasp.dao.ResourceLaneDao {

	/**
	 * ResourceLaneDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceLaneDaoImpl() {
		super();
		this.entityClass = ResourceLane.class;
	}


	/**
	 * getResourceLaneByResourceLaneId(final int resourceLaneId)
	 *
	 * @param final int resourceLaneId
	 *
	 * @return resourceLane
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId) {
    		HashMap m = new HashMap();
		m.put("resourceLaneId", resourceLaneId);

		List<ResourceLane> results = (List<ResourceLane>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceLane rt = new ResourceLane();
			return rt;
		}
		return (ResourceLane) results.get(0);
	}



	/**
	 * getResourceLaneByINameResourceId(final String iName, final int resourceId)
	 *
	 * @param final String iName, final int resourceId
	 *
	 * @return resourceLane
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId) {
    		HashMap m = new HashMap();
		m.put("iName", iName);
		m.put("resourceId", resourceId);

		List<ResourceLane> results = (List<ResourceLane>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceLane rt = new ResourceLane();
			return rt;
		}
		return (ResourceLane) results.get(0);
	}



	/**
	 * getResourceLaneByNameResourceId(final String name, final int resourceId)
	 *
	 * @param final String name, final int resourceId
	 *
	 * @return resourceLane
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId) {
    		HashMap m = new HashMap();
		m.put("name", name);
		m.put("resourceId", resourceId);

		List<ResourceLane> results = (List<ResourceLane>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceLane rt = new ResourceLane();
			return rt;
		}
		return (ResourceLane) results.get(0);
	}



}

