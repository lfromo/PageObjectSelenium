*** Settings ***
Documentation    This is just a test suite for learning purposes
Library	TestRoutines.py
Suite Setup		Use the given browser
Suite Teardown  Close browser

*** Test Cases ***
Verifying all best seller products contain all required information
    [Tags]    TESTING   BEST SELLERS
    Given a user is not logged in
    When the best sellers section is selected
    # Then all elements of each thumbnail is displayed

Adding all popular products to the cart and removing them later
    [Tags]    TESTING   POPULAR
    Given a user is not logged in
    When the popular section is selected
    And add all items to the cart
    Then ensure all items are in the mini cart
    And remove all items from the mini cart

*** Keywords ***
Use the given browser
    Use browser	chrome

the ${section_name} section is selected
    select page section     ${section_name}

ensure all items are in the mini cart
    get items from mini cart
    get active items
    compare active items with mini cart