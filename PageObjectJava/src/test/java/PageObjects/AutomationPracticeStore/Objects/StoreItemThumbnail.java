package PageObjects.AutomationPracticeStore.Objects;

import Base.TestConfiguration;
import PageObjects.AutomationPracticeStore.ErrorMessageException;
import PageObjects.AutomationPracticeStore.ProductPage;
import Support.Support;
import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoreItemThumbnail {

    //Regular web elements found in the items across all screens

    @FindBy(how = How.CSS, using = "a.product_img_link img")
    private WebElement itemImage;

    @FindBy(how = How.TAG_NAME, using = "h5")
    private WebElement itemName;

    @FindBy(how = How.CSS, using = "div.right-block .price")
    private WebElement itemPrice;

    @FindBy(how = How.CSS, using = "div.right-block .price-percent-reduction")
    private List<WebElement> itemDiscount;

    @FindBy(how = How.CSS, using = "div.right-block .old-price")
    private List<WebElement> itemPriceBeforeDiscount;

    /* The following web elements may already exist in the DOM but are not clickable.
       Should be accessed only after hovering the mouse over the item itself. */

    @FindBy(how = How.CSS, using = "div.left-block .price")
    private WebElement itemContextPrice;

    @FindBy(how = How.CSS, using = "div.left-block .price-percent-reduction")
    private List<WebElement> itemContextDiscount;

    @FindBy(how = How.CSS, using = "div.left-block .old-price")
    private List<WebElement> itemContextPriceBeforeDiscount;

    @FindBy(how = How.CSS, using = "div.right-block a.ajax_add_to_cart_button")
    private WebElement itemContextAddToCartButton;

    @FindBy(how = How.CSS, using = "div.right-block a.lnk_view")
    private WebElement itemContextMoreButton;

    @FindBy(how = How.CLASS_NAME, using = "quick-view")
    private WebElement itemQuickViewButton;


    /* TODO: Create the proper exception in the methods handling all the following locators
       Web Elements with extra functionality that only appear outside of the Home screen */

    @FindBy(how = How.CLASS_NAME, using = "color-pick")
    private List<WebElement> itemExtraColorPicking;

    @FindBy(how = How.CLASS_NAME, using = "availability")
    private List<WebElement> itemStockStatus;

    @FindBy(how = How.CLASS_NAME, using = "addToWishlist")
    private List<WebElement> itemAddToWishListTab;    //Needs to be hover over the item first

    @FindBy(how = How.CLASS_NAME, using = "add_to_compare")
    private WebElement itemAddToCompareTab;     //Needs to be hover over the item first

    private final static String IN_STOCK_MESSAGE = "In stock";
    private final static String ADDED_TO_WISH_LIST = "Added to your wishlist.";

    private final static By ERROR_MESSAGE_BOX_LOCATOR = By.className("fancybox-error");

    public String getName(){
        return this.itemName.getText();
    }

    public double getPrice(){
        return Double.parseDouble(this.itemPrice.getText().substring(1));
    }

    public String getImageUrl(){
        return this.itemImage.getAttribute("src");
    }

    public int getDiscountPercentage(){
        if (this.itemDiscount.isEmpty())
            return 0;
        String txt = this.itemDiscount.get(0).getText().split("%")[0].substring(1);
        return Integer.parseInt(txt);
    }

    public double getPriceBeforeDiscount(){
        if(this.itemPriceBeforeDiscount.isEmpty())
            return 0;
        return Double.parseDouble(this.itemPriceBeforeDiscount.get(0).getText().substring(1));
    }

    public double getContextPrice(){
        Support.hoverOverWebElement(this.itemImage);
        return Double.parseDouble(this.itemContextPrice.getText().substring(1));
    }

    public int getContextDiscountPercentage(){
        Support.hoverOverWebElement(this.itemImage);
        if(this.itemContextDiscount.isEmpty())
            return 0;
        return Integer.parseInt(this.itemContextDiscount.get(0).getText().split("%")[0].substring(1));
    }

    public double getContextPriceBeforeDiscount(){
        Support.hoverOverWebElement(this.itemImage);
        if(this.itemContextPriceBeforeDiscount.isEmpty())
            return 0;
        return Double.parseDouble(this.itemContextPriceBeforeDiscount.get(0).getText().substring(1));
    }

    public ProductInCartPopUp addToCart(){
        Support.hoverOverWebElement(this.itemImage);
        this.itemContextAddToCartButton.click();
        return new ProductInCartPopUp();
    }

    public ProductPage moreInformation(){
        Support.hoverOverWebElement(this.itemImage);
        this.itemContextMoreButton.click();
        return new ProductPage();
    }

    public QuickView getQuickView(){
        Support.hoverOverWebElement(this.itemImage);
        this.itemQuickViewButton.click();
        return new QuickView();
    }

    // The following methods should be used only if the item is being handled by a CatalogPage instance

    public boolean isInStock(){
            if(this.itemStockStatus.isEmpty())
                throw new NoSuchElementException("The label for stock availability was not found. Make sure this object " +
                        "is being displayed in the catalog page");
        return this.itemStockStatus.get(0).getText().trim().equals(IN_STOCK_MESSAGE);
    }

    public List<String> getAvailableColors(){
        List<String> availableColors = new ArrayList<>();
        String colorHexCode = null;
        for(WebElement colorWe : itemExtraColorPicking){
            try{
                colorHexCode = colorWe.getAttribute("style").split("#")[1];
            }catch(IndexOutOfBoundsException ex){
                Support.LOGGER.error(String.format("The item named '%s' has one color missing", this.getName()));
            }
            if(!Strings.isNullOrEmpty(colorHexCode))
                availableColors.add(colorHexCode);
        }
        return availableColors;
    }

    public boolean isProductInColor(String colorHexCode){
        return this.getAvailableColors().stream().anyMatch(color -> color.equalsIgnoreCase(colorHexCode));
    }

    public ProductPage selectColor(String colorHexCode){
        if(this.getAvailableColors().isEmpty())
            throw new NoSuchElementException(String.format("The item '%s' has not colors to display. " +
                    "Make sure this object is being displayed in the Catalog page or the color section exists",
                    this.getName()));
        WebElement found = this.itemExtraColorPicking.stream().filter(colorWe -> colorWe.getAttribute("style").equalsIgnoreCase(colorHexCode))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("The item named '%s' is not available in color '%s'", this.getName(), colorHexCode)));
        found.click();
        return new ProductPage();
    }

    public StoreItemThumbnail addToWishList(){
        Support.hoverOverWebElement(this.itemImage);
        if(this.itemAddToWishListTab.isEmpty()){
            String errorText = String.format("The tab for adding to the wish list for item '%s' was not found. " +
                    "Make sure this object is being displayed in the Catalog page", this.getName());
            throw new NoSuchElementException(errorText);
        }

        this.itemAddToWishListTab.get(0).click();

        List<WebElement> msg = TestConfiguration.getDriver().findElements(ERROR_MESSAGE_BOX_LOCATOR);
        if(msg.isEmpty())
            throw new NoSuchElementException("The expected pop up message did not appear");
        if(!msg.get(0).getText().equals(ADDED_TO_WISH_LIST))
            throw new ErrorMessageException(msg.get(0).getText());
        msg.get(0).findElement(By.xpath("../../../a[@title='Close']")).click(); //For closing the popup message that should always appear
        return this;
    }

    public boolean isAddedToWishList(){
        Support.hoverOverWebElement(this.itemImage);
        if(this.itemAddToWishListTab.isEmpty()){
            String errorText = String.format("The tab for adding to the wish list for item '%s' was not found. " +
                    "Make sure this object is being displayed in the Catalog page", this.getName());
            throw new NoSuchElementException(errorText);
        }
        String attr = this.itemAddToWishListTab.get(0).getAttribute("class");
        return attr.endsWith("checked");
    }

    public StoreItemThumbnail addToCompare(boolean addToCompare){
        Support.hoverOverWebElement(this.itemImage);

        if(addToCompare && !this.isAddedToCompare())
            this.itemAddToCompareTab.click();
        if(!addToCompare && this.isAddedToCompare())
            this.itemAddToCompareTab.click();
        return this;
    }

    public boolean isAddedToCompare(){
        Support.hoverOverWebElement(this.itemImage);
        if(Objects.isNull(this.itemAddToCompareTab)){
            String errorText = String.format("The tab for adding for comparison for item '%s' was not found. " +
                    "Make sure this object is being displayed in the Catalog page", this.getName());
            throw new NoSuchElementException(errorText);
        }
        String classAttribute = this.itemAddToCompareTab.getAttribute("class");
        return classAttribute.endsWith("checked");
    }
}
