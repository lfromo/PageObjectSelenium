*** Settings ***
Library	MainTestFile.py
#Test Setup		Use browser	chrome
Suite Teardown		Close browser

*** Test Cases ***
Yet another silly test case
	[Documentation]		This is a different silly test case.
	[Tags]		thee	da useless tag
	A silly test case
	Go to home page

The last one in same suite
	[Documentation]		This is yet another different silly test case in the same suite file
	[Tags]		waat	da tag with spaces
	A silly test case