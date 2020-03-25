package PageObjects.AutomationPracticeStore.Objects;

import Base.TestConfiguration;
import PageObjects.AutomationPracticeStore.NotLoggedInException;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.function.Predicate;

public class QuickView  {

    @FindBy(id = "bigpic") WebElement currentPicture;
    @FindBy(css = "ul#thumbs_list_frame li a") List<WebElement> pictures;
    @FindBy(name = "resetImages") WebElement displayAllPictures;

    @FindBy(tagName = "h1") WebElement productName;
    @FindBy(css = "p#product_reference span.editable") WebElement productModel;
    @FindBy(css = "p#product_condition span.editable") WebElement productCondition;
    @FindBy(id = "short_description_content") WebElement productDescription;
    @FindBy(id = "availability_date_value") WebElement productAvailability; //Available in the page but found in no product
    @FindBy(id = "oosHook") WebElement productOutOfStock; //Not found something where this implementation is invoked
    @FindBy(className = "socialsharing_product") WebElement socialButtonsHolder;

    @FindBy(how = How.ID, using = "our_price_display") WebElement labelPrice;
    @FindBy(how = How.ID, using = "reduction_percent_display") WebElement labelDiscount;
    @FindBy(how = How.ID, using = "old_price_display") WebElement labelOldPrice;
    @FindBy(how = How.ID, using = "quantity_wanted") WebElement inputQuantity;
    @FindBy(how = How.CLASS_NAME, using = "product_quantity_down") WebElement buttonQtySubtract;
    @FindBy(how = How.CLASS_NAME, using = "product_quantity_up") WebElement buttonQtyAdd;
    @FindBy(how = How.ID, using = "group_1") WebElement selectListSize;
    @FindBy(how = How.ID, using = "color_to_pick_list") WebElement listColor;
    @FindBy(how = How.NAME, using = "Submit") WebElement buttonAddToCart;
    @FindBy(how = How.ID, using = "wishlist_button") WebElement buttonAddToWishlist;

    private final By popupMessage = By.className("fancybox-skin");
    private final By frameContainer = By.cssSelector("iframe.fancybox-iframe");
    private By buttonCloseQuickView = By.className("fancybox-close");

    private final WebElement container;

    private final String ADD_TO_WISH_LIST_ERROR_NOT_LOGGED_IN = "You must be logged in to manage your wishlist.";

    public QuickView(){
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 8);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div#fancybox-loading")));
        wait.until(ExpectedConditions.presenceOfElementLocated(frameContainer));
        WebElement frame = TestConfiguration.getDriver().findElement(frameContainer);
        TestConfiguration.getDriver().switchTo().frame(frame);
        this.container = TestConfiguration.getDriver().findElement(By.id("product"));
        ElementLocatorFactory factory = new DefaultElementLocatorFactory(this.container);
        PageFactory.initElements(factory, this);
    }

    public String getCurrentPictureSource(){
        return this.currentPicture.getAttribute("src");
    }

    public List<String> getThumbnailPicturesSourceList(){
        List<String> thumbnailLinks = new ArrayList<>();
        this.pictures.stream().filter(WebElement::isDisplayed).forEach(pic -> thumbnailLinks.add(pic.getAttribute("href")));
        return thumbnailLinks;
    }

    public String getCurrentThumbnailPicture(){
        WebElement selectedThumbnail = this.pictures.stream().filter(pic -> pic.getAttribute("class").equalsIgnoreCase("fancybox shown"))
                .findFirst().orElseThrow(() -> new NoSuchElementException("There was a problem finding the currently selected thumbnail"));
        return selectedThumbnail.findElement(By.tagName("img")).getAttribute("src");
    }

    public QuickView displayAllThumbnailPictures(){
        try{
            displayAllPictures.click();
            return this;
        }catch(NoSuchElementException ex){
            throw new NoSuchElementException("All available thumbnails may be already displayed", ex.fillInStackTrace());
        }
    }

    public boolean areAllThumbnailPicturesDisplayed(){
        try{
            return !displayAllPictures.isDisplayed();
        }catch(NoSuchElementException ex){
            return true;
        }
    }

    public String getProductName(){
        return productName.getText();
    }

    public String getProductModel(){
        return productModel.getText();
    }

    public String getProductCondition(){
        return productCondition.getText();
    }

    public String getProductDescription(){
        return productDescription.getText();
    }

    public double getProductPrice(){
        return Double.parseDouble(labelPrice.getText().substring(1));
    }

    public int getProductDiscount(){
        if (Objects.isNull(labelDiscount) || labelDiscount.getText().isEmpty())
            return 0;
        return Integer.parseInt(labelDiscount.getText().split("%")[0].substring(1));
    }

    public double getPriceBeforeDiscount(){
        if (Objects.isNull(labelOldPrice) || labelOldPrice.getText().isEmpty())
            return 0.0;
        return Double.parseDouble(labelOldPrice.getText().substring(1));
    }

    public int getCurrentQuantity(){
        return Integer.parseUnsignedInt(inputQuantity.getAttribute("value"));
    }

    public QuickView setQuantity(String quantity){
        inputQuantity.clear();
        inputQuantity.sendKeys(quantity);
        return this;
    }

    public List<String> getAvailableSizes(){
        List<String> sizes = new ArrayList<>();
        List<WebElement> options = new Select(this.selectListSize).getOptions();
        options.forEach(itm -> sizes.add(itm.getText()));
        return sizes;
    }

    public QuickView setSize(String size){
        Select options = new Select(this.selectListSize);
        options.selectByValue(size);
        return this;
    }

    public int incrementQuantity(){
        buttonQtyAdd.click();
        return this.getCurrentQuantity();
    }

    public int decrementQuantity(){
        buttonQtySubtract.click();
        return this.getCurrentQuantity();
    }

    public Map<String,String> getColorsAvailable(){
        Map<String,String> colors_map = new HashMap<>();
        this.getWebElementColorsAvailable().forEach(color -> {
            colors_map.put(color.getAttribute("name"), color.getAttribute("style").split(": ")[1]);
        });
        return colors_map;
    }

    public QuickView selectColorByName(String color){
        this.selectColor(color, true);
        return this;
    }

    public QuickView selectColorByCode(String color){
        this.selectColor(color, false);
        return this;
    }

    public ProductInCartPopUp addToCart(){
        this.buttonAddToCart.click();
        TestConfiguration.getDriver().switchTo().defaultContent();
        return new ProductInCartPopUp();
    }

    public QuickView addToWishList(){
        this.buttonAddToWishlist.click();
        try{
            String text = this.getPopupMessage();
            if(text.equals(ADD_TO_WISH_LIST_ERROR_NOT_LOGGED_IN))
                throw new NotLoggedInException(text);
        }catch(TimeoutException ex){
            throw new NoSuchElementException("The pop up message after adding the item to the wish list did not appear", ex.fillInStackTrace());
        }
        this.closePopupMessage();
        return this;
    }

    public String getPopupMessage() throws TimeoutException{
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 4);
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupMessage));
        return TestConfiguration.getDriver().findElement(popupMessage).getText();
    }

    public QuickView closePopupMessage(){
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 4);
        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.fancybox-skin a.fancybox-close")));
        closeButton.click();
        return this;
    }

    public void closeQuickView(){
        TestConfiguration.getDriver().switchTo().defaultContent();
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 3);
        WebElement button = TestConfiguration.getDriver().findElement(buttonCloseQuickView);
        button.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fancybox-overlay")));
    }


    private List<WebElement> getWebElementColorsAvailable(){
        return listColor.findElements(By.tagName("a"));
    }

    private void selectColor(String color, boolean byName){
        if (this.getWebElementColorsAvailable().size() == 0)
            throw new NoSuchElementException("This QuickView window does not contain any color to choose from");

        Predicate<WebElement> findByName = itm -> itm.getAttribute("name").equalsIgnoreCase(color);
        Predicate<WebElement> findByCode = itm -> this.containsIgnoreCase(itm.getAttribute("style"), color);

        WebElement found;
        if(byName)
            found = this.getWebElementColorsAvailable().stream().filter(findByName).findFirst().orElse(null);
        else
            found = this.getWebElementColorsAvailable().stream().filter(findByCode).findFirst().orElse(null);

        if(Objects.isNull(found))
            throw new NoSuchElementException(String.format("The color '%s' was not found among the available colors for product %s", color, this.getProductName()));
        else
            found.click();
    }

    private boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}
