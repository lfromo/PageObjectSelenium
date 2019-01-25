# PageObjectSelenium
Repository for training and to serve as a starting point for front-end test automation projects applied to Web applications using Selenium with a Page Object approach. 
The supported browsers are Firefox, Chorme, Internet Explorer 11, and Edge.

The purposes of these projects are the following:
 * To serve as simple but deliverable test framework that can be implemented to be used with a different set of Page Objects.
 * To serve as a learning source, for both showing how a simple web application's front-end test framework is implemented and how Page Objects can be designed.
 * To serve as a starting point to either extend them or change them for a particular need.
 * To show how the Test Scripts could be implemented, and to show how the Test Frameworks can be used.

Both projects are using PHPTravels.net as the Web application to serve as an example.

## PageObjectJava:
Using Java 8, Selenium Java bindings along its respective support libraries, TestNG, log4j2, among others. The dependencies for this project are being handled by a Gradle wrapper built in in the project.
It is suggested to use Idea's IntelliJ as the IDE.

### USAGE: 
 The framework uses TestNG to execute the Test suite,  which uses an XML document to execute the Tests. A basic example is the following:
 
 <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Selenium/Java Framework Example Suite">
	<parameter name="Browser" value="EDGE"/>
	<parameter name="Url" value="https://www.phptravels.net"/>
  <test name="PHP Travels" >
    <classes>
        <class name="Tests.PHPTravels1"/>
        <class name="Tests.PHPTravels2"/>
    </classes>
  </test>
</suite>

The parameters can be removed from here and handled by the TEstNG plugin in the IDE to provide them along other configurations.

## PageObjectDotNet:
Using .Net Framework 4.7.2 and NuGet as the package manager, being configured as a Class library application. 
Applying the corresponding Selenium bindings, NUnit v3 along its corresponding TestAdapter, and log4net.


### IMPORTANT:
Please refer to the following links for more information on how to properly perform the framework setup.

Browsers WebDriver Configurations:
https://sites.google.com/a/chromium.org/chromedriver/getting-started
https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/
https://developer.mozilla.org/en-US/docs/Web/WebDriver
https://www.seleniumhq.org/download/ <-----Look for "The Internet Explorer Driver Server" section in the page.
