package nl.michaelvanolst.app;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.Getter;
import lombok.Setter;
import nl.michaelvanolst.app.Dto.ScraperResultDto;

@Getter
@Setter
public class MailService {

  private String from;
  private String to;
  private ScraperResultDto result;
  private Session session;
  
  public MailService() {
    this.createSession();
  }

  public void createSession() {
    final String username = Config.getProperty("mail.username");
    final String password = Config.getProperty("mail.password");

    Properties prop = new Properties();
    prop.put("mail.smtp.host", Config.getProperty("mail.host"));
    prop.put("mail.smtp.port", Config.getProperty("mail.port"));
    prop.put("mail.smtp.auth", Config.getProperty("mail.auth"));

    Authenticator authenticator = new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };
    
    this.session = Session.getInstance(prop, authenticator);
  }


  public void send() throws MessagingException {
    Message message = new MimeMessage(this.session);
    message.setFrom(new InternetAddress(this.from));
    message.setRecipients(
      Message.RecipientType.TO,
      InternetAddress.parse(this.to)
    );
    message.setSubject("Testing Gmail TLS");
    message.setText("Dear Mail Crawler," + "\n\n " + result.toString());

    Transport.send(message);

    Logger.info("Done sending email");
  }

}
