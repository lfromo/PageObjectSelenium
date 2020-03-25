from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.expected_conditions import visibility_of_element_located

from PageObjects.AutomationPracticeStore.Objects.MiniCartItem import MiniCartItem
from PageObjects.BasePageObject import BasePageObject


class MiniCart(BasePageObject):

    __mini_items_locator = (By.TAG_NAME, 'dt')
    __label_shipping = (By.CLASS_NAME, 'cart_block_shipping_cost')
    __label_total = (By.CLASS_NAME, 'cart_block_total')
    __button_checkout = (By.ID, 'button_order_cart')

    __mini_cart_element_locator = (By.CLASS_NAME, 'cart_block_list')

    def __init__(self):
        try:
            wait = WebDriverWait(self.base_page_driver, 3)
            wait.until(visibility_of_element_located(self.__button_checkout))
            self.__we = self.base_page_driver.find_element(*self.__mini_cart_element_locator)
        except TimeoutException:
            raise TimeoutException(msg='The context cart was not displayed')

    def get_items(self):
        mini_items = []
        items = self.__we.find_elements(*self.__mini_items_locator)
        for itm in items:
            mini_items.append(MiniCartItem(itm))
        return mini_items

    def get_shipping_cost(self):
        txt = self.__we.find_element(*self.__label_shipping).text
        if txt == 'Free shipping!':
            return 0.0
        return float(txt[1:])

    def get_total(self):
        txt = self.__we.find_element(*self.__label_total).text[1:]
        return float(txt)
