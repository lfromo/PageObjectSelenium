*** Settings ***
Library	MainTestFile.py
Suite Setup		Take the given browser
Test Setup		Go to home page
# Test Teardown	Close browser

*** Test Cases ***
No name TC for nothing
	[Documentation]		This is a silly test case with Selenium Webdriver.
	[Tags]		waat	thee
	A silly test case
	Go to the page	https://www.sopitas.com

*** Keywords ***
Take the given browser
	Use browser	chrome