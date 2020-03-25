using OpenQA.Selenium;
using OpenQA.Selenium.Support.UI;
using SeleniumFramework.Base;
using System;
using System.Collections.Generic;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class ProductInCartPopUp : BasePage
    {
        private readonly By labelProductName = By.Id("layer_cart_product_title");
        private readonly By labelProductAttributes = By.Id("layer_cart_product_attributes");
        private readonly By labelProductQuantity = By.Id("layer_cart_product_quantity");
        private readonly By labelProductTotalPrice = By.Id("layer_cart_product_price");

        private readonly By buttonContinueShopping = By.ClassName("continue");
        private readonly By buttonProceedCheckout = By.CssSelector("a.btn-default");
        private readonly By buttonClosePopup = By.ClassName("cross");
        private readonly By labelNumberOfItemsInCart = By.ClassName("ajax_cart_quantity");
        private readonly By labelTotalProductsAmount = By.ClassName("ajax_block_products_total");
        private readonly By labelTotalShippingAmount = By.CssSelector("div.layer_cart_row span.ajax_cart_shipping_cost");
        private readonly By labelTotalFinalAmount = By.CssSelector("div.layer_cart_row span.ajax_block_cart_total");

        private readonly By popUpLocator = By.CssSelector("div#layer_cart div.clearfix");

        private readonly string PLURAL_PHRASE_LOCATOR = "ajax_cart_product_txt_s";
        private readonly string SINGULAR_PHRASE_LOCATOR = "ajax_cart_product_txt";

        private readonly IWebElement popUp;
        private readonly WebDriverWait wait;


        public ProductInCartPopUp() : base() 
        {
            wait = new WebDriverWait(Driver, TimeSpan.FromSeconds(4));
            wait.Until(ExpectedConditions.ElementIsVisible(popUpLocator));
            popUp = Driver.FindElement(popUpLocator);
        }

        public string GetProductName() 
        {
            return popUp.FindElement(labelProductName).Text;
        }

        public int GetProductQuantity() 
        {
            return int.Parse(popUp.FindElement(labelProductQuantity).Text);
        }

        public double GetProductTotal() 
        {
            return double.Parse(popUp.FindElement(labelProductTotalPrice).Text.Substring(1));
        }

        public IReadOnlyList<string> GetProductAttributes() 
        {
            var attrs = popUp.FindElement(labelProductAttributes).Text.Replace(" ", string.Empty);
            return attrs.Split(',');
        }

        public double GetTotalProductsPrice() 
        {
            return double.Parse(popUp.FindElement(labelTotalProductsAmount).Text.Substring(1));
        }

        public double GetTotalShippingCost() 
        {
            return double.Parse(popUp.FindElement(labelTotalShippingAmount).Text.Substring(1));
        }

        public double GetCartFinalAmount() 
        {
            return double.Parse(popUp.FindElement(labelTotalFinalAmount).Text.Substring(1));
        }

        public string GetTotalItemsPhrase() 
        {
            var classLocator = GetTotalItemsInCart() > 1 ? PLURAL_PHRASE_LOCATOR : SINGULAR_PHRASE_LOCATOR;
            return popUp.FindElement(By.ClassName(classLocator)).Text;
        }

        public int GetTotalItemsInCart() 
        {
            return int.Parse(popUp.FindElement(labelNumberOfItemsInCart).Text);
        }

        public CheckoutPage ProceedToCheckout() 
        {
            popUp.FindElement(buttonProceedCheckout).Click();
            return new CheckoutPage();
        }

        public void ContinueShopping() 
        {
            popUp.FindElement(buttonContinueShopping).Click();
            wait.Until(ExpectedConditions.InvisibilityOfElementLocated(popUpLocator));
        }

        public void ClosePopup() 
        {
            popUp.FindElement(buttonClosePopup).Click();
            wait.Until(ExpectedConditions.InvisibilityOfElementLocated(popUpLocator));
        }

    }
}
