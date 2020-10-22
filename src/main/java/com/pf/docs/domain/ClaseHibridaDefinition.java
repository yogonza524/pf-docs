package com.pf.docs.domain;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClaseHibridaDefinition implements Serializable {
  private String title;
  private String description;
}
