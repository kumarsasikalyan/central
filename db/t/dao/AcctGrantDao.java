
/**
 *
 * AcctGrantDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctGrantDao extends WaspDao<AcctGrant> {

  public AcctGrant getAcctGrantByGrantId (final int grantId);

}

