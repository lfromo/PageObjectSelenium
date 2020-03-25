using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium.Support.UI;
using SeleniumFramework.Base;
using System;
using System.Collections.ObjectModel;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class MiniCartItem
    {
        [FindsBy(How = How.CssSelector, Using = "a.cart-images img")]
        private IWebElement thumbnailPicture;

        [FindsBy(How = How.ClassName, Using = "quantity")]
        private IWebElement quantity;

        [FindsBy(How = How.ClassName, Using = "cart_block_product_name")]
        private IWebElement productName;

        [FindsBy(How = How.ClassName, Using = "product-atributes")]
        private IWebElement attributes;

        [FindsBy(How = How.ClassName, Using = "price")]
        private IWebElement total;

        [FindsBy(How = How.ClassName, Using = "ajax_cart_block_remove_link")]
        private IWebElement buttonClose;


        private readonly IWebElement item;

        public MiniCartItem(IWebElement miniItem) 
        {
            item = miniItem;
        }

        public string GetThumbnailImageUrl() 
        {
            return thumbnailPicture.GetAttribute("src");
        }

        public int GetQuantity() 
        {
            return int.Parse(quantity.Text);
        }

        public string GetProductFullName() 
        {
            return productName.GetAttribute("title");
        }

        public ReadOnlyCollection<string> GetProductAttributes() 
        {
            var attrs = attributes.Text.Replace(" ", string.Empty).Split(',');
            return attrs.ToList().AsReadOnly();
        }

        public double GetProductTotalPrice() 
        {
            return double.Parse(total.Text);
        }

        public void RemoveItem()
        {
            var wait = new WebDriverWait(TestConfiguration.GetDriverInstance(), TimeSpan.FromSeconds(3));
            buttonClose.Click();
            wait.Until(ExpectedConditions.StalenessOf(item));
        }

    }
}