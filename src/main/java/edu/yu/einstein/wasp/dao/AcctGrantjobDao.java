
/**
 *
 * AcctGrantjobDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctGrantjobDao extends WaspDao<AcctGrantjob> {

  public AcctGrantjob getAcctGrantjobByJobId (final int jobId);


}

