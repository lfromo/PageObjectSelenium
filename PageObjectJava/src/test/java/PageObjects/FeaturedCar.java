package PageObjects;

import Base.BasePageObject;
import Support.Support;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class FeaturedCar extends BasePageObject {

    @FindBy(css = "i.fa-star")
    private List<WebElement> rate;

    @FindBy(css = "span.text-center")
    private WebElement currencyAndPrice;

    @FindBy(tagName = "h4")
    private WebElement name;

    @FindBy(css = "div.wow p")
    private WebElement location;


    public int getRate()
    {
        return rate.size();
    }

    public String getCarName()
    {
        return Support.extractTextWithJavaScript(name);
    }

    public float getCarPrice()
    {
        String txt = currencyAndPrice.getText();
        txt = txt.substring(txt.indexOf(" ") + 2);
        return Float.parseFloat(txt);
    }

    public String getCurrency()
    {
        return currencyAndPrice.findElement(By.tagName("small")).getText();
    }

    public String getLocation()
    {
        return Support.extractTextWithJavaScript(location);
    }

}

