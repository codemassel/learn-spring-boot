# learn-spring-boot

In diesem Repository sind alle Grundlagen enthalten um mit Spring Webanwendungen zu erstellen.

## 1. Was ist Spring boot?
Spring Boot ein Framework, das die Entwicklung von Java-Anwendungen, insbesondere Webanwendungen, vereinfacht, indem es Konventionen und Standards verwendet, um den Entwicklungsprozess zu beschleunigen. 
Es ist ideal für die Erstellung von Microservices und bietet viele integrierte Tools und eine starke Community-Unterstützung.

## 2. Projekt-Erstellung mit dem Spring Initializr

Downloads:
- [IntelliJ Download](https://www.jetbrains.com/idea/download/?section=windows) <- Achtung, Community Version auswählen!
- [JDK Download](https://www.oracle.com/java/technologies/downloads/#jdk20-windows)
- [Apache Maven](https://maven.apache.org/download.cgi)

Die beste Methode ein Spring Boot Projekt zu erstellen ist über den [Spring Initializr.](https://start.spring.io/)

### Projektdaten 
Wähle im Spring Initializr folgende Projektdaten:

- Project: Maven
- Language: Java
- Group: com.learn-spring-boot
- Artifact: zm
- Name: zm
- Description: whatever
- Package name: com.learn-spring-boot.zm
- Java: Die aktuell installierte Version

### Dependencies
 Dependencies sind Module von Spring, die man in seine Anwendung als JARs einbinden kann. Diese müssen nicht zwangsläufig hier ausgewählt werden, man kann sie nachher in der pom.xml noch hinzufügen.

Wähle für unser Projekt erstmal nur die folgenden aus:
- Spring Web
- H2 Database

Klicke anschließend auf Generate (Oder Strg + Enter) um das Projekt herunterzuladen, entpacke es und öffne es in der IDE.

## 3. Projekt-Struktur

Die Projekt-Struktur eines Spring Boot Projekts sieht standardmäßig so aus:

![Screenshot](structure.png)

In diese Beispiel ist MySpringBootApplication die Main-Klasse, application.properties eine Konfigurationsdatei und die pom.xml die Steuerungs-Datei für Maven.

Wir nutzen diese Gelegenheit um in der application.properties die inmem-DB einzufügen.









## Help, Tips & Tricks

### Umgebungsvariable für Maven setzen:

Windows:

1. Download von [Apache Maven](https://maven.apache.org/download.cgi)
2. Entpacke in einen beliebeigen Ordner, kopiere den Pfad als Text
3. Rechtsklick auf Windows -> System -> Erweiterte Systemeinstellungen -> Umgebungsvariablen
4. Unter Systemvariablen: Neue anlegen mit 
   - Name: MAVEN_HOME (ja, alles groß)
   - Value: vorher kopierte Pfad
5. Unter Systemvariablen: PATH -> Bearbeiten -> Neu: %MAVEN_HOME%\bin eintragen

Mac:
Aufgeben XD

### Useful Maven Befehle

- Mvn clean install
- 