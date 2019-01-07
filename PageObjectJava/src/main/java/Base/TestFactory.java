package Base;

import org.testng.annotations.Factory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Arrays;
import java.util.List;


public class TestFactory {

    private Class[] testClassesToExecute;




    //@Factory(dataProvider = "setEnvironmentDefault", dataProviderClass = TestConfiguration.class)
    public TestFactory(String browserName, String url){
        List<Class> classes = Arrays.asList(testClassesToExecute);
        XmlSuite suite = new XmlSuite();
        suite.getParameters();
        List<XmlTest> tests = suite.getTests();
        tests.stream().forEach(test -> {
            List<XmlClass> testClasses = test.getClasses();
            testClasses.stream().forEach(clz -> {
                if(!clz.getName().equals("TestFactory"))
                    classes.add(clz.getSupportClass());
            });
        });

    }

    private void BrowserConfigurationFactory(String driver, String url){
        BrowserConfiguration _environment = new BrowserConfiguration(driver, url);
        TestConfiguration.setDriver(_environment.getDriver());
        TestConfiguration.setUrl(url);

        Arrays.stream(testClassesToExecute).forEach(clz -> {

            try {
                clz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }
}
