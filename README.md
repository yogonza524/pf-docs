[![Maven Artifact](https://img.shields.io/nexus/r/io.github.yogonza524/pf-docs?server=https%3A%2F%2Foss.sonatype.org)](https://mvnrepository.com/artifact/io.github.yogonza524/pf-docs)
![Code size](https://img.shields.io/github/languages/code-size/yogonza524/pf-docs)
# Path Finder Game Docs SDK (Spanish)
Simple API for Path Finder game

## Disclaimer
This development use third party translation. Use it under your own risk. 
We don't take any responsability for your usage

## Requirements
- JDK 8+

## Add to your project
To add as dependency using Maven, you should have at ```pom.xml```:
```xml
<dependency>
  <groupId>io.github.yogonza524</groupId>
  <artifactId>pf-docs</artifactId>
  <version>0.0.2</version>
</dependency>
```

To add as dependency using Gradle, you should have at ```build.gradle```:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.github.yogonza524:pf-docs:0.0.2"
}
```

## Usage
1. List categories
```java
PathFinder.categories().stream().forEach(System.out::println);
```

2. Find all classes of characters
```java
// Show all classes
PathFinder.clasesPF().stream().forEach(System.out::println);
```

3. List the glosary of terms
```java
PathFinder.glosary().stream().forEach(System.out::println);
```

4. Show all races of the game
```java
PathFinder.razasPF().stream().forEach(System.out::println);
```

5. Show card description of a race
```java
PathFinder.razasPF()
    .get(0) // Take first 
    .card(true) // true = text plain; false = raw html
    .stream()
    .forEach(System.out::println);
```

5. Show Matrix of classes
```java
PathFinder.clasesMatriz(); // Get the Matrix classes 
```