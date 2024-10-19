package com.salesforce.base;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserFactory {
	
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static BrowserFactory instance;
	
	private BrowserFactory() {}
	
	public static synchronized BrowserFactory getInstance() {
		
		if(instance == null)
			instance = new BrowserFactory();
		 
		return instance;
	}
	
	 
	public void setDriver(String browserName) {
		
		if (browserName.equalsIgnoreCase("Chrome")) {

			WebDriverManager.chromedriver().clearResolutionCache();
			//WebDriverManager.chromedriver().clearDriverCache().setup();
			WebDriverManager.chromedriver().clearDriverCache();
			WebDriverManager.chromedriver().setup();
		
			
			HashMap<String,Object> chromePrefs = new HashMap<String, Object>(); 
	    	chromePrefs.put("plugins.always_open_pdf_externally", true);
			chromePrefs.put("download.prompt_for_download", false); 
			chromePrefs.put("download.default_directory", System.getProperty("user.dir")+"\\src\\test\\resources\\downloadeddata\\");
	    	ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs); 

			// Set Browser to ThreadLocalMap
			driver.set(new ChromeDriver(options));
			
		} else if (browserName.equalsIgnoreCase("FireFox")) {
			WebDriverManager.firefoxdriver().setup();
			driver.set(new FirefoxDriver());
		} else if (browserName.equalsIgnoreCase("IE")) {
			WebDriverManager.iedriver().setup();
			driver.set(new InternetExplorerDriver());
		}
			
	}
	
	public WebDriver getDriver() {
		// Get Driver from threadLocalmap
		return driver.get();
	}
	
	public void removeDriver() {
		
		getDriver().quit();
		driver.remove();
	}

}
