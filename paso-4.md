#paso 4 : gestionando peticiones a microservicios
##enrutado, balanceo y otros malabares
para que las peticiones de clientes externos e internos lleguen a nuestros microservicios necesitamos que una pieza intermediaria gestione el enrutado. [Zuul](https://github.com/Netflix/zuul) es un enrutador y balanceador de carga que se integra perfectamente con eureka de tal manera que enrutará llamadas a urls con servicios eureka, haciendo transparente a cual de todas las instancias del mismo servicio está enrutando la llamada.

para instanciar a Zuul, creamos una aplicación spring boot y añadimos la dependencia maven de zuul
```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-netflix-zuul-server</artifactId>
      <version>1.0.0.BUILD-SNAPSHOT</version>
    </dependency>
```

incluir la anotación @EnableZuulProxy en la clase principal de la aplicación ZuulServiceApplication.java
```java
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulServiceApplication {
   public static void main( String[] args ){
      SpringApplication.run(ZuulServiceApplication.class, args);
   }
}
```
e indicar en el ```application.yml``` que atenderá el puerto 8080 y las rutas que utilizará
```yml
server:
  port: 8080
zuul:
  routes:
    greetings:
      path: /greetings/**
      serviceId: GREETINGS
    helloworld:
      path: /helloworld/**
      serviceId: HELLOWORLD
```
finalmente, lo lanzamos con un:
```sh
$ mvn spring-boot:run
```
##invocando a zuul
http://localhost:8080/helloworld
http://localhost:8080/greetings
##algunas pruebas
vamos a crear otra instancia de greetings ejecutando un ```mvn install``` y luego un:
```sh
$ java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=9876
```
con el que iniciamos otra instancia en otro puerto

vemos en la consola de eureka que el estado del servicio es UP(2)

si lanzamos peticiones, veremos que se balancean entre ambas instancias

##...y otras pruebas más
quitamos todas las instancias y vemos cómo reacciona zuul
