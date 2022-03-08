package nl.michaelvanolst.app.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.michaelvanolst.app.dto.TaskDto;

public class TaskCollector {

  private final String directory;
  private List<TaskDto> tasks = new ArrayList<TaskDto>();

  public TaskCollector(String directory) {
    this.directory = directory;
  }
  
  public List<TaskDto> get() throws IOException {

    for (File file : this.getTaskFilesFromDirectory()) {
      this.tasks.add(this.parseFileToTask(file));
    }

    return this.tasks;
  }


  private List<File> getTaskFilesFromDirectory() throws IOException {
    return Files.list(Paths.get(this.directory))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .map(Path::toFile)
                .collect(Collectors.toList());
  }


  private TaskDto parseFileToTask(File file) throws IOException {
    String json = Files.readString(file.toPath());

    ObjectMapper objectMapper = new ObjectMapper();
    TaskDto task = objectMapper.readValue(json, TaskDto.class);

    return task;
  }

}
