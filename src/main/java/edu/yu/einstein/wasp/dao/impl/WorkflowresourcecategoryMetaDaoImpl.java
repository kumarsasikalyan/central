
/**
 *
 * WorkflowresourcecategoryMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowresourcecategoryMeta Dao Impl
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
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowresourcecategoryMetaDaoImpl extends WaspDaoImpl<WorkflowresourcecategoryMeta> implements edu.yu.einstein.wasp.dao.WorkflowresourcecategoryMetaDao {

	/**
	 * WorkflowresourcecategoryMetaDaoImpl() Constructor
	 *
	 *
	 */
	public WorkflowresourcecategoryMetaDaoImpl() {
		super();
		this.entityClass = WorkflowresourcecategoryMeta.class;
	}


	/**
	 * getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId(final Integer workflowresourcecategoryMetaId)
	 *
	 * @param final Integer workflowresourcecategoryMetaId
	 *
	 * @return workflowresourcecategoryMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryMetaId (final Integer workflowresourcecategoryMetaId) {
    		HashMap m = new HashMap();
		m.put("workflowresourcecategoryMetaId", workflowresourcecategoryMetaId);

		List<WorkflowresourcecategoryMeta> results = (List<WorkflowresourcecategoryMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowresourcecategoryMeta rt = new WorkflowresourcecategoryMeta();
			return rt;
		}
		return (WorkflowresourcecategoryMeta) results.get(0);
	}



	/**
	 * getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK(final Integer workflowresourcecategoryId, final String k)
	 *
	 * @param final Integer workflowresourcecategoryId, final String k
	 *
	 * @return workflowresourcecategoryMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public WorkflowresourcecategoryMeta getWorkflowresourcecategoryMetaByWorkflowresourcecategoryIdK (final Integer workflowresourcecategoryId, final String k) {
    		HashMap m = new HashMap();
		m.put("workflowresourcecategoryId", workflowresourcecategoryId);
		m.put("k", k);

		List<WorkflowresourcecategoryMeta> results = (List<WorkflowresourcecategoryMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			WorkflowresourcecategoryMeta rt = new WorkflowresourcecategoryMeta();
			return rt;
		}
		return (WorkflowresourcecategoryMeta) results.get(0);
	}



}
