package PageObjects.AutomationPracticeStore.Objects;

import Base.BasePageObject;

import PageObjects.AutomationPracticeStore.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MiniCart extends BasePageObject {

    private final By miniItemsLocator = By.tagName("dt");
    private final By labelShippingLocator = By.className("cart_block_shipping_cost");
    private final By labelTotalLocator = By.className("cart_block_total");
    private final By buttonCheckoutLocator = By.id("button_order_cart");

    private final By miniCartLocator = By.className("cart_block_list");

    private final WebElement miniCart;

    public MiniCart(){
        try{
            WebDriverWait wait = new WebDriverWait(_driver, 3);
            wait.until(ExpectedConditions.visibilityOfElementLocated(buttonCheckoutLocator));
            this.miniCart = _driver.findElement(miniCartLocator);
        }catch(WebDriverException toEx){
            throw new WebDriverException("The context cart was not displayed", toEx.getCause());
        }
    }

    public List<MiniCartItem> getItems(){
        List<MiniCartItem> foundItems = new ArrayList<>();
        List<WebElement> items = this.miniCart.findElements(miniItemsLocator);
        items.stream().forEach(item -> {
            MiniCartItem miniItem= new MiniCartItem(item);
            ElementLocatorFactory factory = new DefaultElementLocatorFactory(item);
            PageFactory.initElements(factory, miniItem);
            foundItems.add(miniItem);
        });
        return foundItems;
    }

    public double getShippingCost(){
        String txt = _driver.findElement(labelShippingLocator).getText();
        return txt.equals("Free shipping!") ? 0.0 : Double.parseDouble(txt.substring(1));
    }

    public double getTotal(){
        String txt = _driver.findElement(labelTotalLocator).getText().substring(1);
        return Double.parseDouble(txt);
    }

    public CartPage pressCheckoutButton(){
        _driver.findElement(buttonCheckoutLocator).click();
        return new CartPage();
    }

}
