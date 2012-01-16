
/**
 *
 * JobDraftMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.JobDraftMeta;


public interface JobDraftMetaDao extends WaspDao<JobDraftMeta> {

  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId);

  public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId);


  public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList);

  public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList);




}

