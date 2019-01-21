package Tests;

import Base.BaseTest;
import PageObjects.FeaturedCar;
import PageObjects.FeaturedHotel;
import PageObjects.FeaturedTour;
import PageObjects.PHPTravelsHome;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;

public class PHPTravels1 extends BaseTest {


    @Test
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

    @Test
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

    @Test
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

    @Test
    public void ToursWithCostGreaterThan60()
    {
        PHPTravelsHome _home = new PHPTravelsHome();
        List<FeaturedTour> trips = _home.getFeaturedTours();
        List<FeaturedTour> selectedTrips = trips.stream().filter(trip -> trip.getPrice() > 60).collect(Collectors.toList());
        SoftAssert soft = new SoftAssert();
        selectedTrips.stream().forEach(trip -> {
            soft.assertTrue(trip.getPrice() > 60, String.format("The trip '%s' cost is not greater than 60, costs '%s'", trip.getTourName(), trip.getPrice()));
        });
        soft.assertAll();
        Assert.fail("Just forcing the test to fail xdxdxd");
    }
}
