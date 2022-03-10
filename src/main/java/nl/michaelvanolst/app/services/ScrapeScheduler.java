package nl.michaelvanolst.app.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.michaelvanolst.app.dtos.TaskDto;
import nl.michaelvanolst.app.services.mail.MailService;
import nl.michaelvanolst.app.store.JsonStore;
import nl.michaelvanolst.app.tasks.ScheduleTask;

public class ScrapeScheduler {

  private List<TaskDto> tasks;

  public ScrapeScheduler(List<TaskDto> tasks) {
    this.tasks = tasks;
  }

  public void run() {
    Timer scheduler = new Timer();
    for (TaskDto taskDto : this.tasks) {
      TimerTask timerTask = new ScheduleTask(taskDto, new JsonStore(taskDto.getTitle()), new MailService());
      scheduler.schedule(timerTask, 0,Long.valueOf(taskDto.getInterval() * 1000));
    }
  }

}
