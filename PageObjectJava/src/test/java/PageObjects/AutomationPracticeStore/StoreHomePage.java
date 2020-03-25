package PageObjects.AutomationPracticeStore;

import PageObjects.AutomationPracticeStore.Objects.StoreItemThumbnail;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.util.ArrayList;
import java.util.List;

public class StoreHomePage extends StorePage {

    @FindBy(how = How.ID, using = "home-page-tabs")
    private WebElement homePageTabsSection;

    private By activePaneItemsHolder = By.cssSelector("div.tab-content ul.product_list.active");

    public List<StoreItemThumbnail> getPopularItems(){
        this.selectTab("POPULAR");
        return getActiveItems();
    }

    public List<StoreItemThumbnail> getBestSellerItems(){
        this.selectTab("BEST SELLERS");
        return getActiveItems();
    }

    public List<StoreItemThumbnail> getActiveItems(){
        List<StoreItemThumbnail> itemList = new ArrayList<>();
        WebElement section = null;
        try{
            section = _driver.findElement(activePaneItemsHolder);
        }catch (NoSuchElementException ex){
            throw new NoSuchElementException("The active section holder was not found", ex.fillInStackTrace());
        }
        section.findElements(By.tagName("li")).forEach(we ->{
            ElementLocatorFactory factory = new DefaultElementLocatorFactory(we);
            StoreItemThumbnail item = new StoreItemThumbnail();
            PageFactory.initElements(factory, item);
            itemList.add(item);
        });
        return itemList;
    }

    public List<String> getTabsAvailable(){
        List<String> available = new ArrayList<>();
        this.sectionsAvailable().forEach(tab -> { available.add(tab.getText()); });
        return available;
    }

    public StoreHomePage selectTab(String tabName){
        WebElement found = this.sectionsAvailable().stream().filter(tab -> tab.getText().equalsIgnoreCase(tabName))
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException(String.format("The section tab named '%s' was not found", tabName)));
        found.click();
        return this;
    }

    private List<WebElement> sectionsAvailable(){
        return homePageTabsSection.findElements(By.tagName("a"));
    }


}
