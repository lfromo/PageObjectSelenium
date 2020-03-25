using OpenQA.Selenium;
using System;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    internal class NotLoggedInException : WebDriverException
    {

        public NotLoggedInException(string message) : base(message) { }

        public NotLoggedInException(string message, Exception innerException) : base(message, innerException) { }

    }
}