using NUnit.Framework;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Edge;
using OpenQA.Selenium.IE;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;

namespace SeleniumFramework.Base
{
    public class TestConfiguration
    {
        private static IWebDriver _driver;
        private readonly static IList<IWebDriver> _drvInstances = new List<IWebDriver>();

        public static IEnumerable DriverCfg
        {
            get{
                foreach (var drvName in Enum.GetNames(typeof(Browser)))
                {
                    var _driver = ConfigureDriver(drvName);
                    if (_driver != null)
                        yield return new TestFixtureData(_driver, GetBaseUrl());
                }
            }
        }


        public static IWebDriver GetDriverInstance()
        {
            return _drvInstances.Last();
        }

        public static bool RemoveDriverInstance(IWebDriver driver)
        {
            if(!_drvInstances.Contains(driver))
                return false;
            _drvInstances.Remove(driver);
            driver.Quit();
            return true;
        }


        private static IWebDriver ConfigureDriver(string _browser)
        {
            try
            {
                switch (_browser)
                {
                    case "CHROME":
                        _driver = new ChromeDriver();
                        break;
                    case "IE11":
                        //Please make sure the IE11 browser itself is properly configured by editing the system Internet Options
                        _driver = new InternetExplorerDriver();
                        break;
                    case "EDGE":
                        //Please make sure to have followed this instructions:
                        //https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/#downloads
                        EdgeOptions _edgeOpts = new EdgeOptions
                        {
                            PageLoadStrategy = PageLoadStrategy.Normal,
                            
                        };
                        _driver = new EdgeDriver(_edgeOpts);
                        break;
                    default: throw new WebDriverException($"{_browser} is not supported.");
                }
                _driver.Manage().Window.Maximize();
                _drvInstances.Add(_driver);
                return GetDriverInstance();
            } catch (Exception ex)
            {
                Support.Logger.Fatal($"There was a problem loading a WebDriver for {_browser}." +
                    $"Output: {Environment.NewLine} {ex.Message} {Environment.NewLine} {ex.StackTrace}");
                return null;
            }
            
        }

        private static string GetBaseUrl()
        {
            return ConfigurationManager.AppSettings["AppURL"];
        }
    }
}
