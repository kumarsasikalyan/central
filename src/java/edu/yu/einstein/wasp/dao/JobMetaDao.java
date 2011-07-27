
/**
 *
 * JobMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobMetaDao extends WaspDao<JobMeta> {

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);

  public void updateByJobId (final int jobId, final List<JobMeta> metaList);

}

