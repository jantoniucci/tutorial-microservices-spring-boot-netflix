package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MessageController {

	@Value("${nombre}")
	private String nombre;

	@RequestMapping("/")
	ResponseEntity<Message> home() {
		return new ResponseEntity(new Message("Hello " + nombre), HttpStatus.ACCEPTED);
	}
}
