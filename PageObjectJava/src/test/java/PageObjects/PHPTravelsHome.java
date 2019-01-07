package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.util.ArrayList;
import java.util.List;

public class PHPTravelsHome extends PHPTravelPage {


    private By featuredToursSection = By.cssSelector("div.vc_column-gap-30");
    private By featuredCarsLocator = By.cssSelector("div.additional-info-cars");

    @FindBy(css = "div.hotels div.owl-wrapper > div.owl-item")
    private List<WebElement> featuredHotels;

    public List<FeaturedTour> getFeaturedTours()
    {
        ArrayList list = new ArrayList<FeaturedTour>();
        List<WebElement> features = _driver.findElement(featuredToursSection) // First we try to find the section itself...
                .findElements(By.cssSelector("div.hotel-item")); // ...then proceed to get all the items inside of it...

        for (WebElement we : features)
        {
            FeaturedTour feature = new FeaturedTour();//...and each of these items has the configuration specified in the FeaturedTour PO class
            ElementLocatorFactory factory = new DefaultElementLocatorFactory(we);
            PageFactory.initElements(factory, feature);
            list.add(feature);
        }

        return list;
    }

    public List<FeaturedCar> getFeaturedCars()
    {
        List list = new ArrayList();
        List<WebElement> featuredCars = _driver.findElements(featuredCarsLocator);

        featuredCars.stream().forEach(carWebItm -> {
            FeaturedCar _car = new FeaturedCar();
            ElementLocatorFactory factory = new DefaultElementLocatorFactory(carWebItm);
            PageFactory.initElements(factory, _car);
            list.add(_car);
        });

        return list;
    }

    public List<FeaturedHotel> getFeaturedHotels()
    {
        List list = new ArrayList<FeaturedHotel>();
        featuredHotels.stream().forEach(hotelWebItm -> {
            FeaturedHotel _hotel = new FeaturedHotel(hotelWebItm);
            list.add(_hotel);
        });

        return list;
    }
}
