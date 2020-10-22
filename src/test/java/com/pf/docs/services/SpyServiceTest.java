package com.pf.docs.services;

import com.pf.docs.PathFinder;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class SpyServiceTest {
  private final String HOME = "/index.php";
  private final String URL_BASE = "https://www.rolroyce.com/rol/DDP";

  @Test
  public void shouldPassWhenCategoriesArePresent() throws IOException {
    // spyService.cagetories().forEach(System.out::println);
    //    spyService.glosary().forEach(System.out::println);
    //        PathFinder.razasPF().get(0).card(true).stream().forEach(System.out::println);
    PathFinder.categories().stream().forEach(System.out::println);
    PathFinder.razasPF().stream().forEach(System.out::println);
    System.out.println(PathFinder.clasesMatriz().getDescription());
  }
}
