package org.com.utilities.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.com.BaseFramework;
import org.com.webUI.constants.Constants;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class EnvironmentHelper extends BaseFramework {

    static Properties properties;
    private static final Logger logger = LogManager.getLogger(EnvironmentHelper.class);

    public static Properties loadProperties(){

        String rootPath = Paths.get(userDir, Constants.CONFIG_PROPERTIES_PATH).toString();
        properties = new Properties();
        try{
            properties.load(new FileInputStream(rootPath));
        } catch (Exception e) {
            logger.error("Error while getting properties" + e.getMessage());
        }

        return properties;
    }

    public static String getPropertyValue(String propertyName){
        properties = loadProperties();

       return properties.getProperty(propertyName);
    }
}
