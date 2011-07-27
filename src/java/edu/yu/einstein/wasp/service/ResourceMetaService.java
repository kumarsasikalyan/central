
/**
 *
 * ResourceMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.model.ResourceMeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ResourceMetaService extends WaspService<ResourceMeta> {

  public void setResourceMetaDao(ResourceMetaDao resourceMetaDao);
  public ResourceMetaDao getResourceMetaDao();

  public ResourceMeta getResourceMetaByResourceMetaId (final int resourceMetaId);

  public ResourceMeta getResourceMetaByKResourceId (final String k, final int resourceId);

  public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList);

}

