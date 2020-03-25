using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium.Support.UI;
using SeleniumFramework.Base;
using System;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class QuickView
    {

        [FindsBy(How = How.Id, Using = "bigpic")]
        IWebElement currentPicture;

        [FindsBy(How = How.CssSelector, Using = "ul#thumbs_list_frame li a")]
        IList<IWebElement> pictures;

        [FindsBy(How = How.Name, Using = "resetImages")]
        IWebElement displayAllPictures;

        [FindsBy(How = How.TagName, Using = "h1")]
        IWebElement productName;

        [FindsBy(How = How.CssSelector, Using = "p#product_reference span.editable")]
        IWebElement productModel;

        [FindsBy(How = How.CssSelector, Using = "p#product_condition span.editable")]
        IWebElement productCondition;

        [FindsBy(How = How.Id, Using = "short_description_content")]
        IWebElement productDescription;

        [FindsBy(How = How.Id, Using = "availability_date_value")]
        IWebElement productAvailability; //Available in the page but found in no product

        [FindsBy(How = How.Id, Using = "oosHook")]
        IWebElement productOutOfStock; //Not found something where this implementation is invoked

        [FindsBy(How = How.ClassName, Using = "socialsharing_product")]
        IWebElement socialButtonsHolder;

        [FindsBy(How = How.Id, Using = "our_price_display")]
        IWebElement labelPrice;

        [FindsBy(How = How.Id, Using = "reduction_percent_display")]
        IWebElement labelDiscount;

        [FindsBy(How = How.Id, Using = "old_price_display")]
        IWebElement labelOldPrice;

        [FindsBy(How = How.Id, Using = "quantity_wanted")]
        IWebElement inputQuantity;

        [FindsBy(How = How.ClassName, Using = "product_quantity_down")]
        IWebElement buttonQtySubtract;

        [FindsBy(How = How.ClassName, Using = "product_quantity_up")]
        IWebElement buttonQtyAdd;

        [FindsBy(How = How.Id, Using = "group_1")]
        IWebElement selectListSize;

        [FindsBy(How = How.Id, Using = "color_to_pick_list")]
        IWebElement listColor;

        [FindsBy(How = How.Name, Using = "Submit")]
        IWebElement buttonAddToCart;

        [FindsBy(How = How.Id, Using = "wishlist_button")]
        IWebElement buttonAddToWishlist;

        private readonly By popupMessage = By.ClassName("fancybox-skin");
        private readonly By frameContainer = By.CssSelector("iframe.fancybox-iframe");
        private By buttonCloseQuickView = By.ClassName("fancybox-close");

        private readonly IWebElement quickViewContainer;

        private readonly string ADD_TO_WISH_LIST_ERROR_NOT_LOGGED_IN = "You must be logged in to manage your wishlist.";


        public QuickView()
        {
            var driver = TestConfiguration.GetDriverInstance();
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(8));
            wait.Until(ExpectedConditions.InvisibilityOfElementLocated(By.CssSelector("div#fancybox-loading")));
            wait.Until(ExpectedConditions.ElementExists(frameContainer));
            var iframe = driver.FindElement(frameContainer);
            driver.SwitchTo().Frame(iframe);
            quickViewContainer = driver.FindElement(By.Id("product"));
            PageFactory.InitElements(quickViewContainer, this);
        }

        public string GetCurrentPictureSource() { return currentPicture.GetAttribute("src"); }

        public IReadOnlyList<string> GetThumbnailPicturesSourceList()
        {
            return pictures.Where(img => img.Displayed).Select(img => img.GetAttribute("href")).ToList().AsReadOnly();
        }

        public string GetCurrentThumbnailPicture()
        {
            var found = pictures.Where(img => img.GetAttribute("class").Equals("fancybox shown")).FirstOrDefault()
                ?? throw new NoSuchElementException("There was a problem finding the currently selected thumbnail");
            return found.FindElement(By.TagName("img")).GetAttribute("src");
        }

        public QuickView DisplayAllThumbnailPictures()
        {
            try
            {
                displayAllPictures.Click();
                return this;
            }
            catch (NoSuchElementException ex)
            {
                throw new NoSuchElementException("Link not found or all available thumbnails may be already displayed", ex);
            }
        }

        public bool AreAllThumbnailPicturesDisplayed()
        {
            try
            {
                return !displayAllPictures.Displayed; }
            catch (NoSuchElementException)
            {
                return true;
            }
        }

        public string GetProductName() { return productName.Text; }

        public string GetProductModel() { return productModel.Text; }

        public string GetProductCondition() { return productCondition.Text; }

        public string GetProductDescription() { return productDescription.Text; }

        public float GetProductPrice() { return float.Parse(labelPrice.Text.Substring(1)); }

        public int GetProductDiscount() 
        {
            return string.IsNullOrEmpty(labelDiscount?.Text) ? 0 : int.Parse(labelDiscount.Text.Split('%')[0].Substring(1));
        }

        public float GetPriceBeforeDiscount() 
        {
            return string.IsNullOrEmpty(labelOldPrice?.Text) ? 0 : float.Parse(labelOldPrice.Text.Substring(1));
        }

        public int GetCurrentQuantity() { return int.Parse(inputQuantity.GetAttribute("value")); }

        public QuickView SetQuantity(string quantity) 
        {
            inputQuantity.Clear();
            inputQuantity.SendKeys(quantity);
            return this;
        }

        public IReadOnlyList<string> GetAvailableSizes() 
        {
            var options = new SelectElement(selectListSize).Options;
            return options.Select(opt => opt.Text).ToList().AsReadOnly();
        }

        public QuickView SetSize(string size) 
        {
            var selectElement = new SelectElement(selectListSize);
            selectElement.SelectByValue(size);
            return this;
        }

        public int IncrementQuantity() 
        {
            buttonQtyAdd.Click();
            return GetCurrentQuantity();
        }

        public int DecrementQuantity()
        {
            buttonQtySubtract.Click();
            return GetCurrentQuantity();
        }

        public IReadOnlyDictionary<string, string> GetColorsAvailable() 
        {
            return GetWebElementColorsAvailable().ToDictionary(colorWe => colorWe.GetAttribute("name"), colorWe => colorWe.GetAttribute("style").Split(':')[1].Trim());
        }

        public QuickView SelectColorByName(string color) 
        {
            SelectColor(color, true);
            return this;
        }

        public QuickView SelectColorByCode(string color)
        {
            SelectColor(color, false);
            return this;
        }

        public ProductInCartPopUp AddToCart() 
        {
            buttonAddToCart.Click();
            TestConfiguration.GetDriverInstance().SwitchTo().DefaultContent();
            return new ProductInCartPopUp();
        }

        public QuickView AddToWishList() 
        {
            buttonAddToWishlist.Click();

            try
            {
                var txt = GetPopupMessage();
                if (txt.Equals(ADD_TO_WISH_LIST_ERROR_NOT_LOGGED_IN))
                    throw new NotLoggedInException(txt);
            }
            catch (WebDriverTimeoutException ex)
            {
                throw new NoSuchElementException("The popup message after adding the item to the wish list did not appear", ex);
            }
            ClosePopupMessage();
            return this;
        }

        public string GetPopupMessage() { return GetPopupMessageElement().Text; }

        public QuickView ClosePopupMessage() 
        {
            var driver = TestConfiguration.GetDriverInstance();
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(4));
            var closeBtn = wait.Until(ExpectedConditions.ElementToBeClickable(By.CssSelector("div.fancybox-skin a.fancybox-close")));
            closeBtn.Click();
            return this;
        }

        public void CloseQuickView() 
        {
            var driver = TestConfiguration.GetDriverInstance();
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(3));
            driver.SwitchTo().DefaultContent();
            var closeButton = driver.FindElement(buttonCloseQuickView);
            closeButton.Click();
            wait.Until(ExpectedConditions.InvisibilityOfElementLocated(By.ClassName("fancybox-overlay")));
        }

        private IWebElement GetPopupMessageElement() 
        {
            var driver = TestConfiguration.GetDriverInstance();
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(4));
            wait.Until(ExpectedConditions.ElementIsVisible(popupMessage));
            return driver.FindElement(popupMessage);
        }


        private IList<IWebElement> GetWebElementColorsAvailable()
        {
            return listColor.FindElements(By.TagName("a"));
        }

        private void SelectColor(string color, bool byName) 
        {
            var colorsAvailable = GetWebElementColorsAvailable();
            if (colorsAvailable.Count == 0)
                throw new NoSuchElementException("This QuickView window does not contain any color to choose from");

            var found = byName ? colorsAvailable.Where(we => string.Equals(we.GetAttribute("name"), color, StringComparison.CurrentCultureIgnoreCase)).FirstOrDefault() :
                colorsAvailable.Where(we => we.GetAttribute("style").ToLower().Contains(color.ToLower())).FirstOrDefault()
                ?? throw new NoSuchElementException($"The color '{color}' was not found among the available colors for product '{GetProductName()}'");
            found.Click();
        }

    }
}
