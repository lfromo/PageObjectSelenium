using SeleniumFramework.Base;

namespace PHPTravel.PageObjects
{
    public abstract class PHPTravelPage : BasePage
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
