package com.onetrust.data.careerpagesetup

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.events.EventFiringWebDriver
import org.openqa.selenium.support.pagefactory.ByAll
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.*
import org.testng.annotations.*

import java.util.concurrent.TimeUnit
import com.onetrust.data.uimap.OneTrustBasePage
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import org.openqa.selenium.*
import org.apache.commons.io.FileUtils
public class TestBase{


	EventFiringWebDriver wdbo;
	public EventFiringWebDriver getWdbo() {
		return wdbo;
	}

	public void setWdbo(EventFiringWebDriver wdbo) {
		this.wdbo = wdbo;
	}


	//WebDriver wdwc=null
	//Reference
	WebDriverWait waitbo

	OneTrustUtil onetrustUtil

	Properties otProperties
	OneTrustBasePage bp
	URL otprourl =null
	URL envprourl=null

	//Map
	def map

	@BeforeClass
	void doSetup(){


		onetrustUtil=new OneTrustUtil()
		try {
			otprourl = ClassLoader.getSystemResource("testdata/properties/OneTrust.properties");
			
			otProperties=new Properties();
			otProperties.load(otprourl.openStream());
			bp=new OneTrustBasePage()
			

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	

	public EventFiringWebDriver loginToBrowser() throws MalformedURLException, InterruptedException{
		WebDriver driver=null
		String logurl
		String browserType
		String chromedriverPath
		

		logurl=otProperties.getProperty("ProductUrl");
		browserType=otProperties.getProperty("BrowserType");
		chromedriverPath=otProperties.getProperty("ChromedriverPath");
		
		if(browserType.equalsIgnoreCase("Firefox"))
		{
		  driver=new FirefoxDriver()
		}
		
		else if(browserType.equalsIgnoreCase("Chrome"))
		{
			ChromeOptions options = new ChromeOptions();
			options.addArguments("chrome.switches","--disable-extensions");
			System.setProperty("webdriver.chrome.driver",chromedriverPath)
			driver=new ChromeDriver()
		}
        EventFiringWebDriver wd=new EventFiringWebDriver(driver);
        ActivityCapture handle=new ActivityCapture();
		wd.register(handle)
		wd.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS)
		wd.navigate().to(logurl)
		wd.manage().window().maximize()

     for (int i=0;i<10;i++) {
			if(wd.findElements(By.xpath(bp.OTCareer_LNK)).size()>=1){
				println("element is found")
				break;

			}else{
				println("element not found")
				sleep(2000)
				i+=1
			}

		}
        wd.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS)
		wait=new WebDriverWait(wd, 30000)
		return wd

	}

	public int getElementSizeByXpath(WebDriver driver, String xPath) {
		int size1 = driver.findElements(By.xpath(xPath)).size()
		return size1
	}
	public int getElementSizeById(WebDriver driver, String id) {
		int size1 = driver.findElements(By.id(id)).size()
		return size1
	}


	public void quitBrowser(WebDriver driver){
		driver.quit()
		wdbo=null

	}

	public String getTextByXpath(WebDriver driver,String xpathKey)
	{
		return driver.findElement(By.xpath(xpathKey)).getText().trim();
	}

	public String getPropertyValueByXpath(WebDriver driver,String xpathKey,String attribute)
	{
		return driver.findElement(By.xpath(xpathKey)).getAttribute(attribute);
	}

	public String getPropertyValueByid(WebDriver driver,String id,String attribute)
	{
		return driver.findElement(By.id(id)).getAttribute(attribute);
	}


	public void createScreenShot(WebDriver wd1,String subDir,String screenShotName){

		String propFileName = "TestBase.class";
		String path = getClass().getResource(propFileName).getPath();
		File file = new File(path);
		String absPath = file.getAbsolutePath();
		String destDir = null;

		String OS = System.getProperty("os.name").toLowerCase();
		boolean isWindows = OS.indexOf("win") >= 0;

		if(isWindows){
			int index = absPath.indexOf("bin", 0);
			destDir = absPath.substring(0,index)+"reports/screenshots/"+ subDir;
		}else{
			int index = absPath.indexOf(":", 0);
			String uitestPath = absPath.substring(index+1);
			index = uitestPath.indexOf("lib", 0);
			destDir=uitestPath.substring(0,index)+"reports/screenshots/" + subDir;
		}


		if(destDir!=null){
			File directory  = new File(destDir);
			String destFile = null;
			if(directory.exists())
			{
				destFile = destDir + "/" + screenShotName;
			}
			else
			{
				boolean success = directory.mkdirs();
				if(success)
					destFile = destDir + "/" + screenShotName;
			}

			if(destFile!=null)
			{

				Reporter.log("Screenshot Location: "+destFile);
				File scrFile = ((TakesScreenshot)wd1).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(destFile));
			}
		}


	}

}


