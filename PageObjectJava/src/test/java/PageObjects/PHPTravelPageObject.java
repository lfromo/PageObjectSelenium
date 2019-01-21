package PageObjects;

import Base.BasePageObject;

public abstract class PHPTravelPageObject extends BasePageObject {

    public Header getHeader(){
        return new Header();
    }


    public Footer getFooter(){
        return new Footer();
    }

}
