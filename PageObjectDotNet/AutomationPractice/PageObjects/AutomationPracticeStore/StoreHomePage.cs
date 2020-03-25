using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using AutomationPractice.PageObjects.AutomationPracticeStore.Objects;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore
{
    public class StoreHomePage : StorePage
    {
        [FindsBy(How = How.Id, Using = "home-page-tabs")]
        private IWebElement homePageTabSection;

        private readonly By activePaneItemsHolder = By.CssSelector("div.tab-content ul.product_list.active");


        public IReadOnlyList<StoreItemThumbnail> GetActiveItems() 
        {
            IWebElement section = null;
            var itemList = new List<StoreItemThumbnail>();
            try
            {
                section = Driver.FindElement(activePaneItemsHolder);
            }
            catch (NoSuchElementException ex)
            {
                throw new NoSuchElementException("The active section holder was not found", ex);
            }
            section.FindElements(By.TagName("li")).ToList().ForEach(we => {
                var item = new StoreItemThumbnail();
                PageFactory.InitElements(we, item);
                itemList.Add(item);
            });
            return itemList;
        }

        public IReadOnlyList<string> GetTabsAvailable() 
        {
            return SectionsAvailable().Select(tab => tab.Text).ToList().AsReadOnly();
        }

        public StoreHomePage SelectTab(string tabName) 
        {
            var found = SectionsAvailable().Where(tab => tab.Text.Equals(tabName)).FirstOrDefault() 
                ?? throw new NoSuchElementException($"The section tab named '{tabName}' was not found");
            found.Click();
            return this;
        }

        public IReadOnlyList<StoreItemThumbnail> GetPopularItems()
        {
            SelectTab("POPULAR");
            return GetActiveItems();
        }

        public IReadOnlyList<StoreItemThumbnail> GetBestSellerItems()
        {
            SelectTab("BEST SELLERS");
            return GetActiveItems();
        }

        private IList<IWebElement> SectionsAvailable() { return homePageTabSection.FindElements(By.TagName("a")).ToList(); }

    }
}
