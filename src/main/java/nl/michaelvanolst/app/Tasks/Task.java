package nl.michaelvanolst.app.Tasks;

import java.util.List;
import java.util.TimerTask;
import org.jsoup.nodes.Document;

import nl.michaelvanolst.app.Config;
import nl.michaelvanolst.app.Scraper;
import nl.michaelvanolst.app.Dto.ScraperResultDto;
import nl.michaelvanolst.app.Dto.TaskDto;
import nl.michaelvanolst.app.Exceptions.ScraperException;
import nl.michaelvanolst.app.store.JsonStore;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class Task extends TimerTask {

  private final TaskDto taskDto;

  public Task(TaskDto taskDto) {
    this.taskDto = taskDto;
  }

  public void run() {
    try {
      Scraper scraper = new Scraper(this.taskDto, new JsonStore(this.taskDto.getTitle()));
      for(ScraperResultDto result : scraper.get()) {
        System.out.print("result " + result.toString());
      }
    } catch(Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  private void parseAndFilterContent(String[] contents) {
    // List<String> filteredList = new ArrayList<String>();

    for(String content : contents) {
      System.out.println(content);
    }

    // String[] filterSplitted = this.filter.split(":");
    // String filter = filterSplitted[0].trim();
    // String value = filterSplitted[1].trim();

    // if(filter.contains("contains") && contents.contains(value)) {
    //   System.out.println(contents);
    //   // filteredList.add(text);
    // }
    

    // this.notifyByMail(filteredList);
  }


  private void notifyByMail(List<String> filteredList) {
    final String username = Config.getProperty("mail.username");
    final String password = Config.getProperty("mail.password");

    Properties prop = new Properties();
    prop.put("mail.smtp.host", Config.getProperty("mail.host"));
    prop.put("mail.smtp.port", Config.getProperty("mail.port"));
    prop.put("mail.smtp.auth", Config.getProperty("mail.auth"));
    // prop.put("mail.smtp.starttls.enable", "true"); //TLS
    
    Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("michaelvolst@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("michaelvolst@gmail.com")
        );
        message.setSubject("Testing Gmail TLS");
        message.setText("Dear Mail Crawler,"
                + "\n\n Please do not spam my email!");

        Transport.send(message);

        System.out.println("Done");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
}
