#Conclusiones
##Lo que hemos aprendido
La coordinación de microservicios distribuidos conlleva implementar patrones como:
* Distributed/versioned configuration
* Service registration and discovery
* Routing
* Service-to-service calls
* Load balancing
* Circuit Breaker
* Asynchronous
* Distributed messaging

Netflix los implementa mediante los siguientes proyectos que Spring recubre habilmente:
* Eureka (registro y autoreconocimiento de servicios)
* Hystrix & Turbine (control de ruptura de comunicación con los servicios)
* Ribbon (balance de carga)
* Feign (llamadas servicio a servicio)
* Zuul (enrutado)
* Archaius (configuración distribuida)

además tendríamos que revisar:

* Curator (clustering)
* Asgaard (deployments y gestión del cloud)

e incluso ampliar nuestros conocimiento sobre Zuul ya que Netflix lo utiliza además para:
* Authentication
* Insights
* Stress Testing
* Canary Testing
* Dynamic Routing
* Service Migration
* Load Shedding
* Security
* Static Response handling
* Active/Active traffic management

##IMHO
* spring recubre fenomenal y simplifica muchísimo la utilización de Netflix
* spring debería abstraerse de Netflix y comenzar a implementar integraciones con alternativas como Consul, Vautl, zookeeper, etc.
* los proyectos de Netflix deberían abstraerse más de amazon aws
* el modelo de netflix se hace explícito a los microservicios
    * ellos saben de netflix y se crean dependencias con ellos
    * otras tecnologías como kubernetes son transparentes al microservicio
* hay mucha "auto magia" con sus ventajas y desventajas
* la tecnología de netflix está en producción, la de spring está saliendo del horno
 * la documentación es escasa
 * el soporte informal (stackoverflow, etc) también es escasa
 * el código de ambos es bastante limpio
* no se aborda el aprovisionamiento y despliegue de microservicios (si en kubernetes)
* las herramientas de monitorización de netflix están muy bien
* hay que seguir investigando...