package nl.michaelvanolst.app.dtos;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
  private String to;
  private String from;
  private String title;
}
