
/**
 *
 * SampleFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFileDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleFileDao extends WaspDao<SampleFile> {

  public SampleFile getSampleFileBySampleFileId (final int sampleFileId);

}

