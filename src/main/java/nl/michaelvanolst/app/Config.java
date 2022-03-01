package nl.michaelvanolst.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
  private static Config INSTANCE;
  private Properties properties;
  
  private Config() {

    String configPropertyFile = System.getProperty("user.dir") + "/src/main/resources/config.properties";

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
}
