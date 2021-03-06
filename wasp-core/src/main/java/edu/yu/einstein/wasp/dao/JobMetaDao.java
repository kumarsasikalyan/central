
/**
 *
 * JobMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobMeta;


public interface JobMetaDao extends WaspMetaDao<JobMeta> {

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);

 



}

