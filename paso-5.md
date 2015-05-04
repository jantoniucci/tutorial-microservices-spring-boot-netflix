#paso 5 : clientes inteligentes
##un poco de contexto
ya tenemos microservicios autocontenidos, que buscan configuración en archaius, que se registran a eureka y que se les balancea tráfico desde zuul.

teóricamente, cada microservicio debe autocontener el acceso a la persistencia que requieran pero en muchas ocasiones tienen dependencias con otros microservicios a los que tienen que invocar.

estos clientes deben resolver cuestiones como:
* scafolding (feign)
* balanceo en cliente (ribbon)
* circuit-braker (hystrix)
* monitorización de clientes

##crear un cliente con Feign
Feign es un framework de scaffolding para implementar clientes REST declarativamente. 

funciona a partir de anotaciones, es muy customizable, pudiendo definir diversos encoders y decoders para el formateo de intercambio (json, xml, yaml, etc).

Entre sus ventajas más relevantes: 
* Auto-magia 
* Uso de “converters” de  Spring para codificar y descodificar el formato de intercambio
* Compatibilidad con anotaciones de Spring MVC
* Bien integrado con Ribbon y Eureka

el cliente que construiremos invocará a los microservicios helloworld y greetings utilizando feign. creamos un proyecto spring-boot con las siguientes dependencias:
```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.netflix.feign</groupId>
      <artifactId>feign-core</artifactId>
      <version>${feign.version}</version>
    </dependency>
```

el cliente se crea a partir de una interfaz anotadada como FeignClient y cuyos métodos indican los end-points a utilizar:

```java
@FeignClient("zuulserver")
public interface MicroservicesClient {

  @RequestMapping(method = RequestMethod.GET, value = "/helloworld")
  public Message helloworld();
  
  @RequestMapping(method = RequestMethod.GET, value = "/greetings")
  public Message greetings();
  
}
```
zuul entonces derivará estas invocaciones a los microservicios correspondientes

y para que este cliente sea encontrado debemos anotar la clase principal:
```java
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@FeignClientScan
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
}
```
este cliente lo expondremos a través de un controlador específico:
```java
@RestController
public class MessageController {

  @Autowired
  private MicroservicesClient client;

  @RequestMapping("/")
  ResponseEntity<Message> home() {
    return new ResponseEntity(new Message(client.helloworld().getMessage() + " - " + client.greetings().getMessage() ), HttpStatus.ACCEPTED);
  }
}
```
##añadirle balanceo en cliente con ribbon
Ribbon es un balanceado de carga en cliente para los protocolos de transportes más conocidos (http, tcp, udp). 

se le pueden definir algoritmos de balance de carga “pluggables”, lo que nos da la posibilidad de crear nuestro propio protocolo de carga. Por defecto proporciona los algoritmos más conocidos: Round robin, “best available”, random y response time based. 

soporta listas estáticas de nombres de servicios muy adecuadas para usar con el servidor Eureka.

por último, actualizamos el application.yml para incorporar la configuración del zuulserver:
```yml
zuulserver:
  ribbon:
    listOfServers: localhost:8080
```
si lanzamos el servidor y abrimos http://localhost:9001/ veremos el mensaje resultante de las invocaciones a ambos microservicios:
```json
{
message: "Hello JavierA - Buen dia"
}
```
##añadirle circuit-braker con hystrix
las responsabilidades de un circuit-braker buscan mejorar el comportamiento de los clientes ante fallos de los servicios, las comunicaciones, etc. y mejorar el comportamiento de los servicios ante fallos en cascada, monitorización y alertas. 

Entre sus principales funciones:
 * dotar al cliente de fail-fast y fall-back
 * evitar el over flooding del servicio
 * añadir al cliente capacidades de monitorización y logs
 * ver http://martinfowler.com/bliki/CircuitBreaker.html , http://www.lordofthejars.com/2014/09/defend-your-application-with-hystrix.html , https://github.com/Netflix/Hystrix/wiki y para los más curiosos https://github.com/Netflix/Hystrix/wiki/How-it-Works

primero debemos añadir la dependencia:
```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>

```
luego creamos un nuevo cliente que envuelva al anterior y que utilice la anotación @HystrixCommand para indicar los métodos que serán protegidos con Hystrix. en dicha anotaciones aprovechamos para configurar el método alternativo al que se invocará cuando el cliente no funcione.
```java
@Component
public class MicroservicesHystrixClient {

  @Autowired
  private MicroservicesClient client;
  
  @HystrixCommand(fallbackMethod="helloworldFallback")
  public Message helloworld() {
    return client.helloworld();
  };
  
  @HystrixCommand(fallbackMethod="greetingsFallback")
  public Message greetings() {
    return client.greetings();
  };
  
  public Message helloworldFallback() {
    return new Message("Hystrix saluda al mundo");
  };
  
  public Message greetingsFallback() {
    return new Message("que tengas un buen día desde Hystrix");
  };
    
}
```
en el MessageController cambiamos el cliente para usar el nuevo :
```java
  @Autowired
  private MicroservicesHystrixClient client;
```
y finalmente incluimos la anotación ```@EnableCircuitBreaker``` en la clase principal.

si desconectamos / apagamos los servicios e invocamos al cliente http://localhost:9001/ obtendremos:
```json
{
message: "Hystrix saluda al mundo - que tengas un buen día desde Hystrix"
}
```
###monitorizando clientes con hystrix dashboard
hystrix dispone de una consola de monitorización que podemos instanciar mediante una aplicación spring-boot con las siguientes dependencias:
```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
    </dependency>
```
la clase principal de dicha aplicación debe contener la anotación @EnableHystrixDashboard
```java
@Configuration
@EnableAutoConfiguration
@EnableHystrixDashboard
@ComponentScan
public class HystrixDashboardApp 
{
    public static void main( String[] args )
    {
      new SpringApplicationBuilder(HystrixDashboardApp.class).web(true).run(args);
    }
}
```
en el ```application.yml``` añadimos:
```yml
server:
  port: 9002
spring:
  application:
    name:hystrixdashboard
  config:
    name:hystrixdashboard
security:
  ignored:true
```

arrancamos la aplicación con ```mvn spring-boot:run``` y cuando esté iniciada lanzamos un navegador con la url http://localhost:9002/hystrix para visualizar la consola

en la consola, debemos configurar los servicios a monitorizar indicando la url de stream http://localhost:9001/hystrix.stream

###clusterizando la monitorización hystrix con turbine
tubrine nos permite monitorizar todos los clientes desde un mismo dashboard

turbine usa eureka para descubrir los servicios registrados

para utilizaro, comenamos añadiendo la dependencia en el ```pom.xml```:
```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-netflix-turbine</artifactId>
    </dependency>
```
en la aplicación principal añadimos la anotación ```@EnableTurbine```:
```java
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@FeignClientScan
@EnableCircuitBreaker
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
en el ```application.yml``` añadimos:
```yml
turbine:
  aggregator:
    clusterConfig: MICROSERVICESCLIENT
  appConfig: microservicesclient
````
donde :
* turbine.appConfig es una lista de servicios Eureka que deben exponer su monitorización.
* turbine.aggregator.clusterConfig se usa para agrupar los servicios expuestos en clusters. Debe estar en mayúsculas y coincidir con el nombre de los servicios expuestos en appConfig, tal y como explica en https://github.com/Netflix/Turbine/wiki/Configuration-%281.x%29#important-note .

para poder ver la monitorización completa de un cluster utiliamos en la consola http://localhost:9002/hystrix la url http://localhost:9002/turbine.stream?cluster=MICROSERVICESCLIENT que genera el enlace http://localhost:9002/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A9002%2Fturbine.stream%3Fcluster%3DMICROSERVICESCLIENT
