package nl.michaelvanolst.app.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public final class Config {
  private static Config INSTANCE;
  private Properties properties;
  
  private Config() {
    String configPropertyFile = StringUtils.stripEnd(System.getProperty("user.dir"), "/") + "/config.properties";

    try (InputStream input = new FileInputStream(configPropertyFile)) {
        this.properties = new Properties();
        this.properties.load(input);
    } catch (IOException ex) {
        ex.printStackTrace();
    }

  }
  
  public static Config getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new Config();
    }    
    return INSTANCE;
  }

  public static String getProperty(String key)  {
    Config instance = getInstance();
    return instance.properties.getProperty(key);
  }

  public static String getString(String key)  {
    Config instance = getInstance();
    return instance.properties.getProperty(key);
  }

  public static boolean getBoolean(String key)  {
    Config instance = getInstance();
    return Boolean.parseBoolean(instance.properties.getProperty(key));
  }

  public static int getInt(String key)  {
    Config instance = getInstance();
    return Integer.parseInt(instance.properties.getProperty(key));
  }
}
