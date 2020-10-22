package com.pf.docs.domain;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category implements Serializable {
  private String link;
  private String description;
  private String thumbnail;
}
