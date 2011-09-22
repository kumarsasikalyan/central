package edu.yu.einstein.wasp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*; 

import java.util.Date; 
import java.util.List; 
import java.util.Map; 
import java.util.HashMap; 

import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.DepartmentUserService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/department")
public class DepartmentController extends WaspController {

  private DepartmentService departmentService;
  @Autowired
  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }
  public DepartmentService getDepartmentService() {
    return this.departmentService;
  }

  private DepartmentUserService departmentUserService;
  @Autowired
  public void setDepartmentUserService(DepartmentUserService departmentUserService) {
    this.departmentUserService = departmentUserService;
  }
  public DepartmentUserService getDepartmentUserService() {
    return this.departmentUserService;
  }


  @Autowired
  private LabPendingService labPendingService;
  
  //private static final MetaAttribute.Area AREA = MetaAttribute.Area.labPending;
 
  @RequestMapping("/list")
  @PreAuthorize("hasRole('god') or hasRole('da-*')")
  public String list(ModelMap m) {
    List<Department> departmentList = this.getDepartmentService().findAll();
    
    m.addAttribute("department", departmentList);

    return "department/list";
  }

  @RequestMapping(value="/detail/{departmentId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String detail(@PathVariable("departmentId") Integer departmentId, ModelMap m) {

    Department department = this.getDepartmentService().getById(departmentId.intValue());

    List<Lab> labList = department.getLab();
    labList.size();

    List<DepartmentUser> departmentUserList = department.getDepartmentUser();
    departmentUserList.size();

    Map labPendingQueryMap = new HashMap();
    labPendingQueryMap.put("status", "PENDING");
    labPendingQueryMap.put("departmentId", departmentId);

    List<LabPending> labPendingList = labPendingService.findByMap(labPendingQueryMap);

    m.addAttribute("department", department);
    m.addAttribute("departmentuser", departmentUserList);
    m.addAttribute("lab", labList);
    m.addAttribute("labpending", labPendingList);

    return "department/detail";
  }

  @RequestMapping(value="/create", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god')")
  public String createDepartment(@RequestParam("name") String name, ModelMap m) {
	  
	if( "".equals(name.trim()) ){
		waspMessage("department.list_missingparam.error");
	}
	else{
		Department existingDepartment = this.departmentService.getDepartmentByName(name.trim()); 
		if( existingDepartment.getDepartmentId() > 0 ){//the id will be 0 if empty department [ie.: department does not already exist]
			waspMessage("department.list_department_exists.error");
		}
		else{
			Department department = new Department(); 
			department.setName(name.trim()); 
			departmentService.save(department);
			waspMessage("department.list_ok.label");
		}
	}
    return "redirect:/department/list.do";
  }

  @RequestMapping(value="/user/roleRemove/{departmentId}/{userId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String departmentUserRoleRemove (
    @PathVariable("departmentId") Integer departmentId, 
    @PathVariable("userId") Integer userId, 
    ModelMap m) {

    DepartmentUser departmentUser = departmentUserService.getDepartmentUserByDepartmentIdUserId(departmentId, userId);

    departmentUserService.remove(departmentUser);

    // if i am the user,  reauth
    User me = getAuthenticatedUser();
    if (me.getUserId() == userId) {
      doReauth();
    }

    return "redirect:/department/detail/" + departmentId + ".do";
  }

  @RequestMapping(value="/user/roleAdd", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String departmentUserRoleAdd (
    @RequestParam("departmentId") Integer departmentId,
    @RequestParam("useremail") String useremail,
    ModelMap m) {

	if( "".equals(useremail.trim()) ){
		waspMessage("department.detail_missingparam.error");
	}
	else{
		User user = userService.getUserByEmail(useremail.trim());
		if(user.getUserId()==0){
			waspMessage("department.detail_emailnotfound.error");
		}
		else{
			DepartmentUser departmentUser = new DepartmentUser(); 
			departmentUser.setDepartmentId(departmentId); 
			departmentUser.setUserId(user.getUserId()); 
			departmentUserService.save(departmentUser);
			waspMessage("department.detail_ok.label");
			
			// if i am the user,  reauth
			User me = getAuthenticatedUser();
			if (me.getUserId() == user.getUserId()) {
				doReauth();
			}
		}
	}
    return "redirect:/department/detail/" + departmentId + ".do";
  }


}
