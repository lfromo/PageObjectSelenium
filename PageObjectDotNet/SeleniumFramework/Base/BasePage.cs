
using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;

namespace SeleniumFramework.Base
{
    public abstract class BasePage
    {
        protected IWebDriver Driver { get; } = TestConfiguration.GetDriverInstance();

        public BasePage()
        {
            PageFactory.InitElements(Driver, this);
        }
        
    }
}
