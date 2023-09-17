# learn-spring-boot

In diesem Repository sind alle Grundlagen enthalten um mit Spring Webanwendungen zu erstellen.

## 1. Was ist Spring boot?
Spring Boot ein Framework, das die Entwicklung von Java-Anwendungen, insbesondere Webanwendungen, vereinfacht, indem es Konventionen und Standards verwendet, um den Entwicklungsprozess zu beschleunigen. 
Es ist ideal für die Erstellung von Microservices und bietet viele integrierte Tools und eine starke Community-Unterstützung.

## 2. Projekt-Erstellung mit dem Spring Initializr und Konfiguration

Downloads:
- [IntelliJ Download](https://www.jetbrains.com/idea/download/?section=windows) <- Achtung, Community Version auswählen!
- [JDK Download](https://www.oracle.com/java/technologies/downloads/#jdk20-windows)
- [Apache Maven](https://maven.apache.org/download.cgi)

Die beste Methode ein Spring Boot Projekt zu erstellen ist über den Spring initializr. Also bitte öffnen: [Spring Initializr](https://start.spring.io/)

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

### Projekt-Struktur & pom.xml

Die Projekt-Struktur eines Spring Boot Projekts sieht standardmäßig so aus:

![Screenshot](structure.png)

In dem obigen Beispiel ist MySpringBootApplication die Main-Klasse, application.properties eine Konfigurationsdatei und die pom.xml die Steuerungs-Datei für Maven.

Wir nutzen diese Gelegenheit um in der pom.xml einige Dependencies einzufügen, die wir später brauchen, ich zeige diesmal noch welche wir benötigen. Solltest du aber irgendwann selbstständig welche suchen, so kannst du auf

**application.properties**
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<version>3.1.3</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
			<version>3.1.3</version>
		</dependency>
		
```

<https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html> 

oder  <https://mvnrepository.com/> 
 selbst nachschauen.

### Application.properties

Wie schon erwähnt ist die application.properties im resources-Ordner die **Konfigurationsdatei von Spring.**
Hier können wir z.B. den Localhost-Port ändern oder die DB-Daten angeben.


**Application.properties:**

```
# General Configuration
server.port=8888

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.username=sa
spring.datasource.password=geheim
```

In diesem Beispiel wird also der Default LocalHost-Port von 8080 auf 8888 geändert, eine h2-inMem-Datase mit Login-Details mitgeteilt.

Da wir in der pom.xml die spring-boot-starter-data-jpa hinzugefügt haben, haben wir die h2-Console enabled. 
Diese können wir unter
<localhost:8888/h2-console> aufrufen solange unsere Application "rennt". 

**Führe im Terminal den Befehl mvn spring-boot:run um die Anwendung zu starten aus und öffne die H2-console.**

### H2-Console

![h2Console](h2console.png)

Passe folgende Felder wie in der application.properties an:
- JDBC Url: jdbc:h2:mem:testdb
- Password: geheim

Teste anschließend die Connection, funktioniert diese: connecte.

Jetzt ist die Datenbank natürlich noch leer, jetzt kommt also der Part der Spaß macht.

## 3. REST: Entities, Controller und Routing

### Entity

Eine leere Datenbank macht keinen Spaß, also erstellen wir erstmal einen Table mit Spring.

Wir erstellen hierfür unter src/main/java/com/learnspringboot/zm erstmal eine neue Package **Entity** mit der Klasse **Customer**.

**Customer.java:**

````
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nachname;
    private String vorname;

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
}
````

Es ist also erstmal nichts anderes als ein POJO mit der Annotation @Entity.

@Id in Verbindung mit @GeneratedValue bedeutet hier nur : Das Field ist eine selbstinkrementierende ID.

Jetzt aber zur Spring Magic: Durch Anlegen eben eines simplen POJOs mit Getter und Setter erstellt Hibernate beim Ausführen der Anwendung automatisch einen Table in unserer Datenbank.

**Achtung: Die Getter und Setter sind wichtig, denn so führt Spring Boot, bzw Hibernate später die CRUD-Operationen / DB-Operationen aus.**

**Starte die Anwendung neu, Öffne die h2-Console und überprüfe ob der Table Customer erstellt wurde.**

### Repository, Service, Controller

Erstelle 3 neue Packages.
- Package Repository mit dem Interface CustomerRepository
- Package Service mit der Klasse CustomerService
- Package Controller mit der Klasse CustomerController.

#### **CustomerRepository.java**
````
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
````

@Repository gibt an, dass es sich um ein Repository handelt (und unter anderem auch wie ein Component behandelt wird, aber dazu später mehr)
Das CustomerRepository erbt nur von dem Interface JpaRepository. Dies gibt uns Zugriff auf mehrere CRUD-Methoden:
1. Speichern oder Updaten
- save(Entity Entity): Methode zum **SPEICHERN UND UPDATEN** einer Entity in der DB 
2. Auslesen
- findById(ID id): Ruft eine Entity anhand ihrer ID ab
- findall(): Ruft alle Entities aus der DB ab
- count(): Zählt die Entities
- existsById(ID id): Prüft ob die Entity mit der übergebenen ID in der DB existiert
3. Löschen
- deleteById(ID id): Löscht die Entity mit der übergebenen ID
- delete (Entity entity): Löscht eine bestimmte Entity aus der DB
- deleteAll(): Löscht alle Entities aus der DB
4. Benutzerdefinierte Abfragen
Anhand des Namens kann man benutzerdefinierte Queries erstellen
- List<Customer> findByVorname(String vorname): Gibt alle Records mit dem entsrechendem Vornamen zurück
- List<Customer> findByVornameAndNachname(String vorname, String nachname): Gibt alle Records mit dem entsprechenden Vor- und Nachnamen zurück
- List<Customer> findByNachnameOrderByVornameDesc(String nachname): Gibt alle Records mit dem entsprechendem Nachnamen mit der Sortierung absteigend nach dem Vornamen zurück
- @Query("SELECT * FROM customer WHERE vorname = :vorname", nativeQuery = true)

  @Query(value = SELECT * FROM customer WHERE vorname = :vorname", nativeQuery = true)

  List<Customer> findCustomersByVorname(@Param("vorname") String vorname); Durch nativeQuery = true wird eine native SQL-Abfrage verwendet, Gibt alle Kunden mit entsprechendem Vornamen zurück

- @Query("SELECT c from CUSTOMER c where c.nachname LIKE %:nachname%")

  List<Customer> findCustomersWithPartialNachname(@Param("nachname") String nachname); >>JQPL-Abfrage, gibt alle Kunden mit bestimmten String im Nachnamen zurück

#### **CustomerService.java**

````
@Service
public class CustomerService {

    //Diese Klasse ist nur zu Beispielzwecken gedacht, eigentlich ist sie überflüssig, da uns JpaRepository genau diese Methoden schon zur Verfügung stellt

    private final CustomerRepository customerRepository;

 
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public long countCustomers() {
        return customerRepository.count();
    }

    public void deleteCustomerById() {

    }
}
````
Hier taucht unser eben angelegtes CustomerRepository wieder auf. Die @Autowired-Annotation sgt Spring Boot wieder: Such in unserem Projekt nach den nötigen Beans (hier CustomerRepository).
Übrigens: Die Dependency Injection durch den Constructor ist die sauberste Variante und sollte wo möglich immer genutzt werden.

Wir haben hier also nichts anderes gemacht alt Methoden angelegt, die wir gleich im Controller verwenden werden. 

**Merke: der Service enthält immer die Business Logik.**

#### **CustomerController.java**

````
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;


    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fillCustomers")
    public void fillCustomers() {
        //4 Customers erstellen und in DB einfügen
        Customer customer1 = new Customer();
        customer1.setVorname("Marcel");
        customer1.setNachname("Zimmermann");
        customerService.saveCustomer(customer1);
        System.out.println("Customer "+ customer1.getVorname() + " " + customer1.getNachname() +" angelegt!");

        Customer customer2 = new Customer();
        customer2.setVorname("Ehsan");
        customer2.setNachname("Moradi");
        customerService.saveCustomer(customer2);
        System.out.println("Customer "+ customer2.getVorname() + " " + customer2.getNachname() +" angelegt!");

        Customer customer3 = new Customer();
        customer3.setVorname("Thomas");
        customer3.setNachname("Schäfer");
        customerService.saveCustomer(customer3);
        System.out.println("Customer "+ customer3.getVorname() + " " + customer3.getNachname() +" angelegt!");

        Customer customer4 = new Customer();
        customer4.setVorname("Sven");
        customer4.setNachname("Domberg");
        customerService.saveCustomer(customer4);
        System.out.println("Customer "+ customer4.getVorname() + " " + customer4.getNachname() +" angelegt!");

        System.out.println("-----------------------------------------------------------");
        long customerCount = customerService.countCustomers();
        System.out.println("Aktuell gibt es also " + customerCount + " Customers.");
        System.out.println("-----------------------------------------------------------");

        System.out.println("-----------------------------------------------------------");
        customerService.deleteCustomerById(1L);
        System.out.println("Oha, ein Customer wurde deleted! Kataschtroph");
        System.out.println("-----------------------------------------------------------");

        System.out.println("-----------------------------------------------------------");
        customerCount = customerService.countCustomers();
        System.out.println("Jetzt gibt es also nur noch "+ customerCount + " Customers!");
        System.out.println("-----------------------------------------------------------");
    }
}

````
@RequestMapping("/customers") bedeutet, dass alles in dieser Klasse über die Standardurl + /customers passiert.
In unserem Fall also: <https://localhost:8888/customers>

Wie du siehst ist eine ähnliche Notation aber nochmal über der fillCustomers-Methode, das bedeutet die Url wird nochmal erweitert.
Hier also: <https://localhost:8888/customers/fillCustomers

Jetzt können wir den Spaß testen.
- Rufen wir erstmal einfach nur die Console auf und loggen uns ein, sind unsere Tabellen immernoch leer.
- Rufen wir <https://localhost:8888/customers/fillCustomers> auf, sehen wir schon auf unserer Commandline was passiert ist, checken wir jetzt nochmal die Tabelle in der h2-Console sehen wir die Customer-Einträge in der Tabelle.

## 4. Aufruf von HTML-Seiten mit Thymeleaf

Thymeleaf ist ein Template-Framework, welches uns mit Hilfe von Placeholdern einfach HTML-Seiten füllen lässt. 
Es kann auch noch mehr als das, man kann es sich in etwa wie objektorientiertes HTML vorstellen, dazu aber später mehr.
Wir wollen unseren Code so anpassen, dass unsere Kunden nicht nur in unserer Command line angezeigt werden, sondern im Browser.

Folgende Anpassungen sind nötig:

**application.properties**

Zuerst müssen wir eine weitere Dependency hinzufügen:
````
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
			<version>3.1.3</version>
		</dependency>
````

Außerdem müssen wir unter main/resources/templates eine customers.html erstellen: 

**customers.html**

````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>List of customers</title>
</head>
<body>
<h1>customer list</h1>
<table>
    <tr>
        <th>ID</th>
        <th>Vorname</th>
        <th>Nachname</th>
    </tr>
    <tr th:each="customer : ${customers}">
        <td th:text="${customer.id}"></td>
        <td th:text="${customer.vorname}"></td>
        <td th:text="${customer.nachname}"></td>
    </tr>
</table>
</body>
</html>
````

**CustomerService.java**

````
public List<Customer> getAllCustomers() {
// Diese Methode gibt alle Kunden aus der Datenbank zurück
return customerRepository.findAll();
}
````

**CustomerController.java**

Hier müssen wir oben die Annotation von @RestController auf @Controller ändern, dieser Unterschied ist wichtig.
@Restcontroller wird normalerweise für RESTful Web Service verwendet und gibt Dateien im JSON oder XML-Format zurück, nicht HTML-Seiten. Der Rückgabewert würde also als reiner Text interpretiert werden.
@Controller hingegen wird verwendet um Websites, normalerweise HTML-Seiten zu rendern. 

````
    @GetMapping("/showCustomers")
    public String getAllCustomers(Model model) {
        // Liste aller Kunden aus der Datenbank abrufen
        List<Customer> customers = customerService.getAllCustomers();

        // Die Liste an das Model binden, um sie in der HTML-Seite anzuzeigen
        model.addAttribute("customers", customers);

        // Die HTML-Seite zum Anzeigen der Kundenliste anzeigen (customers.html)
        return "customers";
    }
```` 

Rufen wir jetzt also erst <https://localhost:8888/customers/fillCustomers> und anschließend <https://localhost:8888/customers/showCustomers> auf, werden uns im Browser alle Customers angezeigt.




## Help & Useful Information

### Fehler nach Initialisierung des Projekts?

1. Sind alle relevanten Sachen installiert? JDK, Maven, etc.?
2. Den Target-Ordner löschen, mvn clean install ausführen

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

- mvn spring-boot: run: Führt die Anwendung mit Berücksichtigung der application.properties aus. Setzt die Dependency des spring-boot-maven-plugin in der pom.xml voraus.
- mvn clean: Löscht alle erstellten Artefakte und Temp files im Projekt, "bereinigt" das Projekt.
- mvn install: Kompiliert das Projekt, erstellt das Artefakt und kopiert es in das lokale Repository von Maven (~/.m2/repository). Andere Projekte können dann auf dieses Artefakt als Abhängigkeit zugreifen
- mvn clean install: Kombination aus clean und install(wer hätte es gedacht). Löscht alle vorher erstellten Artefakte und kopiert das neu erstellte in das lokale Repository.
- mvn compile: Kompiliert den Java-Code, grundlegender Schritt um Java-Code in Bytecode zu übersetzen
- mvn dependency tree: Zeigt die Abhängigkeitsstruktur als Tree. Hilft um zu verstehen welche Bibliotheken / Versionen verwendet werden
- mvn test: Führt die Tests aus. Verwendet in der Regel JUnit / TestNG
- mvn clean test: Kombination aus clean und test. Bereinigt das Projekt und führt die Tests aus.
- mvn dependency:resolve: Löst Dependencies auf und checkt ob diese im Repository vorhanden sind. Guter Befehl um Dependencies zu downloaden ohne das gesamte Projekt zu erstellen.

### Error mit Test-Class: MOJO not found

Quick fix: Einfach nur die ZMApplicationTests anpassen:

````
@SpringBootTest
class ZmApplicationTests {

}
````
