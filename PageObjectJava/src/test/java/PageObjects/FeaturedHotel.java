package PageObjects;

import Support.Support;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FeaturedHotel {

    private By hotelName = By.cssSelector("div.country-name h4");
    private By hotelLocation = By.cssSelector("div.country-name p");
    private By hotelRate = By.cssSelector("div.additional-info i.fa-star");
    private By hotelMaxAllowedRate = By.cssSelector("div.additional-info i.star");
    private By hotelCurrencyAndPrice = By.cssSelector("div.additional-info div.pull-right span.text-center");
    private By hotelBookNowButton = By.cssSelector("loader");

    private final WebElement webItem;

    public FeaturedHotel(WebElement hotelWebItem)
    {
        webItem = hotelWebItem;
    }


    public String getHotelName()
    {
        WebElement _nameItem = webItem.findElement(hotelName);
        return Support.extractTextWithJavaScript(_nameItem);
    }

    public String getHotelLocation()
    {
        WebElement _locationItem = webItem.findElement(hotelLocation);
        return Support.extractTextWithJavaScript(_locationItem);
    }

    public int getHotelRate()
    {
        return webItem.findElements(hotelRate).size();
    }

    public int getHotelMaxAllowedRate()
    {
        return webItem.findElements(hotelMaxAllowedRate).size();
    }

    public String getHotelCurrency()
    {
        WebElement _currencyItm = webItem.findElement(hotelCurrencyAndPrice);
        return Support.extractTextWithJavaScript(_currencyItm.findElement(By.tagName("small")));
    }

    public float getHotelPrice()
    {
        WebElement _priceItm = webItem.findElement(hotelCurrencyAndPrice);
        String _priceStr = Support.extractTextWithJavaScript(_priceItm);
        String _priceNoSymbolStr = _priceStr.substring(_priceStr.indexOf("$") + 1);
        return Float.parseFloat(_priceNoSymbolStr);
    }

    public HotelDetails bookNow()
    {
        WebElement button = webItem.findElement(hotelBookNowButton);
        Support.javaScriptClick(button);
        return new HotelDetails();
    }

}
