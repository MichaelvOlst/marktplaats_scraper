package nl.michaelvanolst.app.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import nl.michaelvanolst.app.dto.TaskDto;
import nl.michaelvanolst.app.store.JsonStore;
import nl.michaelvanolst.app.tasks.Task;

public class ScrapeScheduler {

  private List<TaskDto> tasks;

  public ScrapeScheduler(List<TaskDto> tasks) {
    this.tasks = tasks;
  }

  public void run() {
    Timer scheduler = new Timer();
    for (TaskDto task : this.tasks) {
      TimerTask timerTask = new Task(task, new JsonStore(task.getTitle()), new MailService());
      scheduler.schedule(timerTask, 0,Long.valueOf(task.getInterval() * 1000));
    }
  }

}
