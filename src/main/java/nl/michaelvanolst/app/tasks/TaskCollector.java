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

import nl.michaelvanolst.app.dtos.TaskDto;

public class TaskCollector {

  private final String directory;
  private List<TaskDto> taskDtos = new ArrayList<TaskDto>();

  public TaskCollector(String directory) {
    this.directory = directory;
  }
  
  public List<TaskDto> get() throws IOException {
    
    for (File file : this.getTaskFilesFromDirectory()) {
      this.taskDtos.add(this.parseFileToTask(file));
    }

    return this.taskDtos;
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
    TaskDto taskDto = objectMapper.readValue(json, TaskDto.class);

    return taskDto;
  }

}
