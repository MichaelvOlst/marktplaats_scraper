package nl.michaelvanolst.app.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TaskDto {
  
  private String title;
  private String url;
  private int interval;
  private String filter;
  private String itemHolder;
  private String itemHref;
  private EmailDto email;

  private Map<String, String> selectors;

  @Override
  public String toString() {
    return this.url + " " + this.interval + " " + this.filter + " " + this.selectors.toString();
  }

}
