package PageObjects;

import Base.BasePage;

public abstract class PHPTravelPage extends BasePage {

    public Header getHeader(){
        return new Header();
    }


    public Footer getFooter(){
        return new Footer();
    }

}
