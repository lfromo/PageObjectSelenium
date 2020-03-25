package Tests;

import Base.BaseTest;
import PageObjects.AutomationPracticeStore.ErrorMessageException;
import PageObjects.AutomationPracticeStore.NotLoggedInException;
import PageObjects.AutomationPracticeStore.Objects.*;
import PageObjects.AutomationPracticeStore.StoreHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.List;
import java.util.Map;

public class AutomationPractice extends BaseTest {


    @Test
    public void loginTest(){
        StoreHomePage po = new StoreHomePage();
        Assert.assertFalse(po.getHeader().isUserLoggedIn(), "A user is already logged in!. The name is: %s");
        LoginForm loginSection = po.getHeader().signIn().alreadyRegistered();
        try {
            loginSection.setExistentEmailAddress("blublubla@mailinator.com")
                    .setPassword("afakepassword")
                    .pressSignInButton();
            Assert.fail("The expected ErrorMessageException was not thrown!");
        }catch (ErrorMessageException ex){
            assertThat(ex.getErrorsList(), hasItem("Authentication failed."));
        }
    }

    @Test
    public void invalidEmail(){
        StoreHomePage po = new StoreHomePage();
        Assert.assertFalse(po.getHeader().isUserLoggedIn(), "A user is already logged in!. The name is: %s");
        LoginForm loginSection = po.getHeader().signIn().alreadyRegistered();
        try {
            loginSection.setExistentEmailAddress("blublubla")
                    .pressSignInButton();
            Assert.fail("The expected ErrorMessageException was not thrown!");
        }catch (ErrorMessageException ex){
            assertThat(ex.getErrorsList(), hasItem("Invalid email address."));
        }
    }

    @Test
    public void GeneralTest(){
        StoreHomePage po = new StoreHomePage();
        List<StoreItemThumbnail> allItems =  po.getBestSellerItems();
        Header header = po.getHeader();
        List<String> catsAvailable = header.getCategoriesAvailable();

        String thumbImgUrl = allItems.get(4).getImageUrl();
        String thumbName = allItems.get(4).getName();
        double thumbPrice = allItems.get(4).getPrice();
        int thumbDiscount = allItems.get(4).getDiscountPercentage();
        double thumbPriceBeforeDiscount = allItems.get(4).getPriceBeforeDiscount();

        double thumbContextPrice =  allItems.get(4).getContextPrice();
        int thumbContextDiscount = allItems.get(4).getContextDiscountPercentage();
        double thumbContextPriceBeforeDiscount = allItems.get(4).getContextPriceBeforeDiscount();

        QuickView qv = allItems.get(1).getQuickView();
        List<String> thumbs = qv.getThumbnailPicturesSourceList();
        String prodName = qv.getProductName();
        double price = qv.getProductPrice();
        int discountPercentage = qv.getProductDiscount();
        String model = qv.getProductModel();
        String condition = qv.getProductCondition();
        String description = qv.getProductDescription();
        double oldPrice = qv.getPriceBeforeDiscount();
        String bigPicUrl = qv.getCurrentPictureSource();
        qv.setQuantity("3");
        do{
            qv.incrementQuantity();
        }while(qv.getCurrentQuantity() < 10);
        int qty = qv.decrementQuantity();
        qv.decrementQuantity();
        Assert.assertEquals(qv.getCurrentQuantity(), 8);
        qty = qv.getCurrentQuantity();
        List<String> sizes = qv.getAvailableSizes();
        Map<String,String> colors = qv.getColorsAvailable();
        qv.selectColorByName("Blue");
        Assert.assertThrows(NotLoggedInException.class, qv::addToWishList);
        qv.closePopupMessage();
        qv.closeQuickView();
    }

    @Test
    public void AddToCartRemoveFromMiniCart(){
        StoreHomePage po = new StoreHomePage();
        List<StoreItemThumbnail> allItemsInScreen = po.getActiveItems();
        double totalPrice = 0.0;
        ProductInCartPopUp pop;
        for(StoreItemThumbnail item : allItemsInScreen){
            totalPrice += item.getPrice();
            pop = item.addToCart();
            pop.continueShopping();
        }
        MiniCart miniCart = po.getHeader().getContextualCart();
        List<MiniCartItem> miniItems = miniCart.getItems();
        Assert.assertEquals(miniItems.size(), allItemsInScreen.size(),
                "The number of items in the minicart does not match with the ones displayed");
        double cartTotal = miniCart.getTotal() - miniCart.getShippingCost();
        Assert.assertEquals(totalPrice, cartTotal,
                "The total of the sum of the products does not match with the one in the mini cart");
        for(MiniCartItem itm : miniItems)
            itm.removeItem();
    }

}
