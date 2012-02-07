
/**
 *
 * SampleDraftMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SubtypeSample;

@Service
public interface SampleDraftMetaService extends WaspMetaService<SampleDraftMeta> {

  public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao);
  public SampleDraftMetaDao getSampleDraftMetaDao();

  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId);

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId);

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList);

  /**
   * Returns a Map of subtypeSamples and associated field metadata (i.e. that with a metaposition) associated with the provided workflow
   * @param workflowId
   * @return Map< {@link SubtypeSample}, List<{@link SampleDraftMeta}> >
   */
  Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(int workflowId);
  
 
}
