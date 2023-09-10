package com.auto.testing.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.auto.testing.config.ZapProperties;

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
		
		//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.setCapability("proxy", proxy);
    	System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver/chromedriver.exe");
		driver = new ChromeDriver(options);
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
	
	public void GetProductTypes(String Objectname, String page) {
		try{
			
			checkMenu("Home", "home.jsp");
			sleep();
			

		List<WebElement> ProdTypes = driver.findElements(By.xpath(Objectname));
		
		for (WebElement ProdType : ProdTypes)
		{
			
			String ProdTypeName= ProdType.getText();
			sleep();
			System.out.println("ProdTypeName " + ProdTypeName);
			ProdType.click();
			sleep();
			int prodCnt=0;
			
			List<WebElement> Prods = driver.findElements(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr"));
			System.out.println("Prod Prods.size " + Prods.size());
//			for (int i=0;i<Prods.size();i++){
//				//WebElement Prod=Prods.get(i);
//				System.out.println("Prod i " +i);
//				prodCnt=i+1;
//				if (i==0){
//					String Col1=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/th[1]")).getText();
//					String Col2=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/th[2]")).getText();
//					String Col3=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/th[3]")).getText();
//					System.out.println("Prod ColName " + Col1+Col2+Col3);
//				}
//				else
//				{
//					String dat1=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/td[1]")).getText();
//					String dat2=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/td[2]")).getText();
//					String dat3=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]/center/table/tbody/tr["+prodCnt+"]/td[3]")).getText();
//					System.out.println("Prod values " + dat1+dat2+dat3);
//				}
//				
//			}
//			
		}
//		checkMenu("Home", "home.jsp");
//		sleep();
//		
		}catch(Exception e){
			System.out.println("Product types => " + Objectname+" : FAIL :"+e.getMessage());
		}
	}
	
	
	public void Login(String UserName, String Password) {
		sleep();
		try{
			
			checkMenu("Login", "login.jsp");
			
			driver.findElement(By.id("username")).sendKeys(UserName);
			driver.findElement(By.id("password")).sendKeys(Password);
			sleep();
			driver.findElement(By.id("submit")).click();
			sleep();
			String LoginMsg=driver.findElement(By.xpath("//table/tbody/tr[3]/td/table/tbody/tr/td[2]")).getText();
			//System.out.println("LoginMsg => " +LoginMsg);
			
			if ((LoginMsg).contains("You have logged in successfully"))
			{
				System.out.println("Login with User Details => " + UserName+" : "+Password + ": PASS");
				
				String User=driver.findElement(By.xpath("//table/tbody/tr[1]/td/table/tbody/tr/td[3]")).getText();
				if ((User).contains(UserName))
					System.out.println("Loggedin username => " + UserName+" is displayed :  PASS");
				else
					System.out.println("Loggedin username => " + UserName+" is not displayed :  FAIL");
				
			}
			else if ((LoginMsg).contains("You supplied an invalid name or password"))
				System.out.println("Login with User Details =>  " + UserName+" : "+Password + ": FAIL");
			
		}catch(Exception e){
			System.out.println("Login with User Details => " + UserName+" : "+Password + ": FAIL");
		}
	}
	
	public void checkMenuLinks(String page) {
		driver.get(site + page);
		checkMenu("Home", "home.jsp");

		driver.get(site + page);
		checkMenu("About Us", "about.jsp");
		
		driver.get(site + page);
		checkMenu("Contact Us", "contact.jsp");	
		
		driver.get(site + page);
		checkMenu("Login", "login.jsp");
		
		driver.get(site + page);
		checkMenu("Your Basket", "basket.jsp");	
		
		driver.get(site + page);
		checkMenu("Search", "search.jsp");	
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
		Login("raghuraman-k@hcl.com","WrongPwd");
		Login("WrongUsr","Welcome1");
		Login("<Script>Hi</Script>","Welcome1");
		Login("raghuraman-k@hcl.com","Welcome1");
		//GetProductTypes("//table/tbody/tr[3]/td/table/tbody/tr/td[1]/a","");
	}

}
