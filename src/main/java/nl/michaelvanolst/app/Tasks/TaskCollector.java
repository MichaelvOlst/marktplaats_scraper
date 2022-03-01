package nl.michaelvanolst.app.Tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class TaskCollector {

  private final String directory;
  private List<Task> tasks = new ArrayList<Task>();

  public TaskCollector(String directory) {
    this.directory = directory;
  }
  
  public List<Task> get() throws IOException {

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


  private Task parseFileToTask(File file) throws IOException {
    String json = Files.readString(file.toPath());
    JSONObject obj = new JSONObject(json);

    JSONArray jsonSelectors = obj.getJSONArray("selectors");

    List<String> selectorsList = new ArrayList<String>();
    for(int i=0; i < jsonSelectors.length(); i++){
      selectorsList.add(jsonSelectors.getString(i));
    }

    return new Task(
      obj.getString("url"),
      selectorsList.toArray(new String[selectorsList.size()]),
      obj.getInt("interval"),
      obj.getString("filter")
    );
  }

}
