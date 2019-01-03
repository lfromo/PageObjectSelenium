package Base;

import org.testng.TestNG;
import org.testng.annotations.Factory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.Arrays;


public class TestFactory {

    private final Class[] testClassesToExecute;

    public TestFactory(Class... testClasses){
        this.testClassesToExecute = testClasses;
        TestNG ng = new TestNG();
        ng.setTestClasses(testClassesToExecute);
        XmlSuite supersuite = new XmlSuite();
        XmlTest test = supersuite.getTests().get(0);
        XmlClass clz = test.getClasses().get(0);


    }


    @Factory(dataProvider = "setEnvironmentDefault", dataProviderClass = TestConfiguration.class)
    public void environmentFactory(String driver, String url){
        EnvironmentConfiguration _environment = new EnvironmentConfiguration(driver, url);
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
