package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	@Value("${server.port}")
	private int serverPort = 0;
	
	@RequestMapping("/")
	ResponseEntity<Message> home() {
		System.out.println("Greetings "+ serverPort);
		return new ResponseEntity(new Message("Buen dia"), HttpStatus.ACCEPTED);
	}
}
