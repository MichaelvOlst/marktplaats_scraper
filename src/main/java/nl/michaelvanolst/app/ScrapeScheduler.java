package nl.michaelvanolst.app;

import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;

import nl.michaelvanolst.app.Dto.TaskDto;
import nl.michaelvanolst.app.Tasks.Task;

public class ScrapeScheduler {

  private List<TaskDto> tasks;

  public ScrapeScheduler(List<TaskDto> tasks) {
    this.tasks = tasks;
  }

  public void run() {
    Timer scheduler = new Timer();
    for (TaskDto task : this.tasks) {
      scheduler.schedule(new Task(task), 0, Long.valueOf(task.getInterval() * 1000));
    }
  }

}
