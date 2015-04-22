#paso 1 : mi primer microservicio spring-boot 
##qué es un microservicio ?
hay muchísima literatura sobre el tema (incluso quizás demasiada) así que vamos a resumir en que un microservicio como:
> Es una técnica de diseño de sistemas como una colección de pequeños servicios, cada uno ejecutándose en su propio proceso, comunicándose con protocolos ligeros como HTTP/JSon y gestionados desde fuera.

algunas características:

* componentización, 
* organización basada en capacidades funcionales de negocio, 
* gestión descentralizada, 
* endpoints inteligentes, 
* gestión de datos descentralizados, 
* automatización de infraestructura, 

lecturas recomendadas:
* http://martinfowler.com/articles/microservices.html
* http://microservices.io/patterns/microservices.html
* http://en.wikipedia.org/wiki/Microservices

esta técnica de diseño tiene problemáticas específicas:
- ¿cómo configurar los servicios centralizadamente?
- ¿cómo gestionar el tráfico hacia los servicios?
- ¿cómo expongo y comunico los servicios entre ellos?
- ¿cómo monitorizo los servicios?
- ¿cómo detecto que un servicio falla y evito enviarle tráfico?
- ¿cómo comunico los servicios entre ellos?
- ¿cómo envío mensajes a los servicios? (o a los de un tipo) 

##qué es spring boot
spring boot es un automatismo para simplificar la creación de proyectos, desarrollo, empaquetamiento y ejecución de aplicaciones web basadas en el stack tecnológico de spring y maven/gradle y empaquetando en un "fat-JAR" el contenedor de servlets preferido (jetty, tomcat, undertow, etc). dicho de otra forma, han aprendido de los bootstraps de javascript y lo han traido al mundo java elegantemente.

spring boot simplifica tremendamente la configuración a partir de los scanners (busca los beans del contexto mediante sus anotaciones), el fallback de configuraciones (desde parámetros en la línea de comando hasta propiedades del sistema operativo), [los actuators](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready) (endpoints "plugables" para añadir características de administración como shutdown, ver configuración, salud, etc). Los más relevantes:
 * /autoconfig : Displays an auto-configuration report showing all auto-configuration candidates and the reason why they ‘were’ or ‘were not’ applied.
 * /beans : Displays a complete list of all the Spring Beans in your application.
 * /configprops : Displays a collated list of all @ConfigurationProperties.
 * /dump : Performs a thread dump.
 * /env : Exposes properties from Spring’s ConfigurableEnvironment.
 * /health : Shows application health information (a simple ‘status’ when accessed over an unauthenticated connection or full message details when authenticated).
 * /info : Displays arbitrary application info.
 * /metrics : Shows ‘metrics’ information for the current application.
 * /mappings : Displays a collated list of all @RequestMapping paths.
 * /shutdown : Allows the application to be gracefully shutdown (not enabled by default).
 * /trace : Displays trace information (by default the last few HTTP requests).

nuevamente, hay mucha literatura sobre el tema y recomiendo:
* su documentación oficial en http://projects.spring.io/spring-boot/#quick-start
* http://spring.io/guides/tutorials/bookmarks/
* http://www.infoq.com/articles/microframeworks1-spring-boot

##hellow (micro) world

con spring boot vamos a crear microservicios de la forma más sencilla posible: 
  1. Abre http://start.spring.io/ , 
  2. personaliza el formulario (no selecciones ningún check aún) 
  3. con el botón "Generate project" descargarás un zip con el pom.xml y fuentes.
  4. descomprímelo
  5. Crear una clase MessageController con un controlador para GET / :
```java
@RestController
@EnableAutoConfiguration
public class MessageController {

  @RequestMapping("/")
  ResponseEntity<Message> home() {
    return new ResponseEntity( new Message("Hello World!") , HttpStatus.ACCEPTED);
  }
}
```
  6. crear la classe ```Message``` con un atributo ```message``` y getters/setters (o mejor usar [lombok](http://projectlombok.org/))
  7. además añadiremos la dependencia de los actuators para poder jugar con ellos
```xml
    <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-actuator</artifactId>
      </dependency>
```
  8. algunos actuators requieren configuración, así que editaremos el ```/src/main/resources/application.properties``` donde también podríamos setear el ```server.port``` si queremos asignarle un valor diferente al 8080 por defecto o cambiar el ```management.port``` para los actuators
```properties
endpoints.shutdown.enabled=true
```
  9. desde línea de comandos ejecutar spring-boot
```sh
$ mvn spring-boot:run
```
o también podemos hacer un:
```sh
$ mvn install
$ java -jar target/demo-0.0.1-SNAPSHOT.jar
```
  9. desde un navegador, acceder a http://localhost:8080
  10. cuando queramos terminar, podemos hacer un control-c o aprovechar el actuator que hemos activado y hacer
```sh
curl -XPOST http://localhost:8080/shutdown
```


