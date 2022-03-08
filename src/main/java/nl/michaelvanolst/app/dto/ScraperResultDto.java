package nl.michaelvanolst.app.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScraperResultDto {
  
  private String url;
  private Map<String, String> contents;

  @Override
  public String toString() {
    return this.url + " " + this.contents.toString();
  }

}
