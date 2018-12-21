using OpenQA.Selenium;
using SeleniumFramework.Base;
using System.Globalization;

namespace PHPTravel.PageObjects
{
    public class FeaturedHotel : BasePage
    {

        private By hotelName = By.CssSelector("div.country-name h4");
        private By hotelLocation = By.CssSelector("div.country-name p");
        private By hotelRate = By.CssSelector("div.additional-info i.fa-star");
        private By hotelMaxAllowedRate = By.CssSelector("div.additional-info i.star");
        private By hotelCurrencyAndPrice = By.CssSelector("div.additional-info div.pull-right span.text-center");
        private By hotelBookNowButton = By.ClassName("loader");

        private readonly IWebElement webItem;

        public FeaturedHotel(IWebElement hotelWebItem)
        {
            webItem = hotelWebItem;
        }


        public string GetHotelName()
        {
            var _nameItem = webItem.FindElement(hotelName);
            return Support.GetTextUsingJavaScript(Driver, _nameItem);
        }

        public string GetHotelLocation()
        {
            var _locationItem = webItem.FindElement(hotelLocation);
            return Support.GetTextUsingJavaScript(Driver, _locationItem);
        }

        public int GetHotelRate()
        {
            return webItem.FindElements(hotelRate).Count;
        }

        public int GetHotelMaxAllowedRate()
        {
            return webItem.FindElements(hotelMaxAllowedRate).Count;
        }

        public string GetHotelCurrency()
        {
            var _currencyItm = webItem.FindElement(hotelCurrencyAndPrice);
            return Support.GetTextUsingJavaScript(Driver, _currencyItm.FindElement(By.TagName("small")));
        }

        public float GetHotelPrice()
        {
            var _priceItm = webItem.FindElement(hotelCurrencyAndPrice);
            var _priceStr = Support.GetTextUsingJavaScript(Driver, _priceItm);
            var _priceNoSymbolStr = _priceStr.Split('$')[1];
            return float.Parse(_priceNoSymbolStr, NumberStyles.AllowThousands);
        }

        public HotelDetails BookNow()
        {
            var button = webItem.FindElement(hotelBookNowButton);
            Support.JavaScriptClick(Driver, button);
            return new HotelDetails();
        }

    }
}