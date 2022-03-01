package nl.michaelvanolst.app;

import java.util.List;
import java.util.Timer;
import nl.michaelvanolst.app.Tasks.Task;

public class ScrapeScheduler {

  private List<Task> tasks;

  public ScrapeScheduler(List<Task> tasks) {
    this.tasks = tasks;
  }

  public void run() {
    Timer scheduler = new Timer();
    for (Task task : this.tasks) {
      scheduler.schedule(task, 0, task.getInterval());
    }
  }

}
