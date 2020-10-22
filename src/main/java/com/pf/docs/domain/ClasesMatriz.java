package com.pf.docs.domain;

import java.io.Serializable;
import java.util.Map;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClasesMatriz implements Serializable {
  private String title;
  private String description;
  private Map<Integer, ClaseMatrizItem> matriz;
}
