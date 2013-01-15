
/**
 *
 * ResourceMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceMetaDaoImpl extends WaspMetaDaoImpl<ResourceMeta> implements edu.yu.einstein.wasp.dao.ResourceMetaDao {

	/**
	 * ResourceMetaDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceMetaDaoImpl() {
		super();
		this.entityClass = ResourceMeta.class;
	}


	/**
	 * getResourceMetaByResourceMetaId(final Integer resourceMetaId)
	 *
	 * @param final Integer resourceMetaId
	 *
	 * @return resourceMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId) {
    		HashMap m = new HashMap();
		m.put("resourceMetaId", resourceMetaId);

		List<ResourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceMetaByKResourceId(final String k, final Integer resourceId)
	 *
	 * @param final String k, final Integer resourceId
	 *
	 * @return resourceMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("resourceId", resourceId);

		List<ResourceMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceMeta rt = new ResourceMeta();
			return rt;
		}
		return results.get(0);
	}




}

