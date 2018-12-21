using NUnit.Framework;
using OpenQA.Selenium;
using PHPTravel.PageObjects;
using System.Linq;
using SeleniumFramework.Base;

namespace PHPTravel
{
    public class PHPTest : BaseTest
    {
        public PHPTest(IWebDriver driver, string url) : base(driver, url) { }


        [Test]
        public void TestFeaturedTours()
        {
            var _home = new PhpTravelHomePage();
            var _tours = _home.GetFeaturedTours().ToList();
            _tours.ForEach(tour => {
                Assert.Multiple(() => {
                    Assert.IsNotEmpty(tour.GetTourName(), "A name for the tour was not found");
                    Assert.NotZero(tour.GetNumberOfStars(), $"The tour {tour.GetTourName()} does not have a rate");
                    Assert.NotZero(tour.GetPrice(), $"The price for tour {tour.GetTourName()} should not be zero");
                    Assert.IsNotEmpty(tour.GetCurrency(), $"The currency for tour {tour.GetTourName()} was not found");
                });
            });
        }

        [Test]
        public void TestFeaturedCars()
        {
            var _home = new PhpTravelHomePage();
            var cars = _home.GetFeaturedCars().ToList();
            Assert.NotZero(cars.Count, "No cars were found in the home page");
            cars.ForEach(car => {
                Assert.Multiple(() => {
                    Assert.IsNotEmpty(car.GetCarName(), "A car was found without a name assigned");
                    Assert.NotZero(car.GetCarPrice(), $"A car was found without a price. Name: {car.GetCarName()}");
                    Assert.NotZero(car.GetRate(), $"A car was found with zero rate. Name: {car.GetCarName()}");
                    Assert.IsNotEmpty(car.GetCurrency(), $"The currency for car {car.GetCarName()} was not found");
                    Assert.IsNotEmpty(car.GetLocation(), $"The location for car {car.GetCarName()} was not found");
                });

            });

        }

        [Test]
        public void TestFeaturedHotels()
        {
            var _home = new PhpTravelHomePage();
            var hotels = _home.GetFeaturedHotels();
            Assert.AreEqual(8, hotels.Count(), "The number of hotels found is not the expected");
            hotels.ToList().ForEach(hotel => {
                Assert.Multiple(() => {
                    Assert.IsNotEmpty(hotel.GetHotelName(), "An hotel was found without a name assigned");
                    Assert.NotZero(hotel.GetHotelPrice(), $"An hotel was found without a price. Name: {hotel.GetHotelName()}");
                    Assert.NotZero(hotel.GetHotelRate(), $"An hotel was found with zero rate. Name: {hotel.GetHotelName()}");
                    Assert.IsNotEmpty(hotel.GetHotelCurrency(), $"The currency for hotel {hotel.GetHotelName()} was not found");
                    Assert.IsNotEmpty(hotel.GetHotelLocation(), $"The location for hotel {hotel.GetHotelName()} was not found");
                });
            });
        }

        [Test]
        public void PrintTourNames()
        {
            var _home = new PhpTravelHomePage();
            var trips = _home.GetFeaturedTours().ToList();
            var _lastTour = trips.Last();
            TestContext.Progress.WriteLine($"The last tour name is : {_lastTour.GetTourName()}");
            trips.Remove(_lastTour);
            var _secondTolastTour = trips.Last();
            TestContext.Progress.WriteLine($"The last tour name is : {_secondTolastTour.GetTourName()}");
        }

        [Test]
        public void ToursWithCostGreaterThan60()
        {
            var _home = new PhpTravelHomePage();
            var trips = _home.GetFeaturedTours().ToList();
            TestContext.Progress.WriteLine($"Total number of trips: {trips.Count}");
            var selectedTrips = trips.Where(trip => trip.GetPrice() > 60);
            selectedTrips.ToList().ForEach(trip => {
                TestContext.Progress.WriteLine($"Trip Name: {trip.GetTourName()}");
                TestContext.Progress.WriteLine($"Trip Cost: {trip.GetPrice()}");
                TestContext.Progress.WriteLine($"--------------------------------");
            });
        }

        [Test]
        public void CurrencyForAllHotels()
        {
            var _home = new PhpTravelHomePage();
            var hotels = _home.GetFeaturedHotels().ToList();
            Assert.Multiple(() => {
                foreach (var hotel in hotels)
                {
                    Assert.IsNotEmpty(hotel.GetHotelCurrency(), $"The currency for {hotel.GetHotelName()} was not found");
                    TestContext.Progress.WriteLine($"HOTEL: {hotel.GetHotelName()}, CURRENCY: {hotel.GetHotelCurrency()}");
                }
            });
        }

        [Test]
        public void CarIsNotForFree()
        {
            var _home = new PhpTravelHomePage();
            var cars = _home.GetFeaturedCars().ToList();
            Assert.GreaterOrEqual(cars.Count,2);
            Assert.NotZero(cars[1].GetCarPrice(), $"The car {cars[1].GetCarName()} is given for free!");
            TestContext.Progress.WriteLine($"2nd Car: {cars[1].GetCarName()}, PRICE: {cars[1].GetCarPrice()}");
        }


        [Test]
        public void YourePickyArentYa()
        {
            var _home = new PhpTravelHomePage();
            var trips = _home.GetFeaturedTours().ToList();
            var selectedTrip = trips.Where(trip => trip.GetNumberOfStars() > 3).First();
            selectedTrip.BookNow();
        }


    }
}
