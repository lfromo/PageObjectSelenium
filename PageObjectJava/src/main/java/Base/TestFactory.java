package Base;

import org.testng.annotations.Factory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Arrays;
import java.util.List;


public class TestFactory {

    private Class[] testClassesToExecute;

    @Factory(dataProvider = "setEnvironmentWithXMLTestContext", dataProviderClass = TestConfiguration.class)
    public TestFactory(String browserName, String url){
        XmlSuite suite = new XmlSuite();
        suite.getParameters();
        List<XmlTest> tests = suite.getTests();
        tests.stream().forEach(test -> {
            List<XmlClass> testClasses = test.getClasses();
        });
    }


}
