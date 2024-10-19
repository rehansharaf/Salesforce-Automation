package com.salesforce.actioninterface;

import java.text.ParseException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ActionInterface {
	
	//Added all user actions abstract methods to achieve Abstraction  
	public void scrollByVisibilityOfElement(WebDriver driver, WebElement ele) throws InterruptedException;
	public void actionClick(WebDriver ldriver, WebElement ele);
	public boolean isDisplayed(WebDriver ldriver, WebElement ele);
	public boolean type(WebElement ele, String text);
	public boolean findElement(WebDriver ldriver, WebElement ele);
	public boolean isSelected(WebDriver ldriver, WebElement ele);
	public boolean isEnabled(WebDriver ldriver, WebElement ele);
	public boolean selectBySendkeys(String value,WebElement ele);
	public boolean selectByIndex(WebElement element, int index);
	public boolean selectByValue(WebElement element,String value);
	public boolean selectByVisibleText(String visibletext, WebElement ele);
	public boolean mouseHoverByJavaScript(WebElement locator);
	public boolean JSClick(WebDriver driver, WebElement ele);
	public boolean switchToFrameByLocator(WebDriver driver, By locator);
	public boolean switchToFrameByIndex(WebDriver driver,int index);
	public boolean switchToFrameById(WebDriver driver,String idValue);
	public boolean switchToFrameByName(WebDriver driver,String nameValue);
	public boolean switchToDefaultFrame(WebDriver driver);
	public void mouseOverElement(WebDriver driver,WebElement element);
	public boolean moveToElement(WebDriver driver, WebElement ele);
	public boolean mouseover(WebDriver driver, WebElement ele);
	public boolean draggable(WebDriver driver,WebElement source, int x, int y);
	public boolean draganddrop(WebDriver driver,WebElement source, WebElement target);
	public boolean slider(WebDriver driver,WebElement ele, int x, int y);
	public boolean rightclick(WebDriver driver,WebElement ele);
	public boolean switchWindowByTitle(WebDriver driver,String windowTitle, int count);
	public boolean switchToNewWindow(WebDriver driver);
	public boolean switchToOldWindow(WebDriver driver);
	public int getColumncount(WebElement row);
	public int getRowCount(WebElement table);
	public boolean Alert(WebDriver driver);
	public boolean launchUrl(WebDriver driver,String url);
	public boolean isAlertPresent(WebDriver driver);
	public String getCurrentURL(WebDriver driver);
	public String getTitle(WebDriver driver);
	public boolean click(WebElement locator, String locatorName);
	public void fluentWait(WebDriver driver,WebElement element, int timeOut);
	public void implicitWait(WebDriver driver, int timeOut);
	public void pageLoadTimeOut(WebDriver driver, int timeOut);
	public String screenShot(WebDriver driver, String filename);
	public String getCurrentTime();
	public String getCurrentDate(int todayDate, int nextDate, int daysToSkip, String dateFormat);
	public String isFileDownloaded(String fileText, String fileExtension, int timeOut);
	public boolean isAttribtuePresent(WebElement element, String attribute);
	public String getAlertText(WebDriver driver);
	public void explicitWaitElementClickable(WebDriver driver, WebElement element, Duration timeOut);
	public void explicitWait(WebDriver driver, By locator, Duration timeOut);
	public void waitForElement(String elementText, WebElement element) throws InterruptedException;
}
