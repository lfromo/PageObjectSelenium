from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.expected_conditions import staleness_of

from PageObjects.BasePageObject import BasePageObject


class MiniCartItem(BasePageObject):

    __thumbnail_picture_locator = (By.CSS_SELECTOR, 'a.cart-images img')
    __quantity_locator = (By.CLASS_NAME, 'quantity')
    __product_name_locator = (By.CLASS_NAME, 'cart_block_product_name')
    __attributes_locator = (By.CLASS_NAME, 'product-atributes')
    __total_locator = (By.CLASS_NAME, 'price')
    __close_button_locator = (By.CLASS_NAME, 'ajax_cart_block_remove_link')

    def __init__(self, mini_item):
        self.__we = mini_item

    def get_thumbnail_image_link(self):
        return str(self.__we.find_element(*self.__thumbnail_picture_locator).get_attribute('src'))

    def get_quantity(self):
        return int(self.__we.find_element(*self.__quantity_locator).text)

    def get_product_full_name(self):
        return str(self.__we.find_element(*self.__product_name_locator).get_attribute('title'))

    def get_product_attributes(self):
        attr_list = []
        attributes = str(self.__we.find_element(*self.__attributes_locator).text)
        attrs = attributes.split(', ')
        for attr in attrs:
            attr_list.append(attr)
        return attr_list

    def get_product_total_price(self):
        return float(self.__we.find_element(*self.__total_locator).text[1:])

    def remove_item(self):
        wait = WebDriverWait(self.base_page_driver, 3)
        self.__we.find_element(*self.__close_button_locator).click()
        wait.until(staleness_of(self.__we), 'The item took too long to be removed!')
