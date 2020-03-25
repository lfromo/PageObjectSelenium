package Support;

import Base.TestConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.util.*;

public class Support {

    public static final Logger LOGGER = LogManager.getLogger("selenium");
    private static final List<Screenshot> SAVED_SCREENSHOTS = new ArrayList<>();

    public static void javaScriptClick(WebElement element){
        getJavaScriptExecutor().executeScript("arguments[0].click();", element);
    }

    public static String extractTextWithJavaScript(WebElement element){
        return getJavaScriptExecutor()
                .executeScript("return arguments[0].textContent;", element)
                .toString();
    }

    public static JavascriptExecutor getJavaScriptExecutor(){
        return (JavascriptExecutor) TestConfiguration.getDriver();
    }

    public static void takeScreenshot(){
        takeScreenshot(null);
    }

    public static void takeScreenshot(WebElement webItem){
        AShot shotInstance = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000));

        Screenshot shot = Objects.isNull(webItem) ? shotInstance.takeScreenshot(TestConfiguration.getDriver()) :
                shotInstance.takeScreenshot(TestConfiguration.getDriver(), webItem);
        SAVED_SCREENSHOTS.add(shot);
    }

    public static void HoverOverWebElementsThenClickOver(WebElement finalWebItem, WebElement... orderedWebItems)
    {
        Actions act = new Actions(TestConfiguration.getDriver());

        for (WebElement itm : orderedWebItems)
            act = act.moveToElement(itm);

        act.click(finalWebItem).build().perform();
    }

    public static void hoverOverWebElement(WebElement element){
        scrollToWebElement(element);
        Actions act = new Actions(TestConfiguration.getDriver());
        act.moveToElement(element).build().perform();
    }

    public static void scrollToWebElement(WebElement element)
    {
        JavascriptExecutor executor = getJavaScriptExecutor();
        executor.executeScript("arguments[0].scrollIntoView();", element); //Necessary for Firefox
    }

    public static List<Screenshot> getStoredScreenshots(){
        return SAVED_SCREENSHOTS;
    }

    public static void clearScreenshotsList(){
        SAVED_SCREENSHOTS.clear();
    }

}
