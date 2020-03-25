from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support.expected_conditions import visibility_of_element_located

from PageObjects.AutomationPracticeStore.NewAccountForm import NewAccountForm
from PageObjects.AutomationPracticeStore.Objects.POExceptions import ErrorMessageException
from PageObjects.BasePageObject import BasePageObject


class NewAccountEmail(BasePageObject):

    __input_create_account_email_address = (By.ID, 'email_create')
    __button_create_account = (By.ID, 'SubmitCreate')

    def set_email_address(self, email_address):
        input_box = self.base_page_driver.find_element(*self.__input_create_account_email_address)
        input_box.clear()
        input_box.send_keys(email_address)
        return self

    def get_email_address(self):
        return self.base_page_driver.find_element(*self.__input_create_account_email_address).get_attribute('value')

    def press_create_account_button(self):
        wait = WebDriverWait(self.base_page_driver, 2)
        self.base_page_driver.find_element(*self.__button_create_account).click()
        try:
            wait.until(visibility_of_element_located((By.ID, 'create_account_error')))
        except TimeoutException:
            return NewAccountForm()
        else:
            errors_found = []
            for error_element in self.base_page_driver.find_elements((By.CSS_SELECTOR, 'div#create_account_error li')):
                errors_found.append(str(error_element.text))
            raise ErrorMessageException.by_errors_list(errors_found)
