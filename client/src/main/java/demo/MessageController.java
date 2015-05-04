package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	@Autowired
	private MicroservicesHystrixClient client;

	@RequestMapping("/")
	ResponseEntity<Message> home() {
		return new ResponseEntity(new Message(client.helloworld().getMessage() + " - " + client.greetings().getMessage() ), HttpStatus.ACCEPTED);
	}
}
