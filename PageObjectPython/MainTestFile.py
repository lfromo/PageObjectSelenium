from selenium import webdriver
import BrowserConfiguration as Config


class MainTestFile(object):
    _driver: webdriver

    def use_browser(self, browser_name):
        cfg = Config.BrowserConfiguration.local_configuration(browser_name, None)
        MainTestFile._driver = cfg.get_driver()

    def a_silly_test_case(self):
        MainTestFile._driver.get('https://www.google.com.mx')
        elem = MainTestFile._driver.find_element_by_name('q')
        elem.send_keys('Python for Testing with Robot Framework')
        elem.submit()

    def go_to_the_page(self, url):
        MainTestFile._driver.get(url)

    def go_to_home_page(self):
        MainTestFile._driver.get("https://saucelabs.com/")

    def close_browser(self):
        MainTestFile._driver.quit()
