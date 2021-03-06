package nl.michaelvanolst.app.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.michaelvanolst.app.dtos.EmailMessageDto;
import nl.michaelvanolst.app.dtos.ScraperResultDto;
import nl.michaelvanolst.app.dtos.TaskDto;
import nl.michaelvanolst.app.services.Logger;
import nl.michaelvanolst.app.services.Scraper;
import nl.michaelvanolst.app.services.mail.GenerateEmailBody;
import nl.michaelvanolst.app.services.mail.MailService;
import nl.michaelvanolst.app.store.FileStore;

import javax.mail.MessagingException;
import java.io.IOException;

public class ScheduleTask extends TimerTask {

  private final TaskDto taskDto;
  private final FileStore store;
  private List<ScraperResultDto> results = new ArrayList<ScraperResultDto>();

  public ScheduleTask(TaskDto taskDto, FileStore store) {
    this.taskDto = taskDto;
    this.store = store;
  }

  public void run() {
    try {
      Scraper scraper = new Scraper(this.taskDto);
      this.results = scraper.get();

      Logger.info("results: "+ this.results);


      this.handleResults();
    } catch(Exception ex) {
      Logger.fatal("Error in the scraper: "+ ex.getMessage());
    }
  }


  private void handleResults() throws IOException, MessagingException,InterruptedException {

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

    GenerateEmailBody emailBodyGenerator = new GenerateEmailBody(result);

    EmailMessageDto emailMessageDto = EmailMessageDto.builder()
      .to(this.taskDto.getEmailTo())
      .from(this.taskDto.getEmailFrom())
      .title(this.taskDto.getEmailTitle())
      .body(emailBodyGenerator.get())
      .build();

    MailService mailservice = new MailService(emailMessageDto);
    mailservice.send();
  }

}
