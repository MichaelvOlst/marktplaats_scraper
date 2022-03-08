package nl.michaelvanolst.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
  private String to;
  private String from;
  private String title;
}
