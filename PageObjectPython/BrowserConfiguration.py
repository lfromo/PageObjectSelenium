from selenium import webdriver
from selenium.common.exceptions import WebDriverException


class BrowserConfiguration(object):
    """ The purpose of this class is to configure and handle instance of a web driver for a particular web browser """

    def __init__(self, driver):
        self.__driver = driver
        self.__driver.maximize_window()

    @classmethod
    def local_configuration(cls, browser_name, capabilities):
        switcher = {
            "CHROME": webdriver.Chrome,
            "FIREFOX": webdriver.Firefox,
            "IE11": webdriver.Ie,
            "EDGE": webdriver.Edge
        }
        driver_class = switcher.get(browser_name.upper(), WebDriverException)
        if driver_class == WebDriverException:
            supported_browsers = "\nSupported browsers are: CHROME, FIREFOX, IE11, and EDGE"
            raise driver_class('The given browser is unknown or unsupported: ' + browser_name + supported_browsers)
        else:
            if capabilities is None:
                return cls(driver_class())
            elif driver_class == webdriver.Chrome:
                return cls(driver_class(desired_capabilities=capabilities))
            else:
                return cls(driver_class(capabilities=capabilities))

    @classmethod
    def remote_browser(cls, hub_url, capabilities):
        print('This is for configuring a remote browser using a map and the URL for the Hub')
        return cls(webdriver.Remote(command_executor=hub_url, desired_capabilities=capabilities))

    def get_driver(self):
        return self.__driver
