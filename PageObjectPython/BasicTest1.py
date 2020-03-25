from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
import PageObjectTest as BaseTest

BaseTest.PageObjectTest('FIREFOX', 'http://automationpractice.com')
po = StoreHomePage()

try:
    po.select_tab('BEST SELLERS')
    toto = po.get_active_items()
    print('**** BEST SELLER ITEMS ****')
    i = 1
    n = 1
    for itm in toto:
        print('---Item ' + str(i) + '---')
        print('Item Name:' + itm.get_item_name())
        print('Item Price:' + itm.get_item_price().__str__())
        print('Item Discount %:' + itm.get_item_discount_percentage().__str__())
        i += 1
    foo = po.get_popular_items()
    print('\n**** POPULAR ITEMS ****')
    for itm in foo:
        print('---Item ' + str(n) + '---')
        print('Item Name:' + itm.get_item_name())
        print('Item Price:' + itm.get_item_price().__str__())
        print('Item Discount %:' + itm.get_item_discount_percentage().__str__())
        n += 1
    # cheapItems = (wat for wat in toto if wat.get_item_price() < 29)
    cheapItems2 = list(filter(lambda gee: gee.get_item_price() < 29, foo))
    n = 1
    for chp in cheapItems2:
        print('Cheap Item {} Name: {}'.format(str(n), chp.get_item_name()))
        print('Cheap Item {} Price: {}'.format(str(n), str(chp.get_item_price())))
        n += 1
    print('Done')
finally:
    po.base_page_driver.quit()
