package nl.michaelvanolst.app.services.mail;

import java.io.IOException;
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
import nl.michaelvanolst.app.dtos.EmailMessageDto;
import nl.michaelvanolst.app.services.Config;

@Getter
@Setter
public class MailService {

  private EmailMessageDto emailMessageDto;
  private final Session session;
  
  public MailService() {
    this.session = this.createSession();
  }

  public Session createSession() {
    final String username = Config.getString("mail.username");
    final String password = Config.getString("mail.password");

    Properties prop = new Properties();
    prop.put("mail.smtp.host", Config.getString("mail.host"));
    prop.put("mail.smtp.port", Config.getString("mail.port"));
    prop.put("mail.smtp.auth", Config.getString("mail.auth"));

    if(Config.getBoolean("mail.ssl")) {
      prop.put("mail.smtp.starttls.enable", "true"); //TLS
      prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }
    
    Authenticator authenticator = new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    };
    
    return Session.getInstance(prop, authenticator);
  }


  public void send() throws MessagingException,IOException {
    Message message = new MimeMessage(this.session);
    message.setFrom(new InternetAddress(this.emailMessageDto.getFrom()));
    message.setRecipients(
      Message.RecipientType.TO,
      InternetAddress.parse(this.emailMessageDto.getTo())
    );
    message.setSubject(this.emailMessageDto.getTitle());
    message.setText(this.emailMessageDto.getBody());
    message.setHeader("Content-Type", "text/html");

    Transport.send(message);
  }

}
