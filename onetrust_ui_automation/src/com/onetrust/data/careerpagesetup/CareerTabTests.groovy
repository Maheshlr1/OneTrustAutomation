package com.onetrust.data.careerpagesetup

import java.awt.geom.Path2D.Iterator;
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import org.testng.*
import org.testng.annotations.*
import com.onetrust.data.uimap.OneTrustBasePage


public class CareerTabTests extends TestBase{


	//Reference
	
	
	OneTrustBasePage uibo
	TestBase tb
	WebDriverWait wait
	OneTrustUtil boutil
	WebElement elem
	
	
	CareerTabTests()
	{
		super
		uibo=new OneTrustBasePage()
		boutil=new OneTrustUtil()
		
	}
	



	@AfterMethod
	void afterTest(ITestResult result)
	{
		if(!result.isSuccess()&& wdbo!=null)
		{
			createScreenShot(wdbo,"CareerTabTests",result.getMethod().getMethodName()+".png")

		}

		if(wdbo!=null){
			quitBrowser(wdbo)
		}
	}


	//===========================================================



	@Test
	public void careerTest()
	{
		Reporter.log("Printing the Job listed under career page for each cities")
		wdbo=loginToBrowser()
		boutil.careerPagetesting(wdbo);
		quitBrowser(wdbo)
   }


	

}
