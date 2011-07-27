
/**
 *
 * DepartmentUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.DepartmentUserService;
import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.DepartmentUser;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentUserServiceImpl extends WaspServiceImpl<DepartmentUser> implements DepartmentUserService {

  private DepartmentUserDao departmentUserDao;
  @Autowired
  public void setDepartmentUserDao(DepartmentUserDao departmentUserDao) {
    this.departmentUserDao = departmentUserDao;
    this.setWaspDao(departmentUserDao);
  }
  public DepartmentUserDao getDepartmentUserDao() {
    return this.departmentUserDao;
  }

  // **

  
  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId) {
    return this.getDepartmentUserDao().getDepartmentUserByDepartmentUserId(departmentUserId);
  }

  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId) {
    return this.getDepartmentUserDao().getDepartmentUserByDepartmentIdUserId(departmentId, UserId);
  }
}

