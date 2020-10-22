package com.pf.docs.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlosaryPF {
  private String word;
  private String description;
}
