package nl.michaelvanolst.app.services;

import org.apache.logging.log4j.LogManager;

public final class Logger {
  private static Logger INSTANCE;
  private org.apache.logging.log4j.Logger logger;
  
  private Logger() {
     this.logger = LogManager.getLogger(Logger.class);
  }
  
  public static Logger getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new Logger();
    }    
    return INSTANCE;
  }

  public static void info(String message)  {
    Logger instance = getInstance();
    instance.logger.info(message);
  }

  public static void debug(String message)  {
    Logger instance = getInstance();
    instance.logger.debug(message);
  }

  public static void warn(String message)  {
    Logger instance = getInstance();
    instance.logger.warn(message);
  }

  public static void error(String message)  {
    Logger instance = getInstance();
    instance.logger.error(message);
  }

  public static void fatal(String message)  {
    Logger instance = getInstance();
    instance.logger.fatal(message);
  }
}
