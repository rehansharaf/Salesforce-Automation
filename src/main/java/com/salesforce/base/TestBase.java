package com.salesforce.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.salesforce.utils.ExtentManager;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	public WebDriver driver;
	public static Properties prop;

	@BeforeSuite
	public void loadConfig() throws IOException {

		FileUtils.cleanDirectory(new File(System.getProperty("user.dir")+"\\screenshots"));
		//FileUtils.cleanDirectory(new File(System.getProperty("user.dir")+"\\test-output"));
		File screeshot_file = new File(System.getProperty("user.dir")+"\\screenshots\\.gitkeep");
		screeshot_file.createNewFile();
		
		ExtentManager.setExtent();
		DOMConfigurator.configure("log4j.xml");

		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/configuration/config.properties");
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void browserIntialization() {

		String browser = prop.getProperty("browser");
		BrowserFactory.getInstance().setDriver(browser);
		driver = BrowserFactory.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("implicit_wait_timeout"))));
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
		
		
		

	}



	@AfterSuite
	public void afterSuite() {
		ExtentManager.endReport();
	}

}
