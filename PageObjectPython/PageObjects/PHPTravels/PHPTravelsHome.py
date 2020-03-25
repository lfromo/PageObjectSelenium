from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException

from PageObjects.PHPTravels.PHPTravelsPage import PHPTravelsPage
from PageObjects.PHPTravels.Objects.FeaturedTour import FeaturedTour


class PHPTravelsHome(PHPTravelsPage):
    """This is the PHP Travels home page"""

    __featuredToursSectionSelector = (By.CSS_SELECTOR, 'div.section-title.text-center ~ div.gap-xl-30')

    def get_featured_tours(self):
        try:
            section = self.base_page_driver.find_element(*self.__featuredToursSectionSelector)
        except NoSuchElementException:
            raise NoSuchElementException('The Tours section was not found')
        else:
            tours = section.find_elements_by_css_selector('div.col')
            tours_list = []
            for tour in tours:
                tours_list.append(FeaturedTour(tour))
            return tours_list
