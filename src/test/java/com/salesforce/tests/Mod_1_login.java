package com.salesforce.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.salesforce.base.BrowserFactory;
import com.salesforce.base.TestBase;
import com.salesforce.pages.HomePage;
import com.salesforce.pages.LoginPage;
import com.salesforce.utils.Log;

public class Mod_1_login extends TestBase {

	LoginPage lp;
	HomePage hp;

	@BeforeMethod
	public void beforeTest() {

		browserIntialization();

	}

	@Test(groups = "regression,sanity,smoke", priority = 1, description = "Verify Successful Login Only")

	public void verifyLogin_Tc_001() {

		Log.startTestCase("Verify Successful Login Only");
		lp = new LoginPage(driver);
		hp = lp.login(prop.getProperty("email"), prop.getProperty("password"));
		hp.verifyHomePageHeading();
		Assert.assertEquals(hp.verifyHomePageHeading(), "DockSpace Test");
		Log.endTestCase("Verify Successful Login Only");

	}
	
	
	
	
//	(dataProvider = "credentials", dataProviderClass = DataProviders.class)

	@AfterMethod
	public void afterTest() {

		//driver.get(prop.getProperty("logout_url"));
		BrowserFactory.getInstance().removeDriver();

	}

}
