package PageObjects.AutomationPracticeStore;

import org.openqa.selenium.WebDriverException;

public class NotLoggedInException extends WebDriverException {

    public NotLoggedInException(String message){
        super(message);
    }

    public NotLoggedInException(String message, Throwable throwable){
        super(message, throwable);
    }

}
