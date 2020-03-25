using OpenQA.Selenium;
using System;
using System.Collections.Generic;
using System.Linq;

namespace AutomationPractice.PageObjects.AutomationPracticeStore.Objects
{
    [Serializable]
    internal class ErrorMessageException : WebDriverException
    {
        public IList<string> ErrorList { get; }

        public ErrorMessageException(string message) : base(message) { }

        public ErrorMessageException(IList<string> errorList) : base(MessageBuilder(errorList)) { ErrorList = errorList; }

        public ErrorMessageException(string message, Exception innerException) : base(message, innerException) { }


        private static string MessageBuilder(IList<string> errors)
        {
            string errorMsg = string.Empty;
            errors.ToList().ForEach(error =>
            {
                errorMsg = string.Concat(errorMsg, error + "\n");
            });
            return errorMsg;
        }

       
    }
}