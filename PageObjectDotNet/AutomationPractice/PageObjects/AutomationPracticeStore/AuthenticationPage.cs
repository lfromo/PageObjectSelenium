using AutomationPractice.PageObjects.AutomationPracticeStore.Objects;
using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;

namespace AutomationPractice.PageObjects.AutomationPracticeStore
{
    public class AuthenticationPage : StorePage
    {
        private readonly By formErrors = By.CssSelector("div.form-error");


        public LoginForm AlreadyRegistered() 
        {
            var form = new LoginForm();
            PageFactory.InitElements(Driver, form);
            return form;
        }


        public NewAccount CreateAccount() 
        {
            return PageFactory.InitElements<NewAccount>(Driver);
        }

        public bool AreThereErrors() { return Driver.FindElements(formErrors).Count > 0; }

    }
}