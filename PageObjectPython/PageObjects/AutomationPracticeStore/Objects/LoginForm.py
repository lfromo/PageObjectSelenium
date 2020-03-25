from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.Objects.POExceptions import ErrorMessageException
from PageObjects.BasePageObject import BasePageObject
from PageObjects.AutomationPracticeStore.AccountPage import AccountPage


class LoginForm(BasePageObject):

    __input_email_address = (By.ID, 'email')
    __input_password = (By.ID, 'passwd')
    __link_forgot_your_password = (By.CSS_SELECTOR, 'p.lost_password a')
    __button_sign_in = (By.ID, 'SubmitLogin')

    def set_existent_email_address(self, email_address):
        self.base_page_driver.find_element(*self.__input_email_address).clear()
        self.base_page_driver.find_element(*self.__input_email_address).send_keys(email_address)
        return self

    def get_existent_email_address(self):
        return self.base_page_driver.find_element(*self.__input_email_address).get_attribute('value')

    def set_password(self, password):
        self.base_page_driver.find_element(*self.__input_password).clear()
        self.base_page_driver.find_element(*self.__input_password).send_keys(password)
        return self

    def get_password(self):
        return self.base_page_driver.find_element(*self.__input_password).get_attribute('value')

    def press_sign_in_button(self):
        self.base_page_driver.find_element(*self.__button_sign_in).click()
        error_msg_box = self.base_page_driver.find_elements(By.CSS_SELECTOR, 'div.alert')
        if error_msg_box.__len__() > 0:
            error_web_elements = []
            error_items = []
            error_web_elements.append(error_msg_box[0].find_element(By.TAG_NAME, 'p'))
            error_web_elements.extend(error_msg_box[0].find_elements(By.TAG_NAME, 'li'))
            for element in error_web_elements:
                error_items.append(str(element.text))
            raise ErrorMessageException.by_errors_list(error_items)
        else:
            return AccountPage()
