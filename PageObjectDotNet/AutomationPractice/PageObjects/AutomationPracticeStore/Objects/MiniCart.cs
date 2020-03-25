using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium.Support.UI;
using SeleniumFramework.Base;
using System;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class MiniCart : BasePage
    {

        private readonly By miniItemsLocator = By.TagName("dt");
        private readonly By labelShippingLocator = By.ClassName("cart_block_shipping_cost");
        private readonly By labelTotalLocator = By.ClassName("cart_block_total");
        private readonly By buttonCheckoutLocator = By.Id("button_order_cart");

        private readonly By miniCartLocator = By.ClassName("cart_block_list");

        private readonly IWebElement miniCart;

        public MiniCart() 
        {
            try {
                WebDriverWait wait = new WebDriverWait(Driver, TimeSpan.FromSeconds(3));
                //Consider using this >>>> wait.Until(Driver => Driver.FindElement(buttonCheckoutLocator));
                wait.Until(ExpectedConditions.ElementIsVisible(buttonCheckoutLocator));
                miniCart = Driver.FindElement(miniCartLocator);
            } catch (WebDriverException ex) 
            {
                throw new WebDriverException("The context cart was not displayed", ex);
            }
        }


        public List<MiniCartItem> GetItems() 
        {
            List<MiniCartItem> foundItems = new List<MiniCartItem>();
            var items = this.miniCart.FindElements(miniItemsLocator);
            items.ToList().ForEach(item => {
                var miniItem = new MiniCartItem(item);
                PageFactory.InitElements(item, miniItem);
                foundItems.Add(miniItem);
            });

            return foundItems;
        }

        public double GetShippingCost() 
        {
            var shippingCostStr = Driver.FindElement(labelShippingLocator).Text;
            return shippingCostStr.Equals("Free shipping!") ? 0.0 : double.Parse(shippingCostStr.Substring(1));
        }

        public double GetTotal() 
        {
            var txt = Driver.FindElement(labelTotalLocator).Text;
            return double.Parse(txt.Substring(1));
        }

        public CartPage PressCheckoutButton() 
        {
            Driver.FindElement(buttonCheckoutLocator).Click();
            return new CartPage();
        }


    }
}