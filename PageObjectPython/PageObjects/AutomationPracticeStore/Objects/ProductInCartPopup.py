from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.expected_conditions import visibility_of_element_located
from selenium.webdriver.support.expected_conditions import invisibility_of_element_located

from PageObjects.AutomationPracticeStore.CheckoutPage import CheckoutPage
from PageObjects.BasePageObject import BasePageObject


class ProductInCartPopup(BasePageObject):

    __label_product_name = (By.ID, 'layer_cart_product_title')
    __label_product_attributes = (By.ID, 'layer_cart_product_attributes')
    __label_product_quantity = (By.ID, 'layer_cart_product_quantity')
    __label_product_total_price = (By.ID, 'layer_cart_product_price')

    __button_continue_shopping = (By.CLASS_NAME, 'continue')
    __button_proceed_checkout = (By.CSS_SELECTOR, 'a.btn-default')
    __button_close_popup = (By.CLASS_NAME, 'cross')
    __number_of_items_in_cart = (By.CLASS_NAME, 'ajax_cart_quantity')
    __label_total_products_amount = (By.CLASS_NAME, 'ajax_block_products_total')
    __label_total_shipping_amount = (By.CSS_SELECTOR, 'div.layer_cart_row span.ajax_cart_shipping_cost')
    __label_total_final_amount = (By.CSS_SELECTOR, 'div.layer_cart_row span.ajax_block_cart_total')

    __self_locator = (By.CSS_SELECTOR, 'div#layer_cart div.clearfix')

    __PLURAL_PHRASE_LOCATOR = "ajax_cart_product_txt_s"
    __SINGULAR_PHRASE_LOCATOR = "ajax_cart_product_txt"

    def __init__(self):
        self.__wait = WebDriverWait(self.base_page_driver, 4)
        self.__wait.until(visibility_of_element_located(self.__self_locator))
        self.__we = self.base_page_driver.find_element(*self.__self_locator)

    def get_product_name(self):
        return self.__we.find_element(*self.__label_product_name).text

    def get_product_quantity(self):
        return self.__we.find_element(*self.__label_product_quantity).text

    def get_product_total(self):
        return self.__we.find_element(*self.__label_product_total_price).text[1:]

    def get_product_attributes(self):
        attrs = str(self.__we.find_element(*self.__label_product_attributes).text).replace(' ', '')
        return attrs.split(',')

    def get_total_products_price(self):
        return self.__we.find_element(*self.__label_total_products_amount).text[1:]

    def get_total_shipping_cost(self):
        return self.__we.find_element(*self.__label_total_shipping_amount).text[1:]

    def get_cart_final_amount(self):
        return self.__we.find_element(*self.__label_total_final_amount).text[1:]

    def get_total_items_phrase(self):
        locator = self.__PLURAL_PHRASE_LOCATOR if self.get_total_items_in_cart() > 1 else self.__SINGULAR_PHRASE_LOCATOR
        return self.__we.find_element_by_class_name(locator).text

    def get_total_items_in_cart(self):
        return int(self.base_page_driver.find_element(*self.__number_of_items_in_cart).text)

    def proceed_to_checkout(self):
        self.__we.find_element(*self.__button_proceed_checkout).click()
        return CheckoutPage()

    def continue_shopping(self):
        self.__we.find_element(*self.__button_continue_shopping).click()
        self.__wait.until(invisibility_of_element_located(self.__self_locator))

    def close_popup(self):
        self.__we.find_element(*self.__button_close_popup).click()
        self.__wait.until(invisibility_of_element_located(self.__self_locator))
