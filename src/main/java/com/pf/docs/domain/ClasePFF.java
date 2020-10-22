package com.pf.docs.domain;

import com.pf.docs.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.jsoup.Jsoup;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClasePFF implements Serializable {
  private String link;
  private String description;
  private String thumbnail;

  public List<String> card(boolean plainText) throws IOException {
    return Jsoup.connect(link)
        .timeout(Util.TIMEOUT)
        .validateTLSCertificates(false)
        .get()
        .select("td[valign=top]:nth-child(3) > p")
        .stream()
        .map(e -> plainText ? e.text() : e.html())
        .collect(Collectors.toList());
  }
}
