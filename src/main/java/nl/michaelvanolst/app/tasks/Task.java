package nl.michaelvanolst.app.tasks;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.michaelvanolst.app.dto.EmailDto;
import nl.michaelvanolst.app.dto.ScraperResultDto;
import nl.michaelvanolst.app.dto.TaskDto;
import nl.michaelvanolst.app.exceptions.ScraperException;
import nl.michaelvanolst.app.services.Config;
import nl.michaelvanolst.app.services.Logger;
import nl.michaelvanolst.app.services.MailService;
import nl.michaelvanolst.app.services.Scraper;
import nl.michaelvanolst.app.store.FileStore;
import nl.michaelvanolst.app.store.JsonStore;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class Task extends TimerTask {

  private final TaskDto taskDto;
  private final FileStore store;
  private final MailService mailService;
  private List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

  public Task(TaskDto taskDto, FileStore store, MailService mailService) {
    this.taskDto = taskDto;
    this.store = store;
    this.mailService = mailService;
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

    if(this.store.isEmpty()) {
      for(ScraperResultDto result: this.results) {
        this.store.putIfNotExists(result);
      }

      return;
    }
    
    for(ScraperResultDto result: this.results) {
      if(this.store.exists(result.getUrl())) {
        continue;
      }

      this.notify(result);
      this.store.put(result);
      TimeUnit.SECONDS.sleep(1);
    }

    Logger.info("finished");
  }


  private void notify(ScraperResultDto result) throws MessagingException,IOException {
    EmailDto email = this.taskDto.getEmail();
    this.mailService.setResult(result);
    this.mailService.setEmail(this.taskDto.getEmail());
    this.mailService.send();
  }

}
