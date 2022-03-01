package nl.michaelvanolst.app.Watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.json.JSONObject;

import nl.michaelvanolst.app.Tasks.Task;


public class Watcher {
  
  static final long PERIOD = 1000 * 10;

  // static final TimerTask[] TASKS = {
  //   new BicycleDiscountShop(),
  //   new Mantel()
  // };

  private String directory;

  public Watcher(String directory) {
    this.directory = directory;
  }


  public void run() {
    try {
      List<File> files = this.getFiles();

      Timer scheduler = new Timer();

      for (File file : files) {
        Task task = this.parseFile(file);
        scheduler.schedule(task, 0, task.getInterval());
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }


  public List<File> getFiles() throws IOException {
    return Files.list(Paths.get(this.directory))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .map(Path::toFile)
                .collect(Collectors.toList());
  }


  public Task parseFile(File file) throws IOException {
    String json = Files.readString(file.toPath());

    JSONObject obj = new JSONObject(json);
    String url = obj.getString("url");
    String element = obj.getString("element");
    int interval = obj.getInt("interval");
    String filter = obj.getString("filter");

    return new Task(url, element, interval, filter);
  }

}
