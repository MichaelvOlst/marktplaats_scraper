package nl.michaelvanolst.app.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TaskDto {
  
  private String title;
  private String url;
  private int interval;
  private String itemHolder;
  private String itemHref;
  private EmailDto email;

  private Map<String, String> selectors;

  @Override
  public String toString() {
    return this.url + " " + this.interval + " " + this.itemHolder + " " + this.selectors.toString();
  }

  public String getEmailFrom()
  {
    return this.email.getFrom();
  }

  public String getEmailTo()
  {
    return this.email.getTo();
  }

  public String getEmailTitle()
  {
    return this.email.getTitle();
  }

}
