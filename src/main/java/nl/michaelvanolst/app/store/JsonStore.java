package nl.michaelvanolst.app.store;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.michaelvanolst.app.dto.ScraperResultDto;

public class JsonStore extends FileStore {

  public JsonStore(String taskDirectoryName) {
    super(taskDirectoryName);
  }

  @Override
  public String getExtension() {
    return ".json";
  }

  @Override
  public boolean exists(String filename) {
    String extension = "";
    int i = filename.lastIndexOf('.');
    int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));

    if (i > p) {
      extension = filename.substring(i+1);
    }

    // check if the filename is a directory
    if(extension == "") {
      return this.getFile(filename).exists();
    }
    
    // if the filename is a file then check if it has content
    if(this.getFile(filename).length() == 0) {
      return false;
    }
    return true;
  }

  @Override
  public boolean create(String filename) throws IOException {
    return this.getFile(filename).createNewFile();
  }

  @Override
  public ScraperResultDto get(String filename) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(this.getFile(filename), ScraperResultDto.class);
  }

  @Override
  public void put(ScraperResultDto scraperResultDto) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(this.getAbsolutePath(scraperResultDto.getUrl())), scraperResultDto);
  }

  @Override
  public void putIfNotExists(ScraperResultDto scraperResultDto) throws IOException {

    if(this.exists(scraperResultDto.getUrl())) {
      return;
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(this.getAbsolutePath(scraperResultDto.getUrl())), scraperResultDto);
  }

}
