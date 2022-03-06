package nl.michaelvanolst.app.Store;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import nl.michaelvanolst.app.Dto.ScraperResultDto;
import nl.michaelvanolst.app.Services.Config;
import nl.michaelvanolst.app.Services.Logger;

public abstract class FileStore {

  public String storageDirectory;

  public FileStore(String taskDirectoryName) {
    taskDirectoryName = this.generateDirectoryName(taskDirectoryName);
    this.storageDirectory = System.getProperty("user.dir") + "/storage/" + taskDirectoryName;

    this.createDirectoryIfNotExists();
    this.cleanOldFiles();
  }

  abstract public String getExtension();

  abstract public boolean exists(String filename);

  abstract public boolean create(String filename) throws IOException;

  abstract public ScraperResultDto get(String filename) throws IOException;

  abstract public void put(ScraperResultDto scraperResultDto) throws IOException;

  public boolean isEmpty() throws IOException {

    Path storageDirectory = this.getFile(this.storageDirectory).toPath();

    if (Files.isDirectory(storageDirectory)) {
      try (DirectoryStream<Path> directory = Files.newDirectoryStream(storageDirectory)) {
        return !directory.iterator().hasNext();
      }
    }

    return false;
  }

  protected File getFile(String filename) {
    return new File(this.getAbsolutePath(filename));
  }

  protected String getAbsolutePath(String filename) {
    return this.storageDirectory + "/" + this.filenameToMD5(filename) + this.getExtension();
  }

  private String generateDirectoryName(String directory) {
    return directory.toLowerCase().replace(" ", "_");
  }

  private void createDirectoryIfNotExists() {
    File dir = new File(this.storageDirectory);
    if (! dir.exists()){
      dir.mkdirs();
    }
  }
  
  public boolean storageFileExists(String key) throws IOException {
    key = key.toLowerCase().replace(" ", "_");
    
    File storageFile = new File(this.storageDirectory + "/" + key);
    if (! storageFile.exists()){
      storageFile.createNewFile();
      return false;
    }

    return true;
  }

  protected void cleanOldFiles() {
    File folder = new File(this.storageDirectory);
 
    if (!folder.exists()) {
      return;
    }

    File[] listFiles = folder.listFiles();
 
    long eligibleForDeletion = System.currentTimeMillis() - (Config.getInt("scraper.max_days") * 24 * 60 * 60 * 1000);
 
    for (File listFile: listFiles) {
      if (listFile.lastModified() < eligibleForDeletion) {
        Logger.info("deleting: "+listFile.getAbsolutePath());
        if (!listFile.delete()) {
          Logger.error("Unable to delete file: " + listFile.getAbsolutePath());
        }
      }
    }

  }

  protected String filenameToMD5(String filename) {
    String digest = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hash = md.digest(filename.getBytes("UTF-8"));
      
      //converting byte array to Hexadecimal String
      StringBuilder sb = new StringBuilder(2*hash.length);
      for(byte b : hash){
        sb.append(String.format("%02x", b&0xff));
      }
    
      digest = sb.toString();
    } catch (UnsupportedEncodingException ex) {
      Logger.fatal("Unsupperted encoding: " + ex.getMessage());
    } catch (NoSuchAlgorithmException ex) {
      Logger.fatal("No algorithm: " + ex.getMessage());
    }
    return digest;
  }

}