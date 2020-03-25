from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webelement import WebElement


class FeaturedTour(object):
    """This class serves the purpose of holding the locators and attributes for any given Featured Tour displayed at
    the home page """

    __imageMainTour = (By.CSS_SELECTOR, 'div.image img')
    __labelTourTitle = (By.TAG_NAME, 'h5')
    __labelLocation = (By.CLASS_NAME, 'location')
    __imagesStars = (By.CSS_SELECTOR, 'div.rating-item i.rating-rated')
    __labelPrice = (By.CSS_SELECTOR, 'div.d-flex span.text-secondary span')
    __buttonBookNow = (By.CSS_SELECTOR, 'p.location + span')
    __labelDiscount = (By.CLASS_NAME, 'discount')

    __we: WebElement

    def __init__(self, element):
        self.__we = element

    def tour_name(self):
        return self.__we.find_element(*self.__labelTourTitle).text

    def number_of_stars(self):
        return self.__we.find_elements(*self.__imagesStars).__len__()

    def tour_price(self):
        txt = self.__we.find_element(*self.__labelPrice).text[1:]
        return float(txt)

    def discount(self):
        found = self.__we.find_elements(*self.__labelDiscount)
        if found.__len__() > 0:
            return float(found[0].text.split('%')[0])
        return 0.0

    def location(self):
        return self.__we.find_element(*self.__labelLocation).text
