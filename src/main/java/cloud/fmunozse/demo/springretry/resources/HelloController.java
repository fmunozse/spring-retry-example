package cloud.fmunozse.demo.springretry.resources;

import cloud.fmunozse.demo.springretry.services.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RestController
public class HelloController {

    Logger logger = LoggerFactory.getLogger(HelloController.class);

    HelloService helloService;

    public HelloController(HelloService helloService, RetryTemplate retryTemplate) {
        this.helloService = helloService;
    }

    @RequestMapping("/")
    public String index() {
        String uuid = UUID.randomUUID().toString();
        logger.info("\nRequest {} ", uuid);
        return "Greetings from Spring Boot! - call service: " + helloService.callRetriableExternalSystem(uuid);
    }


}
