package com.hcl.testing.selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.hcl.testing.config.ZapProperties;

public class SeleniumTestClass {
	WebDriver driver;
	String site = "http://localhost:8888/bodgeit/";
	ZapProperties properties;
	
	public SeleniumTestClass(String applnsite,ZapProperties properties)
	{
		site=applnsite;
		this.properties=properties;
	}
	public void setUp() throws Exception {
		
		Proxy proxy = new Proxy(); // org.openqa.selenium.Proxy
		String zaproxyconfig=properties.getzaphostname()+":"+properties.getzapport();//"localhost:8080";
		proxy.setHttpProxy(zaproxyconfig);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("proxy", proxy);
    	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver/chromedriver.exe");
		driver = new ChromeDriver(capabilities);
		this.setDriver(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		testAll();
		
		driver.close();
		Thread.sleep(3000);
		driver.quit();
		System.out.println("SeleniumTest Done ");
	}
	public void tstMenuLinks() {
		checkMenuLinks("home.jsp");
	}
	
	public void tearDown() throws Exception {
		driver.close();
	}
	

	
	public void checkMenu(String linkText, String page) {
		sleep();
		WebElement link = driver.findElement(By.linkText(linkText));
		link.click();
		sleep();
		
		if ((site + page).equalsIgnoreCase(driver.getCurrentUrl()))
			System.out.println("CheckCurrentURL from " + linkText + ": PASS");
		else
			System.out.println("CheckCurrentURL from " + linkText + ": FAIL");
	}
	
	public void checkMenuLinks(String page) {
		driver.get(site + page);
		checkMenu("Home", "home.jsp");

		driver.get(site + page);
		checkMenu("About Us", "about.jsp");
		
		driver.get(site + page);
		checkMenu("Contact Us", "contact.jsp");	
	}
	

	


	public void tstSearch() {
		driver.get(site + "search.jsp?q=doo");
		sleep();		
	}
	
	protected WebDriver getDriver() {
		return driver;
	}

	protected void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	protected String getSite() {
		return site;
	}

	protected void setSite(String site) {
		this.site = site;
	}
	
	private void sleep() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.getMessage();
		}		
	}
	
	public void testAll() {
		tstMenuLinks();
	}

}
