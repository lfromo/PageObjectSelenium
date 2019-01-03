package Tests;

import Base.BaseTest;
import Base.TestConfiguration;
import PageObjects.FeaturedCar;
import PageObjects.FeaturedHotel;
import PageObjects.FeaturedTour;
import PageObjects.PHPTravelsHome;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;

@Test(dataProvider = "setEnvironmentDefault", dataProviderClass = TestConfiguration.class)
public class PHPTravels extends BaseTest {


    public PHPTravels(String driver, String url) {
        super(driver, url);
    }

    public void TestFeaturedTours()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> _tours = _home.getFeaturedTours();
        SoftAssert softAssert = new SoftAssert();
        _tours.stream().forEach(tour -> {
            softAssert.assertFalse(tour.getTourName().isEmpty(), "A name for the tour was not found");
            softAssert.assertNotEquals(tour.getNumberOfStars(),0, "The tour {tour.GetTourName()} does not have a rate");
            softAssert.assertNotEquals(tour.getPrice(),0, "The price for tour {tour.GetTourName()} should not be zero");
            softAssert.assertFalse(tour.getCurrency().isEmpty(), String.format("The currency for tour '%s' was not found", tour.getTourName()));
        });
        softAssert.assertAll();
    }

    public void TestFeaturedCars()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedCar> cars = _home.getFeaturedCars();
        Assert.assertNotEquals(cars.size(),0, "No cars were found in the home page");
        SoftAssert softAssert = new SoftAssert();
        cars.stream().forEach(car -> {
            String carName = car.getCarName();
            Assert.assertFalse(carName.isEmpty(), "A car was found without a name assigned");
            softAssert.assertNotEquals(car.getCarPrice(),0, String.format("A car was found without a price. Name: %s", carName));
            softAssert.assertNotEquals(car.getRate(), 0, String.format("A car was found with zero rate. Name: %s", carName));
            softAssert.assertFalse(car.getCurrency().isEmpty(), String.format("The currency for car '%s' was not found", carName));
            softAssert.assertFalse(car.getLocation().isEmpty(), String.format("The location for car '%s' was not found", carName));
        });

        softAssert.assertAll();
    }

    public void TestFeaturedHotels()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedHotel> hotels = _home.getFeaturedHotels();
        Assert.assertEquals(hotels.size(),8, "The number of hotels found is not the expected");
        SoftAssert softAssert = new SoftAssert();
        hotels.stream().forEach(hotel -> {
            String hotelName = hotel.getHotelName();
            Assert.assertFalse(hotelName.isEmpty(), "An hotel was found without a name assigned");
            softAssert.assertNotEquals(hotel.getHotelPrice(), 0, String.format("An hotel was found without a price. Name: %s", hotelName));
            softAssert.assertNotEquals(hotel.getHotelRate(), 0, String.format("An hotel was found with zero rate. Name: %s", hotelName));
            softAssert.assertFalse(hotel.getHotelCurrency().isEmpty(), String.format("The currency for hotel '%s' was not found", hotelName));
            softAssert.assertFalse(hotel.getHotelLocation().isEmpty(), String.format("The location for hotel '%s' was not found", hotelName));
        });
        softAssert.assertAll();
    }

    public void PrintLastTwoTourNames()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> trips = _home.getFeaturedTours();
        FeaturedTour _lastTour = trips.get(trips.size() - 1);
        System.console().writer().println(String.format("The last tour name is %s", _lastTour.getTourName()));
        trips.remove(_lastTour);
        FeaturedTour _secondTolastTour = trips.get(trips.size() - 1);
        System.console().writer().println(String.format("The last tour name is %s", _secondTolastTour.getTourName()));
    }

    public void ToursWithCostGreaterThan60()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> trips = _home.getFeaturedTours();
        List<FeaturedTour> selectedTrips = trips.stream().filter(trip -> trip.getPrice() > 60).collect(Collectors.toList());
        selectedTrips.stream().forEach(trip -> {
            System.console().writer().println(String.format("Trip Name: %s", trip.getTourName()));
            System.console().writer().println(String.format("Trip Cost: %s", trip.getPrice()));
            System.console().writer().println("------------------------------");
        });
    }

    public void CurrencyForAllHotels()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedHotel> hotels = _home.getFeaturedHotels();
        for(FeaturedHotel hotel : hotels){
            String hotelName = hotel.getHotelName();
            Assert.assertFalse(hotelName.isEmpty(), "No hotel name was found for one of the items");
            Assert.assertFalse(hotel.getHotelCurrency().isEmpty(), String.format("The currency for hotel '%s' was not found", hotelName));
        }
    }

    public void CarIsNotForFree()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedCar> cars = _home.getFeaturedCars();
        Assert.assertTrue(cars.size() >= 2, "Not enough cars found. At least 2 must be found");
        Assert.assertNotEquals(cars.get(1).getCarPrice(), 0,String.format("The car '%s' is given for free!",cars.get(1).getCarName()));
    }


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
