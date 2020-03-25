from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.Objects.MiniCart import MiniCart
from PageObjects.BasePageObject import BasePageObject


class Header(BasePageObject):

    __categories_menu_locator = (By.XPATH, '//*[@id=\'block_top_menu\']/ul/li/a')
    __cart_element_locator = (By.CSS_SELECTOR, 'div.shopping_cart a')
    __search_box_input_locator = (By.ID, 'search_query_top')

    __login_button = (By.CLASS_NAME, 'login')
    __account_button = (By.CLASS_NAME, 'account')
    __sign_out_button = (By.CLASS_NAME, 'logout')

    __self_locator = (By.ID, 'header')

    def __init__(self):
        self.__we = self.base_page_driver.find_element(*self.__self_locator)
        self._hover_over_elements(self.__we)

    def search(self, search_term):
        search_box = self.__we.find_element(*self.__search_box_input_locator)
        search_box.send_keys(search_term)
        search_box.submit()
        pass  # TODO: Return some page that inherits from StorePage class.

    def get_available_categories(self):
        available_categories = []
        categories = self.__we.find_elements(*self.__categories_menu_locator)
        for dept in categories:
            available_categories.append(str(dept.text))
        return available_categories

    def go_to_category(self, category_name):
        categories = self.__we.find_elements(*self.__categories_menu_locator)
        found = filter(lambda itm: itm.text == category_name, categories).__next__()
        if found is None:
            raise NoSuchElementException(msg=f"The department named '{category_name}' was not found")
        else:
            found.click()
            pass  # return DepartmentPage()

    def get_category_submenu(self, category_name, submenu_type):
        pass

    def go_to_cart(self):
        cart_item = self.__we.find_element(*self.__cart_element_locator)
        cart_item.click()
        pass  # return CartPage()

    def get_contextual_cart(self):
        cart_item = self.__we.find_element(*self.__cart_element_locator)
        self._hover_over_elements(cart_item)
        return MiniCart()

    def is_user_logged_in(self):
        return self.__we.find_elements(*self.__login_button).__len__() == 0 \
               and self.__we.find_elements(*self.__account_button).__len__() == 1 \
               and self.__we.find_elements(*self.__sign_out_button).__len__() == 1

    def get_logged_user_name(self):
        if not(self.is_user_logged_in()):
            raise NoSuchElementException(msg='No user seems to be logged in')
        return str(self.__we.find_element(*self.__account_button).text)

    def sign_in(self):
        found = self.__we.find_elements(*self.__login_button)
        if found.__len__() == 0:
            raise NoSuchElementException(msg='The button for signing in was not found. A user seems to be already '
                                             'logged in the system')
        found[0].click()
        from PageObjects.AutomationPracticeStore.AuthenticationPage import AuthenticationPage
        return AuthenticationPage()

    def sign_out(self):
        if not(self.is_user_logged_in()):
            raise NoSuchElementException(msg='No user seems to be logged in')
        self.__we.find_element(*self.__sign_out_button).click()
