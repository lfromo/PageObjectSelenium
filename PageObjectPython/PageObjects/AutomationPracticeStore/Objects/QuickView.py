from typing import Optional

from selenium.webdriver.remote import webelement
from selenium.webdriver.support.expected_conditions import visibility_of_element_located, presence_of_element_located
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.Objects.POExceptions import NotLoggedInException
from PageObjects.AutomationPracticeStore.Objects.ProductInCartPopup import ProductInCartPopup
from PageObjects.BasePageObject import BasePageObject


class QuickView(BasePageObject):

    __current_picture = (By.ID, 'bigpic')
    __pictures_list = (By.CSS_SELECTOR, 'ul#thumbs_list_frame li a')
    __label_display_all_pictures = (By.NAME, 'resetImages')

    __product_name = (By.TAG_NAME, 'h1')
    __product_model = (By.CSS_SELECTOR, 'p#product_reference span.editable')
    __product_condition = (By.CSS_SELECTOR, 'p#product_condition span.editable')
    __product_description = (By.ID, 'short_description_content')
    __product_availability_date = (By.ID, 'availability_date_value')
    __product_out_of_stock = (By.ID, 'oosHook')  # Not found something where this implementation is invoked
    __social_button_list = (By.CLASS_NAME, 'socialsharing_product')

    __label_price = (By.ID, 'our_price_display')
    __label_discount = (By.ID, 'reduction_percent_display')
    __label_old_price = (By.ID, 'old_price_display')
    __quantity_text_input = (By.ID, 'quantity_wanted')
    __quantity_subtract = (By.CLASS_NAME, 'product_quantity_down')
    __quantity_add = (By.CLASS_NAME, 'product_quantity_up')
    __size_list = (By.ID, 'group_1')
    __color_list = (By.ID, 'color_to_pick_list')
    __button_add_to_cart = (By.NAME, 'Submit')
    __button_wish_list = (By.ID, 'wishlist_button')

    __frame_container = (By.CSS_SELECTOR, 'iframe.fancybox-iframe')
    __self_body_in_frame = (By.ID, 'product')
    __popup_message = (By.CLASS_NAME, 'fancybox-skin')

    def __init__(self):
        wait = WebDriverWait(self.base_page_driver, 8)
        wait.until_not(visibility_of_element_located((By.CSS_SELECTOR, 'div#fancybox-loading')))
        wait.until(presence_of_element_located(self.__frame_container))
        self.base_page_driver.switch_to.frame(self.base_page_driver.find_element(*self.__frame_container))
        self.__we = self.base_page_driver.find_element(*self.__self_body_in_frame)

    def get_current_picture_source(self):
        picture = self.__we.find_element(*self.__current_picture)
        return str(picture.get_attribute('src'))

    def get_thumbnail_pictures_source_list(self):
        pictures = self.__we.find_elements(*self.__pictures_list)
        pic_links = []
        for pic in pictures:
            if pic.is_displayed():
                pic_links.append(pic.get_attribute('href'))
        return pic_links

    def get_current_thumbnail_picture(self):
        thumbs = self.__we.find_elements(*self.__pictures_list)
        found: Optional[webelement] = next((clr for clr in thumbs if str(clr.get_attribute('class')).casefold()
                                            == 'fancybox shown'), None)
        if found is None:
            raise NoSuchElementException(msg='There was a problem finding the currently selected thumbnail')
        else:
            return str(found.find_element_by_tag_name('img').get_attribute('src'))

    def display_all_thumbnail_pictures(self):
        found = self.__we.find_elements(*self.__label_display_all_pictures)
        if found.__len__ == 0:
            raise NoSuchElementException(msg='All available picture thumbnails are already displayed')
        else:
            found[0].click()
            return self

    def are_all_thumbnails_pictures_displayed(self):
        found = self.__we.find_elements(*self.__label_display_all_pictures)
        if found.__len__ > 0:
            return True
        else:
            return False

    def get_product_name(self):
        return str(self.__we.find_element(*self.__product_name).text)

    def get_product_model(self):
        return str(self.__we.find_element(*self.__product_model).text)

    def get_product_condition(self):
        return str(self.__we.find_element(*self.__product_condition).text)

    def get_product_description(self):
        return str(self.__we.find_element(*self.__product_description).text)

    def get_social(self):
        pass  # This needs to be implemented separately

    def get_price(self):
        return float(self.__we.find_element(*self.__label_price).text[1:])

    def get_discount(self):
        found = self.__we.find_elements(*self.__label_discount)
        if found.__len__() == 0 or found[0].text == '':
            return 0
        else:
            return int(found[0].text.split('%')[0][1:])

    def get_price_before_discount(self):
        found = self.__we.find_elements(*self.__label_old_price)
        if found.__len__() == 0 or found[0].text == '':
            return 0.0
        else:
            return float(found[0].text[1:])

    def get_current_quantity(self):
        text_box = self.__we.find_element(*self.__quantity_text_input)
        return int(text_box.get_attribute('value'))

    def set_quantity(self, quantity):
        input_box = self.__we.find_element(*self.__quantity_text_input)
        input_box.clear()
        input_box.send_keys(quantity)
        return self

    def get_available_sizes(self):
        sizes = []
        size_list = self.__we.find_element(*self.__size_list)
        options = Select(size_list).options
        for opt in options:
            sizes.append(opt.text)
        return sizes

    def set_size(self, size):
        size_list = self.__we.find_element(*self.__size_list)
        Select(size_list).select_by_value(size)
        return self

    def increment_quantity(self):
        self.__we.find_element(*self.__quantity_add).click()
        return self

    def decrement_quantity(self):
        self.__we.find_element(*self.__quantity_subtract).click()
        return self

    def get_available_colors(self):
        colors_list = []
        colors_available = self._get_colors_available()
        for color in colors_available:
            colors_list.append([str(color.get_attribute('name')), str(color.get_attribute('style')).split(': ')[1]])
        return colors_list

    def select_color_by_name(self, color):
        self._select_color(color, True)
        return self

    def select_color_by_code(self, color):
        self._select_color(color, False)
        return self

    def add_to_cart(self):
        self.__we.find_element(*self.__button_add_to_cart).click()
        self.base_page_driver.switch_to.default_content()
        return ProductInCartPopup()

    def add_to_wish_list(self):
        self.__we.find_element(*self.__button_wish_list).click()
        try:
            wait = WebDriverWait(self.base_page_driver, 4)
            wait.until(visibility_of_element_located(self.__popup_message))
            text = self.get_pop_up_message()
            if text == 'You must be logged in to manage your wishlist.':
                raise NotLoggedInException(message=text)
        except TimeoutException as Ex:
            raise TimeoutException(msg='The pop up message after adding to the wish list did not appeared',
                                   stacktrace=Ex.stacktrace)
        self.close_pop_up_message()

    def get_pop_up_message(self):
        return str(self.__we.find_element(*self.__popup_message).text)

    def close_pop_up_message(self):
        self.__we.find_element(*self.__popup_message).find_element_by_tag_name('a').click()

    def _select_color(self, color, by_name):
        colors_available = self._get_colors_available()
        if colors_available.__len__() == 0:
            raise NoSuchElementException(msg='This Quick View window does not contain any color to choose from')
        if by_name is True:
            found = next((clr for clr in colors_available if str(clr.get_attribute('name')).casefold() ==
                          str(color).casefold()), None)
        elif by_name is False:
            found = next((clr for clr in colors_available if str(clr.get_attribute('style')).casefold()
                         .find(str(color).casefold()) > 0), None)
        if found is None:
            raise NoSuchElementException(msg=f"The color '{color}' was not found among the colors available")
        else:
            found.click()

    def _get_colors_available(self):
        color_section = self.__we.find_element(*self.__color_list)
        return color_section.find_elements_by_tag_name('a')
