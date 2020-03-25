using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium.Support.UI;
using SeleniumFramework.Base;
using System;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class NewAccount
    {

        [FindsBy(How = How.Id, Using = "email_create")]
        private IWebElement inputCreateAccountEmailAddress;


        [FindsBy(How = How.Id, Using = "SubmitCreate")]
        private IWebElement buttonCreateAccount;


        public NewAccount(IWebDriver driver) { }

        public NewAccount SetEmailAddress(string emailAddress) 
        {
            inputCreateAccountEmailAddress.Clear();
            inputCreateAccountEmailAddress.SendKeys(emailAddress);
            return this;
        }

        public string GetEmailAddress() 
        {
            return inputCreateAccountEmailAddress.GetAttribute("value");
        }

        public NewAccountForm PressCreateAccountButton() 
        {
            var driver = TestConfiguration.GetDriverInstance();
            var wait = new WebDriverWait(driver, TimeSpan.FromSeconds(2));
            buttonCreateAccount.Click();

            try {
                wait.Until(ExpectedConditions.ElementIsVisible(By.Id("create_account_error")));
            } catch (WebDriverTimeoutException) 
            {
                return new NewAccountForm();
            }
            var errorsFound = new List<string>();
            var errors = driver.FindElements(By.CssSelector("div#create_account_error li"));
            errorsFound.AddRange(errors.Select(error => error.Text));
            throw new ErrorMessageException(errorsFound);
        }

    }
}