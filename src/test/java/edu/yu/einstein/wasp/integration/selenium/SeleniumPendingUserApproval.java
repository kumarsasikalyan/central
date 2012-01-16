package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;
/**
 * 
 * @author nvolnova
 *
 */
public class SeleniumPendingUserApproval extends SeleniumBaseTest{
  
  @BeforeClass
  public void beforeClass() {
	  
  }
  
  /**
   * 
   * @return retObjArr
   * @throws Exception
   */
  @DataProvider(name = "DP1")
  public Object[][] createData1() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test1", "pendingUserApprove");
      
      return(retObjArr);
  }
  
  @DataProvider(name = "DP2")
  public Object[][] createData2() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test1", "pendingUserReject");
      
      return(retObjArr);
  }
  
  @Test (groups = "integration-tests",  dataProvider = "DP1")
  public void pendingUserApproval(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.login(sUserName, sUserPass, driver);
	  driver.findElement(By.xpath("//a[contains(.,'Pending User Approval')]")).click();
	  
      Assert.assertNotNull(driver.findElement(By.linkText("APPROVE")), "'APPROVE' link does not exist");
	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).size() == 0, "Failed to approve a new user");

      
  }
  
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingUserReject(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sRejectedUrl) throws Exception { 
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  driver.findElement(By.xpath("//a[contains(.,'Pending User Approval')]")).click();
	  Assert.assertNotNull(driver.findElement(By.linkText("REJECT")), "'REJECT' link does not exist");
	  
      Assert.assertTrue(SeleniumHelper.verifyTextPresent(sUserEmail, driver),"User with "+ sUserEmail+" email not found");
  	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).size() == 0, "Failed to reject a new user");

  }
  
  
  
  
  @AfterClass
  public void afterClass() {
  }

}
