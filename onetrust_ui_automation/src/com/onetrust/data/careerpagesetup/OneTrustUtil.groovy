package com.onetrust.data.careerpagesetup;

import com.onetrust.data.uimap.OneTrustBasePage
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.pagefactory.ByAll
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*



public class OneTrustUtil {


	boolean rc;

	Properties dt= null;
	Properties env=new Properties();
    OneTrustBasePage bp=new OneTrustBasePage()
	WebDriverWait wait
	Properties  otProperties
	URL otprourl
	String tabstatus
	boolean isalertpresent
	

	OneTrustUtil(){
		otProperties=new Properties();
		otprourl = ClassLoader.getSystemResource("testdata/properties/OneTrust.properties");
		otProperties.load(otprourl.openStream());

	}



	public void careerPagetesting(WebDriver driver)
	{

		
		wait=new WebDriverWait(driver, 30000)
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(bp.OTCompany_LNK))))
		actionClickByXpath(driver,bp.OTCompany_LNK,bp.OTCareer_LNK)
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(bp.Citieslist_LNK))))

		def cities=driver.findElements(By.xpath(bp.Citieslist_LNK)).size()
		println "The total cities under careers page is :"+ cities
        
		println "Now printing the openings under each cities:"

		driver.switchTo().defaultContent()

		if(cities>1)
		{
			for(int i =1;i<=cities;i++)   //===========Total cities count=====
			{
				String cityLists= driver.findElement(By.xpath(bp.CitySpecific_LNK+"/li[$i]")).getText().toLowerCase().toString()

				println "The current city is: "+cityLists



				if(cityLists.startsWith("san"))
				{
					cityLists="san-francisco"
				}

				if(cityLists!='atlanta')
				{
					println bp.CitySpecific_LNK+"/li[$i]/a"

					javaScriptClickByXpath(driver,bp.CitySpecific_LNK+"/li[$i]/a")

				}

				println bp.JobsTitleRowCount_TXT.replace('##', cityLists)
				
				def rowCount=driver.findElements(By.xpath(bp.JobsTitleRowCount_TXT.replace('##', cityLists))).size()
				println "The rowCount under each city is: "+rowCount

				for (int j=1; j<=rowCount;j++) //==========Total row count=========
				{


					def  colCount=driver.findElements(By.xpath(bp.JobsRowAndTitle_TXT.replace('##', cityLists)+"/div[$j]/div[contains(@class,'col-sm')]")).size()
					println "The column Count under each city is: "+colCount

					for(int k=1;k<=colCount;k++)  //=======Total column count=========
					{


						def title=driver.findElement(By.xpath(bp.JobsRowAndTitle_TXT.replace('##', cityLists)+"/div[$j]/div[$k]/descendant::h3")).getText().toString()

						println "The category of the opening is: " +title

						def  jobscount=driver.findElements(By.xpath(bp.JobsRowAndTitle_TXT.replace('##', cityLists)+"/div[$j]/div[$k]/descendant::ul/li")).size()
						println "The jobscount under this "+ title +" is "+jobscount

						for(int k1=1;k1<=jobscount;k1++)
						{
							def  jobsopeningslist=driver.findElement(By.xpath(bp.JobsRowAndTitle_TXT.replace('##', cityLists)+"/div[$j]/div[$k]/descendant::ul/li[$k1]/a")).getText()
							println "The jobsopeningslist under: "+ title +" is " +jobsopeningslist
						}


					}




				}
			}


		}
	}



	public void selectFromDropDownList(WebDriver wd,String objectname,String selecttype,String selectvalue){
		Select sel=new Select(wd.findElement( new ByAll(By.name(objectname),By.xpath(objectname),By.id(objectname))))
		if(selecttype.equalsIgnoreCase("value")){
			sel.selectByValue(selectvalue)
		}else if(selecttype.equalsIgnoreCase("text")){
			sel.selectByVisibleText(selectvalue)
		}
	}
	
	public void selectFromDropDownListByid(WebDriver wd,String objectname,String selecttype,String selectvalue){
		Select sel=new Select(wd.findElement(By.id(objectname)))
		if(selecttype.equalsIgnoreCase("value")){
			sel.selectByValue(selectvalue)
		}else if(selecttype.equalsIgnoreCase("text")){
			sel.selectByVisibleText(selectvalue)

		}

	}

	public void actionClickByXpath(WebDriver wd,String xpathMainMenu,String xpathSubMenu){
		Actions action = new Actions(wd);
		WebElement mainMenu = wd.findElement(By.xpath(xpathMainMenu));
		action.moveToElement(mainMenu).moveToElement(wd.findElement(By.xpath(xpathSubMenu))).click().build().perform();
	}

	public void javaScriptClickByXpath(WebDriver wd,String xpath){

		WebElement element		= wd.findElement(By.xpath(xpath));
		JavascriptExecutor js	= (JavascriptExecutor)wd;
		js.executeScript("arguments[0].click();", element);
	}

	public void javaScriptClick(WebDriver wd,String id){
		JavascriptExecutor js = (JavascriptExecutor) wd;
		String elem="document.getElementById(\""+id+"\").click()"
		js.executeScript(elem);
	}

}
