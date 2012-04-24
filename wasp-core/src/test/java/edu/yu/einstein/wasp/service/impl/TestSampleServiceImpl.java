package edu.yu.einstein.wasp.service.impl;


import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.impl.SampleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.WorkflowDaoImpl;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;

public class TestSampleServiceImpl {
	
	HibernateTemplate hibernateTemplateMock;
	private SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
	SampleDao mockSampleDao;
	WorkflowDao mockWorkflowDao;
	SampleMetaDao mockSampleMetaDao;


  // Test (groups = "unit-tests-service-objects")
  //TODO: test not working, please fix me!
  public void getSampleSubtypesForWorkflowByRole() {
	  
	  sampleServiceImpl.setSampleDao(mockSampleDao);
	  sampleServiceImpl.setWorkflowDao(mockWorkflowDao);
	  
	  Integer workflowId = new Integer(1);
	  Workflow wf = new Workflow();
	  WorkflowSampleSubtype wfss = new WorkflowSampleSubtype();
	  SampleSubtype sampleSubtype = new SampleSubtype();
	  wfss.setSampleSubtype(sampleSubtype);

	  List<WorkflowSampleSubtype> wfssList = new ArrayList <WorkflowSampleSubtype>();
	  wfssList.add(wfss);
	  wf.setWorkflowSampleSubtype(wfssList);
  
	  String [] roles = new String[]{"6","7","8"};
	  
	  List<SampleSubtype> sampleSubtypes = new ArrayList<SampleSubtype>();
	  
	  expect(mockWorkflowDao.getWorkflowByWorkflowId(1)).andReturn(wf);

	  replay(mockWorkflowDao);

      Assert.assertEquals(sampleServiceImpl.getSampleSubtypesForWorkflowByRole(1, roles, null), sampleSubtypes);
	  verify(mockWorkflowDao);
	  
  }
  @Test (groups = "unit-tests-service-objects")
  public void saveSampleWithAssociatedMeta(){
	  sampleServiceImpl.setSampleDao(mockSampleDao);
	  sampleServiceImpl.setSampleMetaDao(mockSampleMetaDao);


	  Sample newsample = new Sample();
	  newsample.setSampleId(1);
	  newsample.setName("0001");
	  List<SampleMeta> sampleMetaList = new ArrayList<SampleMeta>();
	  
	  SampleMeta sampleMeta = new SampleMeta();
	  sampleMeta.setSample(newsample);
	  sampleMeta.setK("sample.sample_type");
	  sampleMeta.setV("ChIP");
	  sampleMetaList.add(sampleMeta);
	  newsample.setSampleMeta(sampleMetaList);
	  
	  expect(mockSampleDao.save(newsample)).andReturn(newsample);
	  replay(mockSampleDao);
	  mockSampleMetaDao.updateBySampleId(1, sampleMetaList);
	  EasyMock.expectLastCall();
	  sampleServiceImpl.saveSampleWithAssociatedMeta(newsample);
	  verify(mockSampleDao);
  }
  
  @Test (groups = "unit-tests-service-objects")
  public void isSampleNameUniqueWithinJob() {
	  SampleType sampleType = new SampleType();
	  sampleType.setIName("sample1");
	  Job job = new Job();
	  Sample sample = new Sample();
	  sample.setName("sample1");
	  sample.setSampleType(sampleType);
	  List<Sample> sampleList =  new ArrayList<Sample>();
	  sampleList.add(sample);
	  job.setSample(sampleList);
	  
	  Assert.assertEquals(sampleServiceImpl.isSampleNameUniqueWithinJob(sample, sampleType, job), false);

	  
  }
  
  @BeforeTest
  public void beforeTest() {
	  mockWorkflowDao = createMockBuilder(WorkflowDaoImpl.class).addMockedMethods(WorkflowDaoImpl.class.getMethods()).createMock();
	  hibernateTemplateMock = createMockBuilder(HibernateTemplate.class).addMockedMethods(HibernateTemplate.class.getMethods()).createMock();
	  mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();
	  mockSampleDao = createMockBuilder(SampleDaoImpl.class).addMockedMethods(SampleDaoImpl.class.getMethods()).createMock();
	  
	  Assert.assertNotNull(mockWorkflowDao);
	  Assert.assertNotNull(hibernateTemplateMock);
	  Assert.assertNotNull(mockSampleMetaDao);
	  Assert.assertNotNull(mockSampleDao);

  }

  @AfterTest
  public void afterTest() {

  }

  @BeforeMethod
  public void setUp() throws Exception{
	  
 }

  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockWorkflowDao);
	  EasyMock.reset(hibernateTemplateMock);
	  EasyMock.reset(mockSampleMetaDao);
	  EasyMock.reset(mockSampleDao);

	  
  }

  @BeforeClass
  public void beforeClass() {
  }
   
  @BeforeSuite
  public void beforeSuite() {
	  
  }

  @AfterSuite
  public void afterSuite() {
  }

}