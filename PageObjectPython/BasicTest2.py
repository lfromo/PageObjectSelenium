from PageObjects.AutomationPracticeStore.Objects.POExceptions import NotLoggedInException
from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
import PageObjectTest as BaseTest

BaseTest.PageObjectTest('FIREFOX', 'http://automationpractice.com')
po = StoreHomePage()

po.select_tab('POPULAR')
all_items_in_screen = po.get_active_items()
header = po.get_header()
cats = header.get_available_categories()
qv = all_items_in_screen[5].quick_view()
thumbs = qv.get_thumbnail_pictures_source_list()
prod_name = qv.get_product_name()
prod_price = qv.get_price()
prod_discount = qv.get_discount()
prod_model = qv.get_product_model()
prod_condition = qv.get_product_condition()
prod_desc = qv.get_product_description()
prod_old_price = qv.get_price_before_discount()
prod_big_pic = qv.get_current_picture_source()
qv.set_quantity(3)
qty = qv.get_current_quantity()
qv.increment_quantity()
qv.decrement_quantity()
qv.decrement_quantity()
qty_new = qv.get_current_quantity()
sizes = qv.get_available_sizes()
colors = qv.get_available_colors()
try:
    qv.add_to_wish_list()
except NotLoggedInException as Ex:
    pop_msg = qv.get_pop_up_message()
    ex_msg = Ex.msg
    qv.close_pop_up_message()
finally:
    po.base_page_driver.quit()
