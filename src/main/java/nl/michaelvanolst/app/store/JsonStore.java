package nl.michaelvanolst.app.store;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.michaelvanolst.app.Dto.ScraperResultDto;

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
  public void put(String filename, ScraperResultDto scraperResultDto) throws IOException {
    System.out.println(scraperResultDto.getUrl());
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(this.getAbsolutePath(scraperResultDto.getUrl())), scraperResultDto);
  }

}
