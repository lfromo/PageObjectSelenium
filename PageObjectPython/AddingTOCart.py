from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
import PageObjectTest as BaseTest

BaseTest.PageObjectTest('FIREFOX', 'http://automationpractice.com')
po = StoreHomePage()
try:
    po.select_tab('POPULAR')
    all_items_in_screen = po.get_active_items()
    le_price = 0.0
    for item in all_items_in_screen:
        le_price += item.get_item_price()
        pop = item.add_to_cart()
        pop.continue_shopping()
    header = po.get_header()
    mini_cart = header.get_contextual_cart()
    mini_items = mini_cart.get_items()
    assert all_items_in_screen.__len__() == mini_items.__len__()
    cart_total = mini_cart.get_total() - mini_cart.get_shipping_cost()
    assert le_price == cart_total
    for itm in mini_items:
        itm.remove_item()

    header = po.get_header()
    # The next failing steps serve as example of PO methods calls from a broken functionality that was working before
    mini_cart = header.get_contextual_cart()
    assert mini_cart.get_shipping_cost() == 0.0

finally:
    po.base_page_driver.quit()
