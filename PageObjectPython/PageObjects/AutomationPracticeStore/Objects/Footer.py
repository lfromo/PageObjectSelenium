from selenium.webdriver.common.by import By

from PageObjects.BasePageObject import BasePageObject


class Footer(BasePageObject):

    __self_locator = (By.ID, 'footer')

    def __init__(self):
        self.__we = super().base_page_driver.find_element(*self.__self_locator)
