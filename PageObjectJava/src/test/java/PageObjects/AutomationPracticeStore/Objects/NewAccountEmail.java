package PageObjects.AutomationPracticeStore.Objects;

import Base.TestConfiguration;
import PageObjects.AutomationPracticeStore.ErrorMessageException;
import PageObjects.AutomationPracticeStore.NewAccountForm;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class NewAccountEmail {

    @FindBy(how = How.ID_OR_NAME, using = "email_create")
    private WebElement inputCreateAccountEmailAddress;

    @FindBy(how = How.ID_OR_NAME, using = "SubmitCreate")
    private WebElement buttonCreateAccount;


    public NewAccountEmail setEmailAddress(String emailAddress){
        inputCreateAccountEmailAddress.clear();
        inputCreateAccountEmailAddress.sendKeys(emailAddress);
        return this;
    }

    public String getEmailAddress(){
        return inputCreateAccountEmailAddress.getAttribute("value");
    }

    public NewAccountForm pressCreateAccountButton(){
        WebDriverWait wait = new WebDriverWait(TestConfiguration.getDriver(), 2);
        buttonCreateAccount.click();
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create_account_error")));
        }catch (TimeoutException ex){
            return new NewAccountForm();
        }
        List<String> errorsFound = new ArrayList<>();
        for(WebElement errorWe : TestConfiguration.getDriver().findElements(By.cssSelector("div#create_account_error li")))
            errorsFound.add(errorWe.getText());
        throw new ErrorMessageException(errorsFound);
    }

}
