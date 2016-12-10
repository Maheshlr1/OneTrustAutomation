package com.onetrust.data.careerpagesetup
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.support.events.WebDriverEventListener
import org.openqa.selenium.*
import org.testng.ITestResult 
import org.testng.Reporter;
public class ActivityCapture implements WebDriverEventListener {
	WebDriver driver;
	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {

		System.out.println("After click "+arg0.toString());
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {

		System.out.println("After FindBy "+arg0.toString());
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {

		System.out.println("After navigating back "+arg0.toString());
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {

		System.out.println("After navigating forword "+arg0.toString());
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {

		System.out.println("After navigating "+arg0.toString());

		System.out.println("After navigating "+arg1.toString());
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {

		System.out.println("before click "+arg0.toString());
	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {

		System.out.println("before FindBY "+arg0.toString());
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {

		System.out.println("Before navigating back "+arg0.toString());
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		System.out.println("Before navigating Forword "+arg0.toString());
	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {

		System.out.println("Before navigating "+arg0.toString());
		System.out.println("Before navigating "+arg1.toString());
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
	}

	@Override
	public void onException(Throwable cause, WebDriver driver) {
		Throwable rootCause = cause;
		while(rootCause.getCause() != null &&  rootCause.getCause() != rootCause){
			 rootCause = rootCause.getCause();
   
	   System.out.println(rootCause.getStackTrace()[0].getClassName());
	   System.out.println(rootCause.getStackTrace()[0].getMethodName());
	   }

		System.out.println("Testcase done"+cause.toString());
		System.out.println("Testcase done"+driver.toString());
	}
	
	
	
	
	public void createScreenShot(WebDriver wd1,String subDir,String screenShotName){
		
				String propFileName = "ActivityCapture.class";
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

	
	
	public String getTestClassName(String testName) {
		String[] reqTestClassname = testName.split("\\.");
		int i = reqTestClassname.length - 1;
		System.out.println("Required Test Name : " + reqTestClassname[i]);
		return reqTestClassname[i];
	}
	
}
