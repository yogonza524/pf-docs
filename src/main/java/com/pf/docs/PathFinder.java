package com.pf.docs;

import com.pf.docs.domain.*;
import com.pf.docs.util.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class PathFinder {

  public static List<Category> categories() throws IOException {
    return SpyService.categories();
  }

  public static List<GlosaryPF> glosary() throws IOException {
    return SpyService.glosary();
  }

  public static List<RazaPF> razasPF() throws IOException {
    return SpyService.razasPF();
  }

  public static List<ClasePF> clasesPF() throws IOException {
    return SpyService.clasesPF();
  }

  public static ClasesMatriz clasesMatriz() {
    return SpyService.clasesMatriz();
  }

  private static final class SpyService {
    private static final String HOME = "/index.php";
    private static final String GLOSARY_PF = "/GlosarioPF.php";
    private static final String RAZAS_PF = "/RazasPF.php";
    private static final String CLASES_PF = "/ClasesPF.php";
    private static final String URL_BASE = "https://www.rolroyce.com/rol/DDP";

    public static List<Category> categories() throws IOException {
      return Jsoup.connect(URL_BASE + HOME)
          .timeout(Util.TIMEOUT)
          .validateTLSCertificates(false)
          .get()
          .select("a")
          .stream()
          .filter(l -> l.html().contains("/DDP/") && l.parents().select("td > p").size() > 0)
          .map(
              link -> {
                Element row = link.parent().parent().parent();
                String a = link.select("a").size() > 0 ? link.select("a").attr("href") : "";
                String desc =
                    row.children().stream()
                        .filter(r -> r.select("p").size() > 0)
                        .map(p -> p.text())
                        .collect(Collectors.joining(" "));
                if (StringUtils.isNotBlank(a) && StringUtils.isNotBlank(desc)) {
                  String image = link.select("a > img").attr("abs:src");
                  return Category.builder()
                      .link(URL_BASE + "/" + a)
                      .description(desc)
                      .thumbnail(image)
                      .build();
                }
                return null;
              })
          .filter(c -> c != null)
          .collect(Collectors.toList());
    }

    public static List<GlosaryPF> glosary() throws IOException {
      return Jsoup.connect(URL_BASE + GLOSARY_PF)
          .timeout(Util.TIMEOUT)
          .validateTLSCertificates(false)
          .get()
          .select("tr")
          .stream()
          .filter(tr -> tr.children().size() == 2 && tr.selectFirst("td").attributes().size() == 0)
          .map(
              tr -> {
                String word = tr.selectFirst("td").text();
                String description = tr.select("td").get(1).text();
                return GlosaryPF.builder().word(word).description(description).build();
              })
          .collect(Collectors.toList());
    }

    public static List<RazaPF> razasPF() throws IOException {
      return Jsoup.connect(URL_BASE + RAZAS_PF)
          .timeout(Util.TIMEOUT)
          .validateTLSCertificates(false)
          .get()
          .select("a")
          .stream()
          .filter(l -> l.html().contains("/DDP/"))
          .map(
              link -> {
                Element row = link.parent().parent();

                String a = row.selectFirst("a").attr("abs:href");
                return RazaPF.builder()
                    .link(a)
                    .description(row.text())
                    .thumbnail(row.select("img").attr("abs:src"))
                    .build();
              })
          .filter(c -> c != null)
          .collect(Collectors.toList());
    }

    private static List<String> razaDesc(String url) throws IOException {
      return Jsoup.connect(url)
          .timeout(15 * 1000)
          .validateTLSCertificates(false)
          .get()
          .select("td[valign=top]:nth-child(3) > p")
          .stream()
          .map(e -> e.html())
          .collect(Collectors.toList());
    }

    public static List<ClasePF> clasesPF() throws IOException {
      return Jsoup.connect(URL_BASE + CLASES_PF)
          .timeout(Util.TIMEOUT)
          .validateTLSCertificates(false)
          .get()
          .select("a")
          .stream()
          .filter(l -> l.html().contains("/DDP/"))
          .map(
              l -> {
                Element row = l.parent().parent().parent();
                boolean isOutlier =
                    row.selectFirst("td > div") != null && row.selectFirst("td > div").hasText();
                if (isOutlier) {
                  row = row.select("td > b").first();
                }
                String link = row.select("a").attr("abs:href");
                String thumbnail = row.select("a > img").attr("abs:src");
                String desc = isOutlier ? row.parent().text() : row.text();

                return ClasePF.builder().link(link).thumbnail(thumbnail).description(desc).build();
              })
          .filter(c -> c != null)
          .collect(Collectors.toList());
    }

    public static ClaseHibridaDefinition claseHibridaDefinition() {
      return ClaseHibridaDefinition.builder()
          .title("Clases Híbridas")
          .description(
              "La mayoría de los héroes avanzan siguiendo un único sendero para convertirse en un temible guerrero, un clérigo pío o un poderoso mago, pero algunos siguen varios caminos. Para ellos, puede ser duro encontrar un equilibrio entre las aptitudes ofrecidas por clases muy dispares. Las clases híbridas resuelven este dilema fundiendo rasgos de dos clases, añadiendo reglas para hacerlos funcionar juntos sin problemas.")
          .build();
    }

    public static ClasesMatriz clasesMatriz() {
      Map<Integer, ClaseMatrizItem> matriz = new HashMap<>();

      matriz.put(
          1, ClaseMatrizItem.builder().dotes("1°").riquezaPJ("Varia").riquezaPNJ("260 po").build());

      matriz.put(
          2,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(3000L)
                      .intermedio(2000L)
                      .rapido(1300L)
                      .build())
              .riquezaPJ("1.000 po")
              .riquezaPNJ("390 po")
              .build());

      matriz.put(
          3,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(750L)
                      .intermedio(5000L)
                      .rapido(3300L)
                      .build())
              .dotes("2°")
              .riquezaPJ("3.000 po")
              .riquezaPNJ("780 po")
              .build());

      return ClasesMatriz.builder()
          .title("Clases Matriz")
          .description(
              "cada una de las clases híbridas (que aparecen en Advanced Class Guide) listan dos clases que utilizan para formar la base de su temática. Aunque que un personaje puede multiclasear con estas clases matriz, normalmente tiene como resultado aptitudes redundantes. Estas aptitudes no se apilan a menos que se especifique. Si un rasgo de clase permite al personaje realizar una elección que sólo se puede hacer una vez (como un linaje), esta opción debe coincidir con opciones similares tomadas en las clases matriz y viceversa (por ejemplo, elegir el mismo linaje).")
          .matriz(matriz)
          .build();
    }
  }
}
