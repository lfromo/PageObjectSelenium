using AutomationPractice.PageObjects.AutomationPracticeStore;
using AutomationPractice.PageObjects.AutomationPracticeStore.Objects;
using NUnit.Framework;
using OpenQA.Selenium;
using SeleniumFramework.Base;

namespace AutomationPractice
{
    public class AutomationPracticeTests : BaseTest
    {

        public AutomationPracticeTests(IWebDriver driver, string url) : base(driver, url) { }


        [Test]
        public void LoginTest() 
        {
            var po = new StoreHomePage();
            Assert.IsFalse(po.Header.IsUserLoggedIn(), "A user is already logged in! Fail the test!!");
            var loginSection = po.Header.SignIn().AlreadyRegistered();
            var ex = Assert.Throws<ErrorMessageException>(() => {
                loginSection.SetExistentEmailAddress("blublubla@mailinator.com").SetPassword("AFakePassword").PressSignInButton();
            });
            Assert.That(ex.ErrorList, Contains.Item("Authentication failed."));
        }

        [Test]
        public void InvalidEmail() 
        {
            var po = new StoreHomePage();
            Assert.IsFalse(po.Header.IsUserLoggedIn(), "A user is already logged in! Fail the test!!");
            var loginSection = po.Header.SignIn().AlreadyRegistered();
            var ex = Assert.Throws<ErrorMessageException>(() => {
                loginSection.SetExistentEmailAddress("blublubla").PressSignInButton();
            });
            Assert.That(ex.ErrorList, Contains.Item("Invalid email address."));
        }

        [Test]
        public void GeneralTest() 
        {
            var po = new StoreHomePage();
            var allItems = po.GetBestSellerItems();
            CollectionAssert.IsNotEmpty(allItems, "Could not retreive the best seller items!");
            var header = po.Header;
            var catsAvailable = header.GetCategoriesAvailable();

            CollectionAssert.IsNotEmpty(catsAvailable, "No categories were found!");

            Assert.Multiple(() => {
                Assert.IsNotEmpty(allItems[3].GetImageURL(), "No image URL as found!");
                Assert.IsNotEmpty(allItems[3].GetName(), "The product name was not found!");
                Assert.AreNotEqual(0, allItems[3].GetPrice(), "The price was not found!");
                Assert.AreNotEqual(0, allItems[3].GetDiscountPercentage(), "The item does not have discount");
                Assert.AreNotEqual(0, allItems[3].GetPriceBeforeDiscount(), "The item has not price before discount");
                Assert.AreNotEqual(0, allItems[3].GetContextPrice(), "The item does not have context price!");
                Assert.AreNotEqual(0, allItems[3].GetContextDiscountPercentage(), "The item does not have context discount!");
                Assert.AreNotEqual(0, allItems[3].GetContextPriceBeforeDiscount(), "The item does not have context price before discount!");
            });

            var qv = allItems[1].GetQuickView();
            var thumbs = qv.GetThumbnailPicturesSourceList();
            string prodName = qv.GetProductName();
            double price = qv.GetProductPrice();
            int discountPercentage = qv.GetProductDiscount();
            string model = qv.GetProductModel();
            string condition = qv.GetProductCondition();
            string description = qv.GetProductDescription();
            double oldPrice = qv.GetPriceBeforeDiscount();
            string bigPicUrl = qv.GetCurrentPictureSource();
            qv.SetQuantity("3");
            do
            {
                qv.IncrementQuantity();
            } while (qv.GetCurrentQuantity() < 10);
            int qty = qv.DecrementQuantity();
            qv.DecrementQuantity();
            Assert.AreEqual(qv.GetCurrentQuantity(), 8);
            qty = qv.GetCurrentQuantity();
            var sizes = qv.GetAvailableSizes();
            var colors = qv.GetColorsAvailable();
            qv.SelectColorByName("Blue");
            Assert.Throws<NotLoggedInException>(() => { qv.AddToWishList(); });
        qv.ClosePopupMessage();
        qv.CloseQuickView();
        }

        [Test]
        public void AddToCartRemoveFromMiniCart() 
        {
            var po = new StoreHomePage();
            var allItemsInScreen = po.GetActiveItems();
            var totalPrice = 0.0;
            ProductInCartPopUp pop;
            foreach (var item in allItemsInScreen)
            {
                totalPrice += item.GetPrice();
                pop = item.AddToCart();
                pop.ContinueShopping();
            }
            var miniCart = po.Header.GetContextualCart();
            var miniItems = miniCart.GetItems();
            Assert.AreEqual(miniItems.Count, allItemsInScreen.Count,
                    "The number of items in the minicart does not match with the ones displayed");
            var cartTotal = miniCart.GetTotal() - miniCart.GetShippingCost();
            Assert.AreEqual(totalPrice, cartTotal,
                    "The total of the sum of the products does not match with the one in the mini cart");
            foreach(var itm in miniItems)
                itm.RemoveItem();
        }

    }
}
