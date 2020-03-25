from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.StorePage import StorePage
from PageObjects.AutomationPracticeStore.Objects.StoreItemThumbnail import StoreItemThumbnail


class StoreHomePage(StorePage):
    """This is the Automationpractice.com store example home page"""

    __home_page_tabs_section = (By.ID, 'home-page-tabs')
    __active_pane_items_holder = (By.CSS_SELECTOR, 'div.tab-content ul.product_list.active')

    def get_popular_items(self):
        self.select_tab('POPULAR')
        self.base_page_driver.take_screenshot()
        return self.get_active_items()

    def get_best_seller_items(self):
        self.select_tab('BEST SELLERS')
        return self.get_active_items()

    def get_active_items(self):
        try:
            section = self.base_page_driver.find_element(*self.__active_pane_items_holder)
        except NoSuchElementException:
            raise NoSuchElementException('The active section holder was not found!')
        else:
            items = section.find_elements_by_tag_name('li')
            items_list = []
            for item in items:
                items_list.append(StoreItemThumbnail(item))
            return items_list

    def get_tabs_available(self):
        sections_available = []
        for itm in self.__sections_available():
            sections_available.append(itm.text)
        return sections_available

    def select_tab(self, tab_name):
        available = self.__sections_available()
        found = filter(lambda itm: itm.text == tab_name, available).__next__()
        if found is None:
            raise NoSuchElementException(msg='The section tab named \'' + tab_name + '\' was not found')
        else:
            found.click()

    def __sections_available(self):
        section = self.base_page_driver.find_element(*self.__home_page_tabs_section)
        return section.find_elements_by_tag_name('a')
