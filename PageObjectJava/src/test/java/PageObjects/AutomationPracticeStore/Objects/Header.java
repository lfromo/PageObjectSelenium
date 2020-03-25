package PageObjects.AutomationPracticeStore.Objects;

import Base.BasePageObject;

import PageObjects.AutomationPracticeStore.AccountPage;
import PageObjects.AutomationPracticeStore.AuthenticationPage;
import PageObjects.AutomationPracticeStore.CartPage;
import PageObjects.AutomationPracticeStore.NotLoggedInException;
import Support.Support;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;

public class Header extends BasePageObject {

    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li/a")
    private List<WebElement> categoriesMenu;

    @FindBy(how = How.CSS, using = "div.shopping_cart a")
    private WebElement shoppingCart;

    @FindBy(how = How.ID, using = "search_query_top")
    private WebElement inputSearchBox;

    private final By loginButton = By.className("login");
    private final By accountButton = By.className("account");
    private final By signOutButton = By.className("logout");


    public void search(String searchTerm){
        inputSearchBox.sendKeys(searchTerm);
        inputSearchBox.submit();
        //TODO Return the search box page as soon is ready
    }

    public List<String> getCategoriesAvailable(){
        ArrayList categories = new ArrayList<String>();
        for (WebElement we : categoriesMenu)
            categories.add(we.getText());
        return categories;
    }

    public void goToCategory(String categoryName){
        WebElement found = categoriesMenu.stream().filter(category -> category.getText().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("The category with name '%s' was not found", categoryName)));
        found.click();
        //TODO Return the proper page implementation as soon is ready
    }

    public void getCategorySubmenu(String categoryName, Class submenuType) {
        //TODO Do something when you find out what to do
    }

    public CartPage goToCart(){
        shoppingCart.click();
        return new CartPage();
    }

    public MiniCart getContextualCart(){
        Support.hoverOverWebElement(shoppingCart);
        return new MiniCart();
    }

    public boolean isUserLoggedIn(){
        return _driver.findElements(loginButton).isEmpty()
                && _driver.findElements(accountButton).size() == 1
                && _driver.findElements(signOutButton).size() == 1;
    }

    public void signOut(){
        List<WebElement> foundElements = _driver.findElements(signOutButton);
        if (foundElements.isEmpty())
            throw new NotLoggedInException("The sign out button was not found. No user seems to be logged in the system");
        foundElements.get(0).click();
    }

    public AuthenticationPage signIn(){
        List<WebElement> foundElements = _driver.findElements(loginButton);
        if(foundElements.isEmpty())
            throw new NoSuchElementException("The button for signing in was not found. A user seems to be already logged in the system");
        foundElements.get(0).click();
        return new AuthenticationPage();
    }

    public String loggedUserName(){
        List<WebElement> foundElements = _driver.findElements(accountButton);
        if (foundElements.isEmpty())
           throw new NotLoggedInException("No user seems to be logged into the system");
        return foundElements.get(0).getText().trim();
    }

    public AccountPage goToAccountPage(){
        if(!this.isUserLoggedIn())
            throw new NotLoggedInException("No user seems to be logged into the system");
        _driver.findElement(accountButton).click();
        return new AccountPage();
    }

}
