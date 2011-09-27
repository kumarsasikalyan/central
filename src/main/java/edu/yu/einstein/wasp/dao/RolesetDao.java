
/**
 *
 * RolesetDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RolesetDao extends WaspDao<Roleset> {

  public Roleset getRolesetByRolesetId (final int rolesetId);

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId);


}

