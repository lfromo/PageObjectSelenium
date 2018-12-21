using OpenQA.Selenium;
using OpenQA.Selenium.Support.PageObjects;
using System.Collections.Generic;
using System.Linq;

namespace PHPTravel.PageObjects
{
    public class PhpTravelHomePage : PHPTravelPage
    {
        private By featuredToursSection = By.CssSelector("div.vc_column-gap-30");

        private By featuredCarsLocator = By.CssSelector("div.additional-info-cars");

        [FindsBy(How = How.CssSelector, Using = "div.hotels div.owl-wrapper > div.owl-item")]
        private IList<IWebElement> featuredHotels;

        public IEnumerable<FeaturedTour> GetFeaturedTours()
        {
            var list = new List<FeaturedTour>();
            var features = Driver.FindElement(featuredToursSection) // First we try to find the section itself...
                .FindElements(By.CssSelector("div.hotel-item")); // ...then proceed to get all the items inside of it...

            foreach (IWebElement we in features)
            {
                var feature = new FeaturedTour();//...and each of these items has the configuration specified in the FeaturedTour PO class
                PageFactory.InitElements(we, feature);
                list.Add(feature);
            }
            
            return list;
        }

        public IEnumerable<FeaturedCar> GetFeaturedCars()
        {
            var list = new List<FeaturedCar>();

            var featuredCars = Driver.FindElements(featuredCarsLocator);

            featuredCars.ToList().ForEach(carWebItm => {
                var _car = new FeaturedCar();
                PageFactory.InitElements(carWebItm, _car);
                list.Add(_car);
            });

            return list;
        }

        public IEnumerable<FeaturedHotel> GetFeaturedHotels()
        {
            var list = new List<FeaturedHotel>();
            featuredHotels.ToList().ForEach(hotelItm => {
                var _hotel = new FeaturedHotel(hotelItm);
                list.Add(_hotel);
            });

            return list;
        }

    }
}