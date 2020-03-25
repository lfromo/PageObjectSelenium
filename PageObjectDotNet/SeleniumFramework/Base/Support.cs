using OpenQA.Selenium;
using OpenQA.Selenium.Interactions;
using System;
using System.IO;
using System.Reflection;

namespace SeleniumFramework.Base
{
    public static class Support
    {
        public static readonly log4net.ILog Logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /// <summary>
        /// This method takes a screenshot, and returns the fullpath of the file saved
        /// </summary>
        /// <param name="_driver"></param>
        /// <param name="SectionName"></param>
        /// <returns>The taken screenshot path</returns>
        public static string TakeScreenShoot(this IWebDriver _driver, string SectionName, string sreenShotPath)
        {
            var baseScreenShotLocation = sreenShotPath;
            if (!Directory.Exists(baseScreenShotLocation))
                Directory.CreateDirectory(baseScreenShotLocation);

            var SSName = Path.Combine(baseScreenShotLocation, SectionName + "_" + DateTime.Now.ToFileTimeUtc() + ".png");
            Screenshot ss = ((ITakesScreenshot)_driver).GetScreenshot();
            ss.SaveAsFile(SSName, ScreenshotImageFormat.Png);
            return SSName;
        }

        /// <summary>
        /// This method returns a <see cref="IJavaScriptExecutor"/> to support Javascript code execution
        /// </summary>
        /// <param name="driver"></param>
        /// <returns></returns>
        public static IJavaScriptExecutor Scripts(this IWebDriver _driver)
        {
            var js = _driver as IJavaScriptExecutor;
            return js;
        }

        /// <summary>
        /// This method highlights an element on the Site using a given locator
        /// </summary>
        /// <param name="driver"></param>
        /// <param name="locator"></param>
        public static void HighlightElement(this IWebDriver driver, By locator)
        {
            HighlightElement(driver, driver.FindElement(locator));
        }


        /// <summary>
        /// This method highlights an element on the Site using the given webelement
        /// </summary>
        /// <param name="driver"></param>
        /// <param name="element"></param>
        public static void HighlightElement(this IWebDriver driver, IWebElement element)
        {
            var js = Scripts(driver);

            string highlightJavascript = @"arguments[0].style.cssText = ""border-width: 3px; border-style: solid; border-color: red""; ";
            js.ExecuteScript(highlightJavascript, element);
            highlightJavascript = @"arguments[0].style.cssText = ""border-width: 0px"";";
            js.ExecuteScript(highlightJavascript, element);
        }

        public static void JavaScriptClick(this IWebDriver driver, IWebElement elementToClick)
        {
            var js = Scripts(driver);
            js.ExecuteScript(@"arguments[0].click();", elementToClick);
        }

        public static string GetTextUsingJavaScript(this IWebDriver driver, IWebElement elementToExtractText)
        {
            var js = Scripts(driver);
            var obj = js.ExecuteScript(@"return arguments[0].textContent;", elementToExtractText);
            return obj.ToString().Trim();
        }

        public static void HoverOverWebElement(this IWebDriver driver, IWebElement element) 
        {
            ScrollToWebElement(driver, element);
            var act = new Actions(driver);
            act.MoveToElement(element).Build().Perform();
        }

        public static void ScrollToWebElement(this IWebDriver driver, IWebElement element) 
        {
            var js = Scripts(driver);
            js.ExecuteScript("arguments[0].scrollIntoView();", element);
        }


    }
}
