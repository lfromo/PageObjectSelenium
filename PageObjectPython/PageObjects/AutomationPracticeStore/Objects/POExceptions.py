from selenium.common.exceptions import WebDriverException


class NotLoggedInException(WebDriverException):

    def __init__(self, message):
        super().__init__(msg=message)


class ErrorMessageException(WebDriverException):

    def __init__(self, message, error_list):
        super().__init__(msg=message)
        self.errors = error_list

    @classmethod
    def by_message(cls, error_message):
        return cls(error_message, None)

    @classmethod
    def by_errors_list(cls, list_of_error_strings):
        error_msg = ''
        for error in list_of_error_strings:
            error_msg = error_msg + (error + '\n')

        return cls(error_msg, list_of_error_strings)
