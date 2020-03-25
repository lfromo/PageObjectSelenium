from PageObjects.AutomationPracticeStore.StorePage import StorePage
from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.Objects.LoginForm import LoginForm
from PageObjects.AutomationPracticeStore.Objects.NewAccountEmail import NewAccountEmail


class AuthenticationPage(StorePage):

    __form_error_container = (By.CSS_SELECTOR, 'div.form-error')

    def already_registered(self):
        return LoginForm()

    def register_new_account(self):
        return NewAccountEmail()

    def are_there_errors(self):
        return self.base_page_driver.find_elements(*self.__form_error_container).__len__() > 0
