
/**
 *
 * SampleDraftImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraft object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleDraft;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftDaoImpl extends WaspDaoImpl<SampleDraft> implements edu.yu.einstein.wasp.dao.SampleDraftDao {

  public SampleDraftDaoImpl() {
    super();
    this.entityClass = SampleDraft.class;
  }

  @Override
@SuppressWarnings("unchecked")
  @Transactional
  public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId) {
    HashMap m = new HashMap();
    m.put("sampleDraftId", sampleDraftId);
    List<SampleDraft> results = this.findByMap(m);
    if (results.size() == 0) {
      SampleDraft rt = new SampleDraft();
      return rt;
    }
    return results.get(0);
  }

  
  @Override
@SuppressWarnings("unchecked")
  @Transactional
  public List<SampleDraft> getSampleDraftByJobId (final int jobdraftId) {
    HashMap m = new HashMap();
    m.put("jobdraftId", jobdraftId);
   List<SampleDraft> results = this.findByMap(m);
   return results;
  }


}
