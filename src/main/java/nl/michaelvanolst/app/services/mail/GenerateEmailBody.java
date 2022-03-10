package nl.michaelvanolst.app.services.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nl.michaelvanolst.app.dtos.ScraperResultDto;

public class GenerateEmailBody {

  private final StringBuilder stringBuilder;
  private final ScraperResultDto scraperResultDto;

  public GenerateEmailBody(ScraperResultDto scraperResultDto) {
    this.stringBuilder = new StringBuilder();
    this.scraperResultDto = scraperResultDto;
  }

  public String get() throws IOException {
    
    this.stringBuilder.append(this.getEmailTemplate());

    for (Map.Entry<String, String> entry : this.scraperResultDto.getContents().entrySet()) {
      this.replace("{{"+ entry.getKey() +"}}", entry.getValue());
    }

    return this.stringBuilder.toString();
  }

  private String getEmailTemplate() throws IOException {
    String emailHtmlPath = StringUtils.stripEnd(System.getProperty("user.dir"), "/") + "/templates/notify_email.html";
    File file = new File(emailHtmlPath);

    return Files.readString(file.toPath());
  }

  private void replace(String from, String to) {
    int index = -1;
    while ((index = this.stringBuilder.lastIndexOf(from)) != -1) {
      this.stringBuilder.replace(index, index + from.length(), to);
    }
  }
  
}
