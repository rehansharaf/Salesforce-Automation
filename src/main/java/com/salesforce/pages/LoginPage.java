package com.salesforce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.salesforce.actiondriver.Action;

public class LoginPage {
	
	WebDriver driver;
	Action action = new Action();
	
	By username = By.id("username");
	By password = By.id("password");
	By loginBtn = By.id("Login");
	
	
	
	
	
	public LoginPage(WebDriver driver) {
	
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	
	public HomePage login(String email, String pass) {
		
		action.type(driver.findElement(username), email);
		action.type(driver.findElement(password), pass);
		//action.click(driver.findElement(loginBtn), "Login btn");
		return new HomePage(driver);
		
	}
	
	
	

	
}
