package edu.yu.einstein.wasp.plugin;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.NullChannel;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class PluginTest {

	private TestPlugin test;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void showCustomMethods() {
		Set<String> methods = test.getMethods();
		for (String x : methods) {
			logger.info("Method: " + x);
		}
	}

	@BeforeTest
	public void beforeTest() {
		test = new TestPlugin("foo", new Properties(), new NullChannel());
	}

	@AfterTest
	public void afterTest() {
	}
	
	private class TestPlugin extends WaspPlugin {

		public TestPlugin(String pluginName, Properties waspSiteProperties,
				MessageChannel channel) {
			super(pluginName, waspSiteProperties, channel);
			// TODO Auto-generated constructor stub
		}
		
		public Message testMethod(Message m) {
			return null;
		}
		
		public Message<String> secondTest(Message<String> m) {
			return null;
		}
		
		public <T> Message<T> thirdTest(Message<T> m) {
			return null;
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void destroy() throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getBatchJobName(String BatchJobType) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getBatchJobNameByArea(String BatchJobType, String area) {
			// TODO Auto-generated method stub
			return null;
		}

		
		
	}

}
