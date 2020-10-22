package com.pf.docs.domain;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TotalPuntosDeExperiencia implements Serializable {
  private Long lento;
  private Long intermedio;
  private Long rapido;
}
