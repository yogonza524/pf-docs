package com.pf.docs;

import com.pf.docs.domain.*;
import com.pf.docs.util.Util;
import java.io.IOException;
import java.util.Arrays;
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

  public static List<ClasePFF> clasesPFF() throws IOException {
    return SpyService.clasesPFF();
  }
    public static List<Arquetipo> arquetipos() throws IOException {
      return SpyService.arquetipos();
    }
    public static List<ArquetipoInfo> arquetipoInfo() {
      return SpyService.arquetiposInfo();
    }

  private static final class SpyService {
    private static final String HOME = "/index.php";
    private static final String GLOSARY_PF = "/GlosarioPF.php";
    private static final String RAZAS_PF = "/RazasPF.php";
    private static final String CLASES_PF = "/ClasesPF.php";
    private static final String CLASES_PFF = "/ClasesPPF.php";
    private static final String ARQUETIPOS = "/ArquetiposPF.php";
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

      matriz.put(
          4,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(14000L)
                      .intermedio(9000L)
                      .rapido(6000L)
                      .build())
              .puntuacionDeCaracteristica("1°")
              .riquezaPJ("6.000 po")
              .riquezaPNJ("1650 po")
              .build());
      matriz.put(
          5,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(23000L)
                      .intermedio(15000L)
                      .rapido(10000L)
                      .build())
              .dotes("3°")
              .riquezaPJ("10.500 po")
              .riquezaPNJ("2400 po")
              .build());

      matriz.put(
          6,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(35000L)
                      .intermedio(23000L)
                      .rapido(15000L)
                      .build())
              .riquezaPJ("16.000 po")
              .riquezaPNJ("3.450 po")
              .build());

      matriz.put(
          7,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(53000L)
                      .intermedio(35000L)
                      .rapido(23000L)
                      .build())
              .dotes("4°")
              .riquezaPJ("23.500 po")
              .riquezaPNJ("4.650 po")
              .build());

      matriz.put(
          8,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(77000L)
                      .intermedio(51000L)
                      .rapido(34000L)
                      .build())
              .puntuacionDeCaracteristica("2°")
              .riquezaPJ("33.000 po")
              .riquezaPNJ("6.000 po")
              .build());

      matriz.put(
          9,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(115000L)
                      .intermedio(75000L)
                      .rapido(50000L)
                      .build())
              .dotes("5°")
              .riquezaPJ("46.000 po")
              .riquezaPNJ("7.800 po")
              .build());

      matriz.put(
          10,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(160000L)
                      .intermedio(105000L)
                      .rapido(71000L)
                      .build())
              .riquezaPJ("62.000 po")
              .riquezaPNJ("10.050 po")
              .build());

      matriz.put(
          11,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(235000L)
                      .intermedio(155000L)
                      .rapido(105000L)
                      .build())
              .dotes("6°")
              .riquezaPJ("82.000 po")
              .riquezaPNJ("12.750 po")
              .build());

      matriz.put(
          12,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(330000L)
                      .intermedio(220000L)
                      .rapido(115000L)
                      .build())
              .puntuacionDeCaracteristica("3°")
              .riquezaPJ("108.000 po")
              .riquezaPNJ("16.350 po")
              .build());

      matriz.put(
          13,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(475000L)
                      .intermedio(315000L)
                      .rapido(210000L)
                      .build())
              .dotes("7°")
              .riquezaPJ("104.000 po")
              .riquezaPNJ("21.000 po")
              .build());

      matriz.put(
          14,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(665000L)
                      .intermedio(445000L)
                      .rapido(295000L)
                      .build())
              .riquezaPJ("185.000 po")
              .riquezaPNJ("27.000 po")
              .build());

      matriz.put(
          15,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(955000L)
                      .intermedio(635000L)
                      .rapido(425000L)
                      .build())
              .dotes("8°")
              .riquezaPJ("240.000 po")
              .riquezaPNJ("34.800 po")
              .build());

      matriz.put(
          16,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(955000L)
                      .intermedio(635000L)
                      .rapido(425000L)
                      .build())
              .dotes("8°")
              .riquezaPJ("240.000 po")
              .riquezaPNJ("34.800 po")
              .build());

      matriz.put(
          17,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(1000000L)
                      .intermedio(1300000L)
                      .rapido(850000L)
                      .build())
              .dotes("9°")
              .riquezaPJ("410.000 po")
              .riquezaPNJ("58.500 po")
              .build());

      matriz.put(
          18,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(2700000L)
                      .intermedio(1800000L)
                      .rapido(1200000L)
                      .build())
              .riquezaPJ("530.000 po")
              .riquezaPNJ("75.000 po")
              .build());

      matriz.put(
          19,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(3850000L)
                      .intermedio(2550000L)
                      .rapido(1700000L)
                      .build())
              .dotes("10°")
              .riquezaPJ("685.000 po")
              .riquezaPNJ("96.000 po")
              .build());

      matriz.put(
          20,
          ClaseMatrizItem.builder()
              .totalPuntosDeExperiencia(
                  TotalPuntosDeExperiencia.builder()
                      .lento(5350000L)
                      .intermedio(3600000L)
                      .rapido(2400000L)
                      .build())
              .puntuacionDeCaracteristica("5°")
              .riquezaPJ("880.000 po")
              .riquezaPNJ("123.000 po")
              .build());

      return ClasesMatriz.builder()
          .title("Clases Matriz")
          .description(
              "cada una de las clases híbridas (que aparecen en Advanced Class Guide) listan dos clases que utilizan para formar la base de su temática. Aunque que un personaje puede multiclasear con estas clases matriz, normalmente tiene como resultado aptitudes redundantes. Estas aptitudes no se apilan a menos que se especifique. Si un rasgo de clase permite al personaje realizar una elección que sólo se puede hacer una vez (como un linaje), esta opción debe coincidir con opciones similares tomadas en las clases matriz y viceversa (por ejemplo, elegir el mismo linaje).")
          .matriz(matriz)
          .build();
    }

    public static List<ClasePFF> clasesPFF() throws IOException {
      return Jsoup.connect(URL_BASE + CLASES_PFF)
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
                return ClasePFF.builder()
                    .link(a)
                    .description(row.text())
                    .thumbnail(row.select("img").attr("abs:src"))
                    .build();
              })
          .filter(c -> c != null)
          .collect(Collectors.toList());
    }
      public static List<Arquetipo> arquetipos() throws IOException {
          return Jsoup.connect(URL_BASE + ARQUETIPOS)
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
                              return Arquetipo.builder()
                                      .link(a)
                                      .description(row.parent().text())
                                      .thumbnail(row.select("img").attr("abs:src"))
                                      .build();
                          })
                  .filter(c -> c != null)
                  .collect(Collectors.toList());
      }

      public static List<ArquetipoInfo> arquetiposInfo() {
        return Arrays.asList(
            new ArquetipoInfo("resume", "Los arquetipos son una forma rápida y sencilla de especializar a los personajes de una clase, añadiendo nuevas y divertidas aptitudes para aventureros ya definidos. Los personajes puede elegir más de un arquetipo siempre que cumplan los prerrequisitos."),
                new ArquetipoInfo("Usar arquetipos de clase", "Cada clase básica parte de una idea central, un concepto básico que representa la interpretación general de lo que un personaje de cierca clase debería ser y está diseñado para ser útil como base para la mayor variedad posible de personajes. Más allá de ese concepto básico, sin embargo, existe el potencial para innumerables interpretaciones y refinamientos. Un miembro de la clase de bardo, por ejemplo, podría ser un arqueólogo incorregible, un elegante espadachín, un peligrosamente grácil bailarín derviche, cada uno refinado mediante la elección de los detalles del trasfondo, opciones de clase y reglas específicas como dotes para simular mejor el personaje que el jugador imagina y hacerlo más efectivo en la persecución de sus objetivos específicos.\n" +
                        "\n" +
                        "Algunos conceptos de personaje son demasiado cercanos a clases existentes como para merecer una clase propia, pero se comprueba que son lo bastante presentes y excitantes como para que regresen una y otra vez al juego. Para estas siguaciones, el Juego de Rol Pathfinder ha creado los arquetipos, modificaciones predefinidas para las aptitudes que pueden ser fácilmente intercambiables a partir de una clase determinada para ayudar a personalizar su concepto. Para ayudar a los jugadores interesados en crear personajes icónicos de fantasía, los arquetipos exploran nuevas reglas, opciones y rasgos de clase alternativos para muchas clases diferentes.\n" +
                        "\n" +
                        "Aunque los tipos de opciones presentados para cada clase difieren, cada subsistema y arquetipo está personalizado para servir a esa clase, emulando las aptitudes y talentos de los personajes clásicos de fantasía, y amplía la libertad de los jugadores para diseñar exactamente los personajes que desean.\n" +
                        "\n"),
                new ArquetipoInfo("Rasgos de clase alternativos", "La forma principal por la que los arquetipos modifican a sus correspondientes clases básicas es a través del uso de rasgos de clase alternativos. Cuando un personaje elige una clase, normalmente debe elegir utilizar los rasgos de clase normales que aparecen en la clase original. Excepto si elige adoptar un arquetipo. Cada rasgo de clase alternativo presentado en un arquetipo reemplaza a un rasgo de clase específico de su clase padre. Por ejemplo, el rasgo de clase redirección del arquetipo de monje fluido reemplaza el rasgo de clase Puñetazo aturdidor de la clase estándar de monje.\n" +
                        "\n" +
                        "Cuando un arquetipo incluye múltiples rasgos de clase alternativos, el personaje debe usarlos todos, impidiendo que el personaje vuelva a obtener ciertos rasgos de clase estándar, pero reemplazándolos con otras opciones. Todos los demás rasgos de clase de la clase básica que no se mencinen entre los rasgos de clase alternativos permanecen inalterados y se adquieren normalmente cuando el personaje llega a cierto nivel, a menos que se indique otra cosa. Un parsonaje que adopta un rasgo de clase alternativo no cuenta como si poseyera el rasgo de clase que fue reemplazado a efectos de cumplir cualquier requerimiento o prerrequisito.\n" +
                        "\n" +
                        "Un personaje puede adoptar más de un arquetipo y recopilar rasgos de clase alternativos adicionales, pero ninguno de los rasgos de clase alternativos puede reemplazar o alterar el mismo rasgo de clase de la clase básica por otro rasgo de clase alternativo. Por ejemplo, un guerrero no podría ser al mismo tiempo MAestro de armas y Pendenciero, ya que ambos arquetipos reemplazan el rasgo de clase de entrenamiento en armas 1 por algo diferente.\n" +
                        "\n" +
                        "Si un arquetipo reemplaza un rasgo de clase que es parte de una serie de mejoras o adiciones a una aptitud de la clase base (como el entrenamiento en armas de un guerrero o el enemigo predilecto del explorador), la próxima vez que el personaje vaya a obtener esa aptitud, contará como la aptitud de menor nivel que fue reemplazada por el arquetipo. De hecho, todas las aptitudes en esa serie son retrasadas hasta la próxima vez que la clase mejore esa aptitud. Por ejemplo, si un arquetipo reemplaza el bonificador de ataque furtivo de +2d6 del pícaro a nivel 3, cuando alcance el nivel 5 y obtenga un bonificador de ataque furtivo, este no saltará de +1d6 a +3d6, sino que mejorará hasta +2d6, como si hubiera conseguido el aumento de nivel 3. Este ajuste continúa a cada nivel en que mejore su ataque furtivo, hasta nivel 19 en el que tendrá +9d6 en lugar de los +10d6 de un pícaro normal."),
                new ArquetipoInfo("Adaptar personajes existentes", "Los jugadores con personajes existentes deberían hablar con sus DMs acerca de si estas clases alternativas están disponibles en sus partidas y si lo están, si los jugadores pueden modificar retroactivamente sus personajes para adoptarlas. Ya que los rasgos de clase alternativos presentados están diseñados para ser equilibrados con los de las clases básicas, los jugadores que revisen sus personajes no deberían obtener ninguna ventaja especial sobre el resto del grupo. Siempre que el DM esté de acuerdo con ajustar retroactivamente los detalles de los personajes, no debería haber ninguna alteración en futuras aventuras. Normalmente, el mejor momento para que un personaje adopte rasgos de clase alternativos y revisar a fondo su personaje es el periodo de subida de nivel entre aventuras, aunque siempre debería comprobarlo con su DM antes de hacerlo, ya que podría hacer cambios en la campaña para adaptarse mejor al personaje revisado.\n" +
                        "\n" +
                        "Aunque los DMs podrían hacer concesiones para los jugadores que no tienen estos rasgos de clase alternativos cuando crearon a sus personajes, los PJs deberían seguir siendo uno de los elementos más constantes de una campaña. Cambiar y recrear regularmente a los personajes ha demostrado ser problemático para una campaña. Los DMs deberían ser adaptables y permitir a los jugadores que se han aburrido de sus personajes redefinirlos, pero los rasgos de clase alternativos no deberían entenderse como opciones explotables que permitan a los jugadores reconstruir a sus personajes de cualquier forma que vean más ventajosa para un momento determinado. Permitir a los jugadores rehacer a sus personajes con reglas triviales o nuevas puede ser deseable ocasionalmente, pero los DMs no deberían tener la sensación de que sean injustas o romper cualquier regla prohibiendo a los jugadores rehacer sus personajes o prohibir ciertas opciones. Aunque los DMs siempre tratan de ayudar a los jugadores a jugar los personajes que desean, al final saben lo que es mejor para sus campañas.")
        );
      }
  }
}
