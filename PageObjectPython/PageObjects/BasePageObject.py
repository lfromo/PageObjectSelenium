from selenium import webdriver
from selenium.webdriver import ActionChains
import PageObjectTest as BaseTest


class BasePageObject(object):
    """ The purpose of this class is to serve as foundation to get the currently used web driver after any
    instantiation of a child element. Before any class derived from this one gets instantiated it is necessary
    that an instance of PageObjectTest has been created beforehand.
    """

    __driver: webdriver

    def __init__(self):
        BasePageObject.__driver = BaseTest.PageObjectTest.driver  # TODO: Fix this ugly reference to a public variable.

    @property
    def base_page_driver(self):
        return BasePageObject.__driver

    def _hover_over_elements_then_click_last(self, final_element, *elements):
        self._scroll_to_element(elements[0])
        act = ActionChains(self.__driver)
        for we in elements:
            act = act.move_to_element(we)
        act.click(final_element).perform()

    def _hover_over_elements(self, *elements):
        self._scroll_to_element(elements[0])
        act = ActionChains(self.__driver)
        for we in elements:
            act = act.move_to_element(we).perform()

    def _scroll_to_element(self, element):
        self.__driver.execute_script("arguments[0].scrollIntoView();", element)  # Necessary for FIREFOX

    def _javascript_click(self, element):
        self.__driver.execute_script("arguments[0].click();", element)
