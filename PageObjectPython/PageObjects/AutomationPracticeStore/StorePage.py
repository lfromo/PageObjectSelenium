from PageObjects.AutomationPracticeStore.Objects.Footer import Footer
from PageObjects.AutomationPracticeStore.Objects.Header import Header
from PageObjects.BasePageObject import BasePageObject


class StorePage(BasePageObject):

    def get_header(self):
        return Header()

    def get_footer(self):
        return Footer()
