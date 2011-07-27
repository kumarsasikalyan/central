
/**
 *
 * AcctGrantjobImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrantjob;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctGrantjobDaoImpl extends WaspDaoImpl<AcctGrantjob> implements edu.yu.einstein.wasp.dao.AcctGrantjobDao {

  public AcctGrantjobDaoImpl() {
    super();
    this.entityClass = AcctGrantjob.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctGrantjob getAcctGrantjobByJobId (final int jobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctGrantjob a WHERE "
       + "a.jobId = :jobId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobId", jobId);

    return query.getResultList();
  }
  });
    List<AcctGrantjob> results = (List<AcctGrantjob>) res;
    if (results.size() == 0) {
      AcctGrantjob rt = new AcctGrantjob();
      return rt;
    }
    return (AcctGrantjob) results.get(0);
  }


}

