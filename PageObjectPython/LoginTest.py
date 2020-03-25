import unittest
import PageObjectTest as BaseTest
from PageObjects.AutomationPracticeStore.Objects.POExceptions import ErrorMessageException

from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
from PageObjects.AutomationPracticeStore.StorePage import StorePage


class LoginTests(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        BaseTest.PageObjectTest('FIREFOX', 'http://automationpractice.com')

    def test_login(self):
        """
        This test cases exercises the login functionality using invalid credentials. Expecting an error message and
        failing authentication
        """
        self.po = StoreHomePage()
        self.assertFalse(self.po.get_header().is_user_logged_in())
        login_section = self.po.get_header().sign_in().already_registered()
        login_section = login_section.set_existent_email_address('blublubla@mailinator.com').set_password('afakepassword')
        try:
            login_section.press_sign_in_button()
        except ErrorMessageException as Ex:
            self.assertTrue(Ex.errors.count('Authentication failed.') == 1)
        else:
            self.fail("The error message was not displayed!")

    def test_invalid_email(self):
        """
        This test cases exercise the capability of the login functionality to handle invalid data. Expecting an error
        message after trying to log into the application.
        """
        self.po = StorePage()
        self.assertFalse(self.po.get_header().is_user_logged_in())
        login_section = self.po.get_header().sign_in().already_registered()
        with self.assertRaises(ErrorMessageException) as Ex:
            login_section.set_existent_email_address('blublubla').press_sign_in_button()
            self.assertTrue(Ex.exception.errors.count('Invalid email address.'))

    @classmethod
    def tearDownClass(cls):
        BaseTest.PageObjectTest.driver.quit()


if __name__ == '__main__':
    unittest.main()
