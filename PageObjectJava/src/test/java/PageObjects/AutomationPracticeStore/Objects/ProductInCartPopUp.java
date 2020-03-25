package PageObjects.AutomationPracticeStore.Objects;

import Base.BasePageObject;
import PageObjects.AutomationPracticeStore.CheckoutPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class ProductInCartPopUp extends BasePageObject {

    private final By labelProductName = By.id("layer_cart_product_title");
    private final By labelProductAttributes = By.id("layer_cart_product_attributes");
    private final By labelProductQuantity = By.id("layer_cart_product_quantity");
    private final By labelProductTotalPrice = By.id("layer_cart_product_price");

    private final By buttonContinueShopping = By.className("continue");
    private final By buttonProceedCheckout = By.cssSelector("a.btn-default");
    private final By buttonClosePopup = By.className("cross");
    private final By labelNumberOfItemsInCart = By.className("ajax_cart_quantity");
    private final By labelTotalProductsAmount = By.className("ajax_block_products_total");
    private final By labelTotalShippingAmount = By.cssSelector("div.layer_cart_row span.ajax_cart_shipping_cost");
    private final By labelTotalFinalAmount = By.cssSelector("div.layer_cart_row span.ajax_block_cart_total");

    private final By popUpLocator = By.cssSelector("div#layer_cart div.clearfix");

    private final String PLURAL_PHRASE_LOCATOR = "ajax_cart_product_txt_s";
    private final String SINGULAR_PHRASE_LOCATOR = "ajax_cart_product_txt";

    private final WebElement popUp;
    private final WebDriverWait wait;

    public ProductInCartPopUp(){
        this.wait = new WebDriverWait(_driver, 4);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(popUpLocator));
        this.popUp = _driver.findElement(popUpLocator);
    }

    public String getProductName(){
        return this.popUp.findElement(labelProductName).getText();
    }

    public int getProductQuantity(){
        return Integer.parseUnsignedInt(this.popUp.findElement(labelProductQuantity).getText());
    }

    public double getProductTotal(){
        return Double.parseDouble(this.popUp.findElement(labelProductTotalPrice).getText().substring(1));
    }

    public List<String> getProductAttributes(){
        String attributesLine = this.popUp.findElement(labelProductAttributes).getText().replace(" ","");
        return Arrays.asList(attributesLine.split(","));
    }

    public double getTotalProductsPrice(){
        return Double.parseDouble(this.popUp.findElement(labelTotalProductsAmount).getText().substring(1));
    }

    public double getTotalShippingCost(){
        return Double.parseDouble(this.popUp.findElement(labelTotalShippingAmount).getText().substring(1));
    }

    public double getCartFinalAmount(){
        return Double.parseDouble(this.popUp.findElement(labelTotalFinalAmount).getText().substring(1));
    }

    public String getTotalItemsPhrase(){
        String classLocator = this.getTotalItemsInCart() > 1 ? PLURAL_PHRASE_LOCATOR : SINGULAR_PHRASE_LOCATOR;
        return this.popUp.findElement(By.className(classLocator)).getText();
    }

    public int getTotalItemsInCart(){
        return Integer.parseUnsignedInt(this.popUp.findElement(labelNumberOfItemsInCart).getText());
    }

    public CheckoutPage proceedToCheckout(){
        this.popUp.findElement(buttonProceedCheckout).click();
        return new CheckoutPage();
    }

    public void continueShopping(){
        this.popUp.findElement(buttonContinueShopping).click();
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(popUpLocator));
    }

    public void closePopUp(){
        this.popUp.findElement(buttonClosePopup).click();
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(popUpLocator));
    }

}
