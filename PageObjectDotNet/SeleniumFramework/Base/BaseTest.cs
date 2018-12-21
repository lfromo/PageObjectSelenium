using NUnit.Framework;
using NUnit.Framework.Interfaces;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using System;
using System.Configuration;

namespace SeleniumFramework.Base
{
    [TestFixtureSource(typeof(TestConfiguration), "DriverCfg")]
    public abstract class BaseTest
    {
        protected readonly IWebDriver Driver;
        private readonly string browserName;
        private readonly string _baseUrl;

        public BaseTest(IWebDriver driver, string url)
        {
            Driver = driver;
            _baseUrl = url;
            browserName = (Driver as RemoteWebDriver).Capabilities.GetCapability("browserName").ToString().ToUpperInvariant();
        }

        [OneTimeSetUp]
        public void setupAll()
        {
            Driver.Navigate().GoToUrl(_baseUrl);
            Support.Logger.Info($"=====>EXECUTING TEST SUITE FOR {browserName}. URL: {_baseUrl}<=====");
        }


        [OneTimeTearDown]
        public void tearAllDown()
        {
            TestConfiguration.RemoveDriverInstance(Driver);
            Support.Logger.Info($"=====>TEST SUITE FINISHED FOR {browserName}. URL: {_baseUrl}<=====");
        }

        [SetUp]
        public void setup()
        {
            var testName = TestContext.CurrentContext.Test.Name;
            Support.Logger.Info($"--->EXECUTING TEST - TestCase:{testName}<---");
        }

        [TearDown]
        public void teardown()
        {

            var testName = TestContext.CurrentContext.Test.Name;
            var fullName = string.Concat(browserName, "_", testName);
            var _status = TestContext.CurrentContext.Result.Outcome.Status;

            switch (_status)
            {
                case TestStatus.Passed:
                    Support.Logger.Info($"--->TEST PASSED - TestCase:{testName}<---");
                    break;

                case TestStatus.Failed:
                    var path = ConfigurationManager.AppSettings["ScreenShotPath"];
                    var screenshotPath = Support.TakeScreenShoot(Driver, fullName, path);
                    TestContext.AddTestAttachment(screenshotPath);
                    var msg = $"Output: {TestContext.CurrentContext.Result.Message} {Environment.NewLine} { TestContext.CurrentContext.Result.StackTrace}";
                    Support.Logger.Error($"--->TEST FAILED - TestCase:{testName}<---{Environment.NewLine}{msg}");
            
                    Driver.Navigate().GoToUrl(_baseUrl); //For the next Test Case to have a good & clean starting point. Useful after any fatal failure.
                    break;

                case TestStatus.Skipped:
                    Support.Logger.Info($"--->TEST SKIPPED - TestCase:{testName}<---");
                    break;

                default:
                    Support.Logger.InfoFormat("--->Test Finished - Browser:{0}, TestCase:{1}, Reason:{2}<---",
                        browserName, testName, Enum.GetName(typeof(TestStatus), _status));
                    break;
            }

        }

    }

}
