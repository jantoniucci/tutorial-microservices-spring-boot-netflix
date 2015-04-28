package demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaServer
@EnableAutoConfiguration
public class EurekaServiceApplication {

    public static void main(String[] args) {
    	new SpringApplicationBuilder(EurekaServiceApplication.class).web(true).run(args);
    }
}
