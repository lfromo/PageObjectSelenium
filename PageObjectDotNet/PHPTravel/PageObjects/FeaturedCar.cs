using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using SeleniumFramework.Base;
using System.Collections.Generic;
using System.Globalization;

namespace PHPTravel.PageObjects
{
    public class FeaturedCar : BasePage
    {
        [FindsBy(How = How.CssSelector, Using = "i.fa-star")]
        private IList<IWebElement> rate;

        [FindsBy(How = How.CssSelector, Using = "span.text-center")]
        private IWebElement currencyAndPrice;

        [FindsBy(How = How.CssSelector, Using = "h4.ellipsis")]
        private IWebElement name;

        [FindsBy(How = How.CssSelector, Using = "div.wow p")]
        private IWebElement location;


        public int GetRate()
        {
            return rate.Count;
        }

        public string GetCarName()
        {
            return Support.GetTextUsingJavaScript(Driver, name);
        }

        public float GetCarPrice()
        {
            var txt = currencyAndPrice.Text.Split('$')[1];
            return float.Parse(txt, NumberStyles.AllowThousands);
        }

        public string GetCurrency()
        {
            return currencyAndPrice.FindElement(By.TagName("small")).Text;
        }

        public string GetLocation()
        {
            return Support.GetTextUsingJavaScript(Driver, location);
        }

    }
}
