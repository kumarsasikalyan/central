
/**
 *
 * MetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta Dao Impl
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

import edu.yu.einstein.wasp.model.Meta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class MetaDaoImpl extends WaspDaoImpl<Meta> implements edu.yu.einstein.wasp.dao.MetaDao {

	/**
	 * MetaDaoImpl() Constructor
	 *
	 *
	 */
	public MetaDaoImpl() {
		super();
		this.entityClass = Meta.class;
	}


	/**
	 * getMetaByMetaId(final int metaId)
	 *
	 * @param final int metaId
	 *
	 * @return meta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Meta getMetaByMetaId (final int metaId) {
    		HashMap m = new HashMap();
		m.put("metaId", metaId);

		List<Meta> results = (List<Meta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return (Meta) results.get(0);
	}



	/**
	 * getMetaByPropertyK(final String property, final String k)
	 *
	 * @param final String property, final String k
	 *
	 * @return meta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Meta getMetaByPropertyK (final String property, final String k) {
    		HashMap m = new HashMap();
		m.put("property", property);
		m.put("k", k);

		List<Meta> results = (List<Meta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return (Meta) results.get(0);
	}



	/**
	 * getMetaByPropertyV(final String property, final String v)
	 *
	 * @param final String property, final String v
	 *
	 * @return meta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Meta getMetaByPropertyV (final String property, final String v) {
    		HashMap m = new HashMap();
		m.put("property", property);
		m.put("v", v);

		List<Meta> results = (List<Meta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Meta rt = new Meta();
			return rt;
		}
		return (Meta) results.get(0);
	}



}

