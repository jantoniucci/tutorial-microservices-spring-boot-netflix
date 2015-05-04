package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
