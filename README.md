# OneTrustAutomation
AutomationProjects

Requirements:

 Jave 1.8.x,groovy 2.0.x,gradle 3.2.x,testNg,ANT

Guide lines: [Complete package and Platform independent]

Configuration:

1. Clone the OneTrustAutomation project to local directory.

2. Update the url and chromedriver path under oneTrust.properties file.

3. Navigate to build folder and update the project directory path and output path[\onetrust_ui_automation\build]

4. open command prompt and execute  "gradle build-tests"

5. Now this will generate the build under testOutput folder[onetrust_ui_automation\testOutput],unzip folder.

6. Navigate to run-test under testOutput folder and open command prompt and execute  "gradle run-tests"

7. Now execution will start in chrome browser and reports also gets updated.


