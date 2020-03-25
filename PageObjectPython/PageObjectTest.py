import unittest
from selenium import webdriver
import BrowserConfiguration as Config


class PageObjectTest(unittest.TestCase):
    driver: webdriver

    def __init__(self, browser_name, start_url):
        cfg = Config.BrowserConfiguration.local_configuration(browser_name, None)
        PageObjectTest.driver = cfg.get_driver()
        PageObjectTest.driver.get(start_url)
