package com.pf.docs.domain;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClaseMatrizItem implements Serializable {
  private TotalPuntosDeExperiencia totalPuntosDeExperiencia;
  private String dotes;
  private String puntuacionDeCaracteristica;
  private String riquezaPJ;
  private String riquezaPNJ;
}
