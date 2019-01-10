package Support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class PropertiesHandler {



    public Properties getProperties(String... propertiesFilePath){
        Properties properties = new Properties();
        Arrays.stream(propertiesFilePath).forEach(propertyFile -> {
            InputStream input = getClass().getClassLoader().getResourceAsStream(propertyFile);
            try {
                if(!Objects.isNull(input)) {
                    properties.load(input);
                }
                else
                    throw new FileNotFoundException(String.format("Property file '%s' was not found", propertyFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return properties;
    }



}
