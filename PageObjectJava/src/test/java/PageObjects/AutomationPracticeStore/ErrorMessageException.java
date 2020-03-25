package PageObjects.AutomationPracticeStore;

import org.openqa.selenium.WebDriverException;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessageException extends WebDriverException {

    private final List<String> errors;

    public ErrorMessageException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public ErrorMessageException(List<String> errorsList){
        super(messageBuilder(errorsList));
        this.errors = errorsList;
    }

    public List<String> getErrorsList(){
        return this.errors;
    }

    private static String messageBuilder(List<String> errors){
        StringBuilder builder = new StringBuilder();
        for (String error : errors)
            builder.append(error + "\n");
        return builder.toString();
    }
}
