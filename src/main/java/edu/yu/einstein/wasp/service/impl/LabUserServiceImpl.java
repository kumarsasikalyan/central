
/**
 *
 * LabUserServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUserService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.LabUser;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabUserServiceImpl extends WaspServiceImpl<LabUser> implements LabUserService {

	/**
	 * labUserDao;
	 *
	 */
	private LabUserDao labUserDao;

	/**
	 * setLabUserDao(LabUserDao labUserDao)
	 *
	 * @param labUserDao
	 *
	 */
	@Autowired
	public void setLabUserDao(LabUserDao labUserDao) {
		this.labUserDao = labUserDao;
		this.setWaspDao(labUserDao);
	}

	/**
	 * getLabUserDao();
	 *
	 * @return labUserDao
	 *
	 */
	public LabUserDao getLabUserDao() {
		return this.labUserDao;
	}


  public LabUser getLabUserByLabUserId (final int labUserId) {
    return this.getLabUserDao().getLabUserByLabUserId(labUserId);
  }

  public LabUser getLabUserByLabIdUserId (final int labId, final int UserId) {
    return this.getLabUserDao().getLabUserByLabIdUserId(labId, UserId);
  }

}

