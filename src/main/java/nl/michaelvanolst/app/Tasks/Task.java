package nl.michaelvanolst.app.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.michaelvanolst.app.Config;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class Task extends TimerTask {

  private final String url;
  private final String element;
  private final int interval;
  private final String filter;

  private Document document;

  public Task(String url, String element, int interval, String filter) {
    this.url = url;
    this.element = element;
    this.interval = interval;
    this.filter = filter;
  }

  public String getUrl() {
    return this.url;
  }

  public String getElement() {
    return this.element;
  }

  public Long getInterval() {
    return Long.valueOf(interval * 1000); 
  }

  public void run() {
    try {
      this.document = Jsoup.connect(url).get();
      this.handle();
    } catch(IOException ex) {
      System.out.println(ex.getStackTrace());
    }
  }

  private void handle() {
    List<String> filteredList = new ArrayList<String>();

    String[] filterSplitted = this.filter.split(":");
    String filter = filterSplitted[0].trim();
    String value = filterSplitted[1].trim();

    Elements Elements = this.document.select(this.element);
    for(Element element : Elements) {
      String text = element.text();

      if(filter.contains("contains") && text.contains(value)) {
        filteredList.add(text);
      }
    }

    this.notifyByMail(filteredList);
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
