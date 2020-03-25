package PageObjects.AutomationPracticeStore;

import Base.BasePageObject;
import PageObjects.AutomationPracticeStore.Objects.Footer;
import PageObjects.AutomationPracticeStore.Objects.Header;

public class StorePage extends BasePageObject {

    public Header getHeader(){
        return new Header();
    }

    public Footer getFooter(){
        return new Footer();
    }

}
