using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using SeleniumFramework.Base;
using System;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class StoreItemThumbnail
    {

        //Regular web elements found in the items across all screens

        [FindsBy(How = How.CssSelector, Using = "a.product_img_link img")]
        private IWebElement itemImage;

        [FindsBy(How = How.TagName, Using = "h5")]
        private IWebElement itemName;

        [FindsBy(How = How.CssSelector, Using = "div.right-block .price")]
        private IWebElement itemPrice;

        [FindsBy(How = How.CssSelector, Using = "div.right-block .price-percent-reduction")]
        private IList<IWebElement> itemDiscount;

        [FindsBy(How = How.CssSelector, Using = "div.right-block .old-price")]
        private IList<IWebElement> itemPriceBeforeDiscount;

        /* The following web elements may already exist in the DOM but are not clickable.
           Should be accessed only after hovering the mouse over the item itself. */

        [FindsBy(How = How.CssSelector, Using = "div.left-block .price")]
        private IWebElement itemContextPrice;

        [FindsBy(How = How.CssSelector, Using = "div.left-block .price-percent-reduction")]
        private IList<IWebElement> itemContextDiscount;

        [FindsBy(How = How.CssSelector, Using = "div.left-block .old-price")]
        private IList<IWebElement> itemContextPriceBeforeDiscount;

        [FindsBy(How = How.CssSelector, Using = "div.right-block a.ajax_add_to_cart_button")]
        private IWebElement itemContextAddToCartButton;

        [FindsBy(How = How.CssSelector, Using = "div.right-block a.lnk_view")]
        private IWebElement itemContextMoreButton;

        [FindsBy(How = How.ClassName, Using = "quick-view")]
        private IWebElement itemQuickViewButton;


        /* TODO: Create the proper exception in the methods handling all the following locators
           Web Elements with extra functionality that only appear outside of the Home screen */

        [FindsBy(How = How.ClassName, Using = "color-pick")]
        private IList<IWebElement> itemExtraColorPicking;

        [FindsBy(How = How.ClassName, Using = "availability")]
        private IList<IWebElement> itemStockStatus;

        [FindsBy(How = How.ClassName, Using = "addToWishlist")]
        private IList<IWebElement> itemAddToWishListTab;    //Needs to be hover over the item first

        [FindsBy(How = How.ClassName, Using = "add_to_compare")]
        private IWebElement itemAddToCompareTab;     //Needs to be hover over the item first

        private readonly static string IN_STOCK_MESSAGE = "In stock";
        private readonly static string ADDED_TO_WISH_LIST = "Added to your wishlist.";

        private readonly static By ERROR_MESSAGE_BOX_LOCATOR = By.ClassName("fancybox-error");



        public string GetName() { return itemName.Text; }

        public float GetPrice() { return float.Parse(itemPrice.Text.Substring(1)); }

        public string GetImageURL() 
        {
            return itemImage.GetAttribute("src");
        }

        public int GetDiscountPercentage() 
        {
            return itemDiscount.Count == 0 ? 0 : int.Parse(itemDiscount[0].Text.Split('%')[0].Substring(1));
        }

        public float GetPriceBeforeDiscount() 
        {
            return itemPriceBeforeDiscount.Count == 0 ? 0 : float.Parse(itemPriceBeforeDiscount[0].Text.Substring(1));
        }

        public float GetContextPrice() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            return float.Parse(itemContextPrice.Text.Substring(1));
        }

        public int GetContextDiscountPercentage() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            return itemContextDiscount.Count == 0 ? 0 : int.Parse(itemContextDiscount[0].Text.Split('%')[0].Substring(1));
        }

        public float GetContextPriceBeforeDiscount() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            return itemContextPriceBeforeDiscount.Count == 0 ? 0 : float.Parse(itemContextPriceBeforeDiscount[0].Text.Substring(1));
        }


        public ProductInCartPopUp AddToCart() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            itemContextAddToCartButton.Click();
            return new ProductInCartPopUp();
        }

        public ProductPage MoreInformation() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            itemContextMoreButton.Click();
            return new ProductPage();
        }

        public QuickView GetQuickView() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            itemQuickViewButton.Click();
            return new QuickView();
        }


        // The following methods should be used only if the item is being handled by a CatalogPage instance

        public bool IsInStock() 
        {
            if (itemStockStatus.Count == 0)
                throw new NoSuchElementException("The label for stock availability was not found. Make sure this object is being displayed in the Catalog page");
            return itemStockStatus[0].Text.Trim().Equals(IN_STOCK_MESSAGE);
        }

        public IReadOnlyList<string> GetAvailableColors() 
        {
            var availableColors = new List<string>();
            string colorHexCode = string.Empty;
            itemExtraColorPicking.ToList().ForEach(colorWe => {
                try
                {
                    colorHexCode = colorWe.GetAttribute("style").Split('#')[1];
                }
                catch (IndexOutOfRangeException)
                {
                    Console.Error.WriteLine($"The item named '{ GetName() }' has one color missing.");
                }
                if (!string.IsNullOrWhiteSpace(colorHexCode))
                    availableColors.Add(colorHexCode);
            });
            return availableColors.AsReadOnly();
        }

        public bool IsProductInColor(string colorHexCode) 
        {
            return GetAvailableColors().Any(color => string.Equals(color, colorHexCode, StringComparison.CurrentCultureIgnoreCase));
        }

        public ProductPage SelectColor(string colorHexCode) 
        {
            if (GetAvailableColors().Count == 0)
                throw new NoSuchElementException($"The item '{ GetName() }' has not colors to display. " +
                    "Make sure this object is being displayed in the Catalog page or the color section exists");

            var found = itemExtraColorPicking.Where(colorWe => string.Equals(colorWe.GetAttribute("style"), colorHexCode, StringComparison.CurrentCultureIgnoreCase)).FirstOrDefault() 
                ?? throw new NoSuchElementException($"The item named '{ GetName() }' is not available in color '{ colorHexCode }'");

            found.Click();
            return new ProductPage();
        }

        public StoreItemThumbnail AddToWishList() 
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            if (itemAddToWishListTab.Count == 0)
                throw new NoSuchElementException($"The tab for adding to the wish list for item '{ GetName() }' was not found. " +
                    "Make sure this object is being displayed in the Catalog page");

            itemAddToWishListTab[0].Click();

            var msg = TestConfiguration.GetDriverInstance().FindElements(ERROR_MESSAGE_BOX_LOCATOR);
            if(msg.Count == 0)
                throw new NoSuchElementException("The expected pop up message did not appear");
            if (!msg[0].Text.Equals(ADDED_TO_WISH_LIST))
                throw new ErrorMessageException(msg[0].Text);
            msg[0].FindElement(By.XPath("../../../a[@title='Close']")).Click(); //For closing the popup message that should always appear
            return this;
        }

        public bool IsAddedToWishList()
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            if (itemAddToWishListTab.Count == 0)
                throw new NoSuchElementException($"The tab for adding to the wish list for item '{ GetName()}' was not found. " +
                        "Make sure this object is being displayed in the Catalog page");
            string attr = itemAddToWishListTab[0].GetAttribute("class");
            return attr.EndsWith("checked");
        }

        public StoreItemThumbnail AddToCompare(bool addToCompare)
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            if (addToCompare && !IsAddedToCompare())
                itemAddToCompareTab.Click();
            if (!addToCompare && IsAddedToCompare())
                itemAddToCompareTab.Click();
            return this;
        }

        public bool IsAddedToCompare()
        {
            Support.HoverOverWebElement(TestConfiguration.GetDriverInstance(), itemImage);
            var found = itemAddToCompareTab ?? throw new NoSuchElementException($"The tab for adding for comparison for item '{ GetName() }' was not found. " +
                        "Make sure this object is being displayed in the Catalog page");
            string classAttribute = found.GetAttribute("class");
            return classAttribute.EndsWith("checked");
        }

    }

}
