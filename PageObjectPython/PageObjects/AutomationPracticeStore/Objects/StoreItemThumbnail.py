from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By

from PageObjects.AutomationPracticeStore.Objects.POExceptions import ErrorMessageException
from PageObjects.AutomationPracticeStore.Objects.ProductInCartPopup import ProductInCartPopup
from PageObjects.AutomationPracticeStore.Objects.QuickView import QuickView
from PageObjects.AutomationPracticeStore.ProductPage import ProductPage
from PageObjects.BasePageObject import BasePageObject


class StoreItemThumbnail(BasePageObject):
    # Regular web elements found in the items across all screens
    __item_image = (By.CSS_SELECTOR, 'a.product_img_link img')
    __item_name = (By.TAG_NAME, 'h5')
    __item_price = (By.CSS_SELECTOR, 'div.right-block .price')
    __item_discount = (By.CSS_SELECTOR, 'div.right-block .price-percent-reduction')
    __item_price_before_discount = (By.CSS_SELECTOR, 'div.right-block .old-price')

    # The following web elements may already exist in the DOM but are not clickable.
    # Should be accessed only after hovering the mouse over the item itself.
    __item_context_price = (By.CSS_SELECTOR, 'div.left-block .price')
    __item_context_discount = (By.CSS_SELECTOR, 'div.left-block .price-percent-reduction')
    __item_context_price_before_discount = (By.CSS_SELECTOR, 'div.left-block .old-price')
    __item_context_add_to_cart_button = (By.CSS_SELECTOR, 'div.right-block a.ajax_add_to_cart_button')
    __item_context_more_button = (By.CSS_SELECTOR, 'div.right-block a.lnk_view')
    __item_context_quick_view_button = (By.CLASS_NAME, 'quick-view')  # Mobile version using class 'quick-view-mobile'

    # TODO: Create the proper exception in the methods handling all the following locators
    # Web Elements with extra functionality that only appear outside of the Home screen
    __item_color_picking = (By.CLASS_NAME, 'color-pick')
    __item_stock_status = (By.CLASS_NAME, 'availability')
    __item_add_to_wish_list_tab = (By.CLASS_NAME, 'addToWishlist')  # Needs to hover over the item first
    __item_add_to_compare_tab = (By.CLASS_NAME, 'add_to_compare')   # Needs to hover over the item first

    __IN_STOCK_MESSAGE = 'In stock'

    def __init__(self, element):
        self.__we = element

    def get_item_name(self):
        return self.__we.find_element(*self.__item_name).text

    def get_item_price(self):
        txt = self.__we.find_element(*self.__item_price).text[1:]
        return float(txt)

    def get_image_url(self):
        return self.__we.find_element(*self.__item_image).get_attribute('src')

    def get_item_discount_percentage(self):
        found = self.__we.find_elements(*self.__item_discount)
        if found.__len__() > 0:
            return int(found[0].text.split('%')[0][1:])
        return 0

    def get_item_price_before_discount(self):
        found = self.__we.find_elements(*self.__item_price_before_discount)
        if found.__len__() > 0:
            return float(found[0].text[1:])
        return 0.0

    def get_context_item_price(self):
        super()._hover_over_elements(self.__we)
        txt = self.__we.find_element(*self.__item_context_price).text[1:]
        return float(txt)

    def get_context_item_discount_percentage(self):
        super()._hover_over_elements(self.__we)
        found = self.__we.find_element(*self.__item_context_discount)
        if found.__len__() > 0:
            return int(found[0].text.split('%')[0][1:])
        return 0

    def get_context_item_price_before_discount(self):
        super()._hover_over_elements(self.__we)
        found = self.__we.find_element(*self.__item_context_price_before_discount)
        if found.__len__() > 0:
            return float(found[0].text[1:])
        return 0.0

    def add_to_cart(self):
        super()._hover_over_elements(self.__we)
        atc = self.__we.find_element(*self.__item_context_add_to_cart_button)
        atc.click()
        return ProductInCartPopup()

    def more_information(self):
        super()._hover_over_elements(self.__we)
        mib = self.__we.find_element(*self.__item_context_more_button)
        mib.click()
        return ProductPage()

    def quick_view(self):
        super()._hover_over_elements(self.__we)
        qvb = self.__we.find_element(*self.__item_context_quick_view_button)
        qvb.click()
        return QuickView()

    # The following methods should be used only if the item is being handled by a CatalogPage instance
    def is_the_item_in_stock(self):
        try:
            txt = self.__we.find_element(*self.__item_stock_status).text
        except NoSuchElementException:
            msg = 'The label item for stock availability was not found.' + \
                  'Make sure this object is being displayed in the Catalog Page'
            raise NoSuchElementException(msg=msg)
        if str(txt).strip() == self.__IN_STOCK_MESSAGE:
            return True
        else:
            return False

    def get_available_colors(self):
        available_colors = []
        colors = self.__we.find_elements(*self.__item_color_picking)
        for color in colors:
            try:
                color_code = str(color.get_attribute('style')).split('#')[1]
            except IndexError:
                color_code = None
            if color_code is None:
                print(f"The item named '{self.get_item_name()}' has one color missing!")
            available_colors.append(color_code[:1])
        return available_colors

    def is_item_in_color(self, wanted_color):
        found = next((clr for clr in self.get_available_colors() if clr == wanted_color), None)
        if found is None:
            return False
        else:
            return True

    def select_item_in_color(self, wanted_color):
        colors_available = self.__we.find_elements(*self.__item_color_picking)
        if colors_available.__len__() == 0:
            msg = f"This item '{self.get_item_name()}' has no colors to display" + \
                  'Make sure this object is being displayed in the Catalog Page or that the color section exist'
            raise NoSuchElementException(msg=msg)
        found = filter(lambda itm: itm.get_attribute('style') == wanted_color, colors_available).__next__()
        if found is None:
            raise NoSuchElementException(msg=f"The item '{self.get_item_name()}' is not available in {wanted_color}")
        else:
            found.click()
            return ProductPage()

    def add_to_wish_list(self):
        super()._hover_over_elements(self.__we)
        atw = self.__we.find_elements(*self.__item_add_to_wish_list_tab)
        if atw.__len__() == 0:
            msg = 'The tab item for adding to the wish list was not found.' + \
                  'Make sure this object is being displayed in the Catalog Page'
            raise NoSuchElementException(msg=msg)
        else:
            atw[0].click()

        error = super().base_page_driver.find_elements_by_class_name('fancybox-error')  # To locate a error message box
        if error.__len__() > 0 & error[0].text != 'Added to your wishlist.':
            raise ErrorMessageException(msg=f"{error[0].text}")
        else:
            return self

    def is_added_to_wish_list(self):
        super()._hover_over_elements(self.__we)
        atw = self.__we.find_elements(*self.__item_add_to_wish_list_tab)
        if atw.__len__() == 0:
            msg = 'The tab item for adding to the wish list was not found.' + \
                  'Make sure this object is being displayed in the Catalog Page'
            raise NoSuchElementException(msg)
        else:
            attr = str(atw[0].get_attribute('class'))
            return attr.endswith('checked')

    def add_to_compare(self, decision):
        super()._hover_over_elements(self.__we)
        atc = self.__we.find_elements(*self.__item_add_to_compare_tab)
        if decision is True & (not self.is_added_to_compare):
            atc[0].click()
        elif decision is False & self.is_added_to_compare:
            atc[0].click()
        return self

    def is_added_to_compare(self):
        super()._hover_over_elements(self.__we)
        atc = self.__we.find_elements(*self.__item_add_to_compare_tab)
        if atc.__len__() == 0:
            msg = 'The tab item for adding for comparison was not found.' + \
                  'Make sure this object is being displayed in the Catalog Page'
            raise NoSuchElementException(msg=msg)
        else:
            attr = str(atc[0].get_attribute('class'))
            return attr.endswith('checked')
