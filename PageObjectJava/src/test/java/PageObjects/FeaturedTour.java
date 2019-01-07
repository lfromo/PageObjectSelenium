package PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class FeaturedTour {

    @FindBy(css = "div.hotel-image a div.img img")
    private WebElement imageMainTour;

    @FindBy(css = "div.hotel-body h3")
    private WebElement labelTourTitle;

    @FindBy(css = "div.hotel-body")
    private WebElement labelLocation;

    @FindBy(css = "span.stars i.fa-star")
    private List<WebElement> imagesStars;

    @FindBy(css = "div.hotel-person span small")
    private WebElement labelCurrency;


    @FindBy(css = "div.hotel-person span")
    private WebElement labelPrice;

    @FindBy(css = "div.hotel-person ~ a.btn-block")
    private WebElement buttonBookNow;



    public String getTourName()
    {
        return labelTourTitle.getText();
    }

    public int getNumberOfStars()
    {
        return imagesStars.size();
    }

    public String getCurrency()
    {
        return labelCurrency.getText();
    }

    public double getPrice()
    {
        String txt = labelPrice.getText().split( " ")[1];
        return Double.parseDouble(txt);
    }

    public TourDetails bookNow()
    {
        buttonBookNow.click();
        return new TourDetails();
    }

}
