package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMockBuilder;
import junit.framework.Assert;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.impl.ConfirmEmailAuthDaoImpl;
import edu.yu.einstein.wasp.dao.impl.TaskDaoImpl;
import edu.yu.einstein.wasp.dao.impl.UserDaoImpl;
import edu.yu.einstein.wasp.model.User;

public class TestUserServiceImpl {
	
  private ConfirmEmailAuthDao mockConfirmEmailAuthDao;
  private UserDao mockUserDao;
  UserServiceImpl userServiceImpl = new UserServiceImpl();
 
  @Test
  public void getUniqueLoginName() {
	  
	  User userNew = new User();
	  userNew.setUserId(123);
	  userNew.setFirstName("John");
	  userNew.setLastName("Greally");
	  
	  
	  User userExists = new User();
	  userNew.setUserId(123);
	  userExists.setFirstName("Jane");
	  userExists.setLastName("Doe");
	  
	  User user_apostrophe = new User();
	  userNew.setUserId(123);
	  user_apostrophe.setFirstName("Jane");
	  user_apostrophe.setLastName("O'Brien");
	  
	  User user_hyphen = new User();
	  userNew.setUserId(123);
	  user_hyphen.setFirstName("Jane");
	  user_hyphen.setLastName("Some-Lastname");
	  
	  User user_apostrophe_middle = new User();
	  userNew.setUserId(123);
	  user_apostrophe_middle.setFirstName("Jane");
	  user_apostrophe_middle.setLastName("Some'Lastname");
	  
	  User userBlank = new User();//use this to break out of the while loop
	  
	  
	  userServiceImpl.setUserDao(mockUserDao);
	  
	  expect(mockUserDao.getUserByLogin("jdoe")).andReturn(userExists);
	  expect(mockUserDao.getUserByLogin("jdoe1")).andReturn(userBlank);
	  replay(mockUserDao);
	  Assert.assertEquals("jdoe1", userServiceImpl.getUniqueLoginName(userExists));

	  
	  
	  
	  
	  
	  
	  
	  
  }
  
  @Test
  public void getNewAuthcodeForUser() {
	  
	  
	  
  }
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

  @BeforeTest
  public void beforeTest() {
	  mockConfirmEmailAuthDao = createMockBuilder(ConfirmEmailAuthDaoImpl.class).addMockedMethods(ConfirmEmailAuthDaoImpl.class.getMethods()).createMock();
	  mockUserDao = createMockBuilder(UserDaoImpl.class).addMockedMethods(UserDaoImpl.class.getMethods()).createMock();

	  Assert.assertNotNull(mockConfirmEmailAuthDao);
  }

  @AfterTest
  public void afterTest() {
  }

}
