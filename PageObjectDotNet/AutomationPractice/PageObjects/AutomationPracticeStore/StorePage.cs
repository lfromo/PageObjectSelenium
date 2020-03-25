using AutomationPractice.PageObjects.AutomationPracticeStore.Objects;
using SeleniumFramework.Base;

namespace AutomationPractice.PageObjects.AutomationPracticeStore
{
    public class StorePage : BasePage
    {

        public Header Header
        {
            get
            {
                return new Header();
            }
        }


        public Footer Footer
        {
            get
            {
                return new Footer();
            }
        }

    }
}