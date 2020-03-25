from PageObjects.AutomationPracticeStore.StoreHomePage import StoreHomePage
import PageObjectTest as BaseTest

BaseTest.PageObjectTest('CHROME', 'http://automationpractice.com')
po = StoreHomePage()

try:
    po.select_tab('POPULAR')
    all_items_in_screen = po.get_active_items()
    cart = all_items_in_screen[3].add_to_cart()
    print(f"Total items in cart: {cart.get_total_items_in_cart()}")
    print(f"The shipping cost is {cart.get_total_shipping_cost()}")
    print(f"The final amount for everything in the cart is {cart.get_cart_final_amount()}")

    print(f"Name of product added is '{cart.get_product_name()}'. Quantity is {cart.get_product_quantity()}")
    print(f"The cost of the item added is {cart.get_product_total()}")
    print(f"The Attributes of the product are: {cart.get_product_attributes()}")
    print(f"The phrase says: '{cart.get_total_items_phrase()}'")

    cart.continue_shopping()
    cart = all_items_in_screen[5].add_to_cart()
    print(f"Total items in cart: {cart.get_total_items_in_cart()}")
    print(f"The shipping cost is {cart.get_total_shipping_cost()}")
    print(f"The final amount for everything in the cart is {cart.get_cart_final_amount()}")

    print(f"Name of product added is '{cart.get_product_name()}'. Quantity is {cart.get_product_quantity()}")
    print(f"The cost of the item added is {cart.get_product_total()}")
    print(f"The Attributes of the product are: {cart.get_product_attributes()}")
    print(f"The phrase says: '{cart.get_total_items_phrase()}'")

finally:
    po.base_page_driver.quit()
