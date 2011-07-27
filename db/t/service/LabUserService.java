
/**
 *
 * LabUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.model.LabUser;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface LabUserService extends WaspService<LabUser> {

  public void setLabUserDao(LabUserDao labUserDao);
  public LabUserDao getLabUserDao();

  public LabUser getLabUserByLabUserId (final int labUserId);

  public LabUser getLabUserByLabIdUserId (final int labId, final int UserId);

}

