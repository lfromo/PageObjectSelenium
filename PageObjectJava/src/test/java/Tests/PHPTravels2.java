package Tests;

import Base.BaseTest;
import PageObjects.FeaturedCar;
import PageObjects.FeaturedHotel;
import PageObjects.FeaturedTour;
import PageObjects.PHPTravelsHome;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PHPTravels2 extends BaseTest {


    @Test
    public void PrintLastTwoTourNames()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> trips = _home.getFeaturedTours();
        FeaturedTour _lastTour = trips.get(trips.size() - 1);
        System.out.println(String.format("The last tour name is %s", _lastTour.getTourName()));
        trips.remove(_lastTour);
        FeaturedTour _secondTolastTour = trips.get(trips.size() - 1);
        System.out.println(String.format("The last tour name is %s", _secondTolastTour.getTourName()));
    }

    @Test
    public void CurrencyForAllHotels()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedHotel> hotels = _home.getFeaturedHotels();
        for(FeaturedHotel hotel : hotels){
            String hotelName = hotel.getHotelName();
            Assert.assertFalse(hotelName.isEmpty(), "No hotel name was found for one of the items");
            Assert.assertFalse(hotel.getHotelCurrency().isEmpty(), String.format("The currency for hotel '%s' was not found", hotelName));
            Assert.fail("Forcing again the test to fail");
        }
    }

    @Test
    public void CarIsNotForFree()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedCar> cars = _home.getFeaturedCars();
        Assert.assertTrue(cars.size() >= 2, "Not enough cars found. At least 2 must be found");
        Assert.assertNotEquals(cars.get(1).getCarPrice(), 0,String.format("The car '%s' is given for free!",cars.get(1).getCarName()));
    }

    @Test
    public void YourePickyArentYa()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> trips = _home.getFeaturedTours();
        FeaturedTour foundTour = trips.stream().filter(trip -> trip.getNumberOfStars() > 3)
                .findFirst()
                .orElseThrow(AssertionError::new);
        foundTour.bookNow();
    }

}
