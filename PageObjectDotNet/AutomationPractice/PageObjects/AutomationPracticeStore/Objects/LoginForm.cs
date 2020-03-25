using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using SeleniumFramework.Base;
using System.Collections.Generic;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class LoginForm
    {

        [FindsBy(How = How.Id, Using = "email")]
        private IWebElement inputEmailAddress;

        [FindsBy(How = How.Id, Using = "passwd")]
        private IWebElement inputPassword;

        [FindsBy(How = How.CssSelector, Using = "p.lost_password a")]
        private IWebElement linkForgotYourPassword;

        [FindsBy(How = How.Id, Using = "SubmitLogin")]
        private IWebElement buttonSignIn;


        public LoginForm SetExistentEmailAddress(string emailAddress) 
        {
            inputEmailAddress.Clear();
            inputEmailAddress.SendKeys(emailAddress);
            return this;
        }

        public string GetExistentEmailAddress() 
        {
            return inputEmailAddress.GetAttribute("value");
        }


        public LoginForm SetPassword(string password) 
        {
            inputPassword.Clear();
            inputPassword.SendKeys(password);
            return this;
        }

        public string GetPassword() 
        {
            return inputPassword.GetAttribute("value");
        }


        public AccountPage PressSignInButton() 
        {
            buttonSignIn.Click();
            var driver = TestConfiguration.GetDriverInstance();
            var errorMsgBox = driver.FindElements(By.CssSelector("div.alert"));
            if (errorMsgBox.Count != 0) 
            {
                var errorWebItems = new List<IWebElement>();
                var errorList = new List<string>();
                errorWebItems.Add(errorMsgBox[0].FindElement(By.TagName("p"))); //First line of the error message.
                errorWebItems.AddRange(errorMsgBox[0].FindElements(By.TagName("li"))); //Consequent more specific error message list.

                errorWebItems.ForEach(itm => {
                    errorList.Add(itm.Text);
                });

                throw new ErrorMessageException(errorList);
            }

            return new AccountPage();
        }

        public AccountPage LoginWithUser(string userEmail, string password) 
        {
            return SetExistentEmailAddress(userEmail)
                .SetPassword(password)
                .PressSignInButton();
        }

    }
}