package nl.michaelvanolst.app.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailMessageDto {
  
  private final String to;
  private final String from;
  private final String title;
  private final String body;

}
