package PageObjects.AutomationPracticeStore.Objects;

import Base.TestConfiguration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniCartItem {

    @FindBy(how = How.CSS, using = "a.cart-images img")
    private WebElement thumbnailPicture;

    @FindBy(className = "quantity")
    private WebElement quantity;

    @FindBy(how = How.CLASS_NAME, using = "cart_block_product_name")
    private WebElement productName;

    @FindBy(how = How.CLASS_NAME, using = "product-atributes")
    private WebElement attributes;

    @FindBy(className = "price")
    private WebElement total;

    @FindBy(className = "ajax_cart_block_remove_link")
    private WebElement closeButton;

    private final WebElement miniItem;

    public MiniCartItem(WebElement miniItem){
        this.miniItem = miniItem;
    }

    public String getThumbnailImageLink(){
        return this.thumbnailPicture.getAttribute("src");
    }

    public int getQuantity(){
        return Integer.parseUnsignedInt(this.quantity.getText());
    }

    public String getProductFullName(){
        return this.productName.getAttribute("title");
    }

    public List<String> getProductAttributes(){
        List<String> attributeList = new ArrayList<>();
        String txt = this.attributes.getText();
        Collections.addAll(attributeList, txt.split(", "));
        return attributeList;
    }

    public double getProductTotalPrice(){
        return Double.parseDouble(this.total.getText());
    }

    public void removeItem(){
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 3);
        closeButton.click();
        wait.until(ExpectedConditions.stalenessOf(this.miniItem));
    }


}
