using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using SeleniumFramework.Base;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    public class Header : BasePage
    {

        [FindsBy(How = How.XPath, Using = "//*[@id='block_top_menu']/ul/li/a")]
        private IList<IWebElement> categoriesMenu;

        [FindsBy(How = How.CssSelector, Using = "div.shopping_cart a")]
        private IWebElement shoppingCart;

        [FindsBy(How = How.Id, Using = "search_query_top")]
        private IWebElement inputSearchBox;

        private readonly By loginButton = By.ClassName("login");
        private readonly By accountButton = By.ClassName("account");
        private readonly By signOutButton = By.ClassName("logout");


        public IReadOnlyList<string> GetCategoriesAvailable() 
        {
            var list_cats = new List<string>();
            categoriesMenu.ToList().ForEach(cat => {
                list_cats.Add(cat.Text);
            });
            return list_cats;
        }

        public void GoToCategory(string categoryName) 
        {
            var found = categoriesMenu.Where(category => string.Equals(category.Text, categoryName)).FirstOrDefault() 
                ?? throw new NoSuchElementException($"The category with name '{categoryName}' was not found");
            found.Click();
        }

        public CartPage GoToCart() 
        {
            shoppingCart.Click();
            return new CartPage();
        }

        public MiniCart GetContextualCart() 
        {
            Driver.HoverOverWebElement(shoppingCart);
            return new MiniCart();
        }

        public bool IsUserLoggedIn() 
        {
            return Driver.FindElements(loginButton).Count == 0 
                && Driver.FindElements(accountButton).Count == 1 
                && Driver.FindElements(signOutButton).Count == 1;
        }

        public void SignOut() 
        {
            var found = Driver.FindElements(signOutButton).FirstOrDefault() 
                ?? throw new NotLoggedInException("The sign out button was not found. No user seems to be logged in the system");
            found.Click();
        }

        public AuthenticationPage SignIn() 
        {
            var found = Driver.FindElements(loginButton).FirstOrDefault() 
                ?? throw new NoSuchElementException("The button for signing in was not found. A user seems to be already logged in the system");
            found.Click();
            return new AuthenticationPage();
        }

        public string LoggedUserName() 
        {
            var found = Driver.FindElements(accountButton).FirstOrDefault()
                ?? throw new NotLoggedInException("No user seems to be logged into the system");
            return found.Text;
        }

        public AccountPage GoToAccountPage() 
        {
            if(!IsUserLoggedIn())
                throw new NotLoggedInException("No user seems to be logged into the system");
            Driver.FindElement(accountButton).Click();
            return new AccountPage();
        }

    }
}