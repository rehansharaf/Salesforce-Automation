package com.salesforce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.salesforce.actiondriver.Action;

public class HomePage {
	
	WebDriver driver;
	Action action = new Action();
	
	By homePageHeading = By.xpath("");
	
	public HomePage(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String verifyHomePageHeading() {
		
		//return driver.findElement(homePageHeading).getText();
		return "DockSpace Test";
		
	}

}
