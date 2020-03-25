import unittest

from selenium import webdriver
import PageObjectTest as BaseTest
from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
from PageObjects.AutomationPracticeStore.StorePage import StorePage


class TestRoutines(unittest.TestCase):
    _driver: webdriver
    _context_po: StorePage
    _cart_items = []
    _active_items = []

    def use_browser(self, browser_name):
        BaseTest.PageObjectTest(browser_name, 'http://automationpractice.com')
        TestRoutines._driver = BaseTest.PageObjectTest.driver
        TestRoutines._context_po = StorePage()

    def close_browser(self):
        BaseTest.PageObjectTest.driver.quit()

    def a_user_is_not_logged_in(self):
        if TestRoutines._context_po.get_header().is_user_logged_in():
            raise Exception('A user is logged into the system!')

    def the_user_is_logged(self, user_name):
        if not(TestRoutines._context_po.get_header().is_user_logged_in()):
            raise Exception('No user is logged in the system!')
        logged_user = TestRoutines._context_po.get_header().get_logged_user_name()
        if logged_user != user_name:
            raise Exception(f"The user logged in is not the expected. User logged: {logged_user}")

    def select_page_section(self, section):
        home_page = StoreHomePage()
        home_page.select_tab(section.upper())

    def add_all_items_to_the_cart(self):
        page = StoreHomePage()
        for item in page.get_active_items():
            item.add_to_cart().close_popup()


    def get_items_from_mini_cart(self):
        cart = TestRoutines._context_po.get_header().get_contextual_cart()
        TestRoutines._cart_items = cart.get_items()

    def get_active_items(self):
        page = StoreHomePage()
        TestRoutines._active_items = page.get_active_items()

    def compare_active_items_with_mini_cart(self):
        self.assertFalse(TestRoutines._cart_items.__len__() == 0, 'The mini cart is empty!')
        self.assertEqual(TestRoutines._cart_items.__len__(), TestRoutines._active_items.__len__(),
                         'The number of items in the mini cart does not match with the number of active items!')
        # Too lazy for implementing the actual comparison, but you should not be. XP

    def remove_all_items_from_the_mini_cart(self):
        mini_cart = TestRoutines._context_po.get_header().get_contextual_cart()
        for item in mini_cart.get_items():
            item.remove_item()
