package nl.michaelvanolst.app.Tasks;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.michaelvanolst.app.Dto.ScraperResultDto;
import nl.michaelvanolst.app.Dto.TaskDto;
import nl.michaelvanolst.app.Exceptions.ScraperException;
import nl.michaelvanolst.app.Services.Config;
import nl.michaelvanolst.app.Services.Logger;
import nl.michaelvanolst.app.Services.MailService;
import nl.michaelvanolst.app.Services.Scraper;
import nl.michaelvanolst.app.Store.JsonStore;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class Task extends TimerTask {

  private final TaskDto taskDto;
  private final JsonStore jsonStore;
  private final MailService mailService;
  private List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

  public Task(TaskDto taskDto) {
    this.taskDto = taskDto;
    this.jsonStore = new JsonStore(this.taskDto.getTitle());
    this.mailService = new MailService();
  }

  public void run() {
    try {
      Scraper scraper = new Scraper(this.taskDto);
      this.results = scraper.get();
      this.handleResults();
    } catch(Exception ex) {
      Logger.fatal("Error in the scraper: "+ ex.getMessage());
    }
  }


  private void handleResults() throws IOException,MessagingException,InterruptedException {

    if(this.jsonStore.isEmpty()) {
      for(ScraperResultDto result: this.results) {
        this.jsonStore.putIfNotExists(result);
      }

      return;
    }
    
    for(ScraperResultDto result: this.results) {
      if(this.jsonStore.exists(result.getUrl())) {
        continue;
      }

      this.notify(result);
      this.jsonStore.put(result);
      TimeUnit.SECONDS.sleep(1);
    }

    Logger.info("finished");
  }


  private void notify(ScraperResultDto result) throws MessagingException,IOException {
    this.mailService.setResult(result);
    this.mailService.setTitle(this.taskDto.getTitle());
    this.mailService.setTo(this.taskDto.getMailTo());
    this.mailService.setFrom(this.taskDto.getMailFrom());
    this.mailService.send();
  }

}
