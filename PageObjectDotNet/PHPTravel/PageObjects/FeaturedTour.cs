using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using System.Collections.Generic;
using System.Globalization;

namespace PHPTravel.PageObjects
{
    public class FeaturedTour
    {
        [FindsBy(How = How.CssSelector, Using = "div.hotel-image a div.img img")]
        private IWebElement imageMainTour;

        [FindsBy(How = How.CssSelector, Using = "div.hotel-body h3")]
        private IWebElement labelTourTitle;

        [FindsBy(How = How.CssSelector, Using = "div.hotel-body")]
        private IWebElement labelLocation;

        [FindsBy(How = How.CssSelector, Using = "span.stars i.fa-star")]
        private IList<IWebElement> imagesStars;

        [FindsBy(How = How.CssSelector, Using = "div.hotel-person span small")]
        private IWebElement labelCurrency;

        [FindsBy(How = How.CssSelector, Using = "div.hotel-person span")]
        private IWebElement labelPrice;

        [FindsBy(How = How.CssSelector, Using = "div.hotel-person ~ a.btn-block")]
        private IWebElement buttonBookNow;



        public string GetTourName()
        {
            return labelTourTitle.Text;
        }

        public int GetNumberOfStars()
        {
            return imagesStars.Count;
        }

        public string GetCurrency()
        {
            return labelCurrency.Text;
        }

        public double GetPrice()
        {
            var txt = labelPrice.Text.Split(' ')[1];
            return double.Parse(txt, NumberStyles.AllowThousands);
        }

        public TourDetails BookNow()
        {
            buttonBookNow.Click();
            return new TourDetails();
        }

    }

}
