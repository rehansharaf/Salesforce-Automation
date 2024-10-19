package com.salesforce.utils;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
/**
 * ExtentManager class is used for Extent Report
 *  
 */
public class ExtentManager {
	
	public static ExtentSparkReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ThreadLocal<ExtentTest> extenttest = new ThreadLocal<>();
	
	public static void setExtent() {
		//htmlReporter= new ExtentHtmlReporter(System.getProperty("user.dir")+"/test-output/ExtentReport/"+"MyReport_"+BaseClass.getCurrentTime()+".html");
		htmlReporter= new ExtentSparkReporter(System.getProperty("user.dir")+"/test-output/extentreport/"+"MyReport.html");
		try {
			htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/extent-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		htmlReporter.config().setDocumentTitle("Automation Test Report");
//		htmlReporter.config().setReportName("Medflow Test Automation Report");
//		htmlReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		//extent.setSystemInfo("HostName", "Marina");
		extent.setSystemInfo("ProjectName", "SalesForce");
		extent.setSystemInfo("Tester", "Automation Team");
		extent.setSystemInfo("OS", "Windows Server");
		extent.setSystemInfo("Browser", "Chrome");
	}
	public static void endReport() {
		extent.flush();
	}
}
