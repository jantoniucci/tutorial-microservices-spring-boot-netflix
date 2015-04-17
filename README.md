# tutorial sobre microservicios utilizando spring-boot y arquitectura de netflix

este tutorial consiste en breves explicaciones, muchos enlaces y código de ejemplo sobre el que experimentar. está basado en la documentación oficial de spring cloud y netfilx oss condimentado con otros contenidos que me parecieron oportunos.

```sh
$ git clone http://github.com/jantoniucci/....git
```
el tuturial está organizado en "pasos" que van acompañados de tags en este repositorio. comenzamos accediendo al primer paso con el comando:
```sh
$ git checkout paso-1
```

cada paso incluirá un paso-n.md con el contenido del tutorial y el código actualizado

para ver los cambios entre pasos, puedes utilizar:
```sh
$ git diff paso-1 paso-2
```
seguramente con un [visualGit](https://github.com/pvginkel/VisualGit) o [sourceTree](http://www.sourcetreeapp.com/) lo verás mejor.

NOTA: este tutorial está dirigido a java developers con experiencia en desarrollo de aplicaciones mvc utilizando el stack tecnológico de spring. 

## agenda
 - paso 1 : mi primer microservicio spring-boot 
   - qué es un microservicio ?
   - qué es spring boot
   - hellow (micro) world
 - paso 2 : comenzando con netflix oss
    - la problemática de los microservicios
    - qué es netflix oss ?
    - el primer servicio netflix: archaius
    - actualizamos el microservicio para usar configuración distribuida
    - cambiar la configuración en caliente a un microservicio
    - cambiar la configuración en caliente en el repo y los microservicios
 - paso 3 : registro y localización de microservicios
    - registro, localización y eureka!
    - instanciando un servidor
    - añadiendo el cliente al microservicio
    - la consola de eureka
 - paso 4 : gestionando peticiones a microservicios
    - enrutado, balanceo y otros malabares
    - invocando a zuul
    - algunas pruebas
    - ...y otras pruebas más
 - paso 5 : clientes inteligentes
    - un poco de contexto
    - crear un cliente con Feign
    - añadirle balanceo en cliente con ribbon
    - añadirle circuit-braker con hystrix
      -  monitorizando clientes con hystrix dashboard
      - clusterizando la monitorización hystrix con turbine
 - paso 6 : comunicando microservicios
    -  un bus para comunicarles a todos
    -  instalando rabbitmq
    -  actualizar los microservicios
    -  enviar mensajes
 - paso 7 : TBD

Te gustan estos temas? Entonces tienes que conocer lo que hacemos en [Adesis Netlife](http://www.adesis.com).

Te gusta este tutorial? Márcalo como favorito y twitealo al mundo!

## Contacto
 * [javier.antoniucci@gmail.com](mailto:javier.antoniucci@gmail.com)
 * http://www.adesis.com
 
## Contribuciones
 * Preguntas, erratas, solicitudes: escribir en la sección Issues del repo
 * Correcciones, mejoras, añadidos : enviar pull-requests
 * Propuestas, colaboraciones en otros tutoriales, etc: javier.antoniucci@gmail.com

## Licencia
[Beerware Software Licence](http://en.wikipedia.org/wiki/Beerware). Beerware es un término de licencia de software otorgado bajo términos muy libres. Provee al usuario final el derecho a un programa particular (y hacer lo que quiera con el código fuente). Si el usuario considera el software útil, se le exhorta a comprarle al autor una cerveza "para devolver el favor".
