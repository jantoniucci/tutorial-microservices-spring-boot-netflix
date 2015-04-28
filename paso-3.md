#paso 3 : registro y localización de microservicios
## registro, localización y eureka!
esta agilidad de lanzar y eliminar microservicios en IPs y puertos diferentes es muy ágil pero tiene un precio: encontrarlos . el sistema eureka viene a gestionar los registros de los microservicios y las consultas de sus clientes intentando localizarlos.

Eureka es un servicio basado en servicios REST ideado para balancear carga y proporcionar acciones “failover” de los servicios que gestiona. Se comenzó a usar en el AWS de amazon, para después ser incluido en la plataforma netflix.
## instanciando un servidor
Creamos un proyecto spring boot (podemos copiar el pom.xml anterior, archaius) y añadimos la siguiente dependencia
```xml
  <parent>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-parent</artifactId>
    <version>1.0.0.RELEASE</version>
  </parent>
    ...
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>
  </dependencies>
    ... 
  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

```
Luego crear una aplicación:
```java
@Configuration
@EnableEurekaServer
@EnableAutoConfiguration
public class EurekaServiceApplication {

    public static void main(String[] args) {
      new SpringApplicationBuilder(EurekaServiceApplication.class).web(true).run(args);
    }
}
```
Y finalmente, incorporar la configuración en el ```application.yml``` :
```yml
server:
  port: 8761
spring:
  application:
      name:eureka
  cloud:
    config:
      enabled: false
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```
Ahora ya podemos inciar el servidor con:
```sh
$ mvn spring-boot:run
```
y probarlo accediendo a su consola en http://localhost:8761

## añadiendo el cliente al microservicio
copiamos el proyecto ```microservicio-helloworld``` a un nuevo proyecto ```microservicio-greetings``` renombrando el artifactId en el pom.xml y añadiéndole la dependencia:
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```
y actualizando la clase principal:
```java
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
el application.yml debe configurar el puerto 9000 y la url del servidor de eureka:
```yml
server:
  port: 9100
spring:
  application:
    name: greetings
  cloud:
    config:
      enabled: false
endpoints:
  shutdown:
    enabled: true
  restart:
    enabled: true
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

```
y modificamos el controller para que retorne "buenos dias"...
también vamos a modificar el proyecto hello-world
 - en el pom.xml el spring-cloud-starter-eureka
 - en el DemoApplication el @EnableDiscoveryClient
 - en el application.xml el eureka:client:serviceUrl:defaultZone:...

## la consola de eureka
en la consola de eureka http://localhost:8761/ veremos nuestros servicios en la sección "Instances currently registered with Eureka"