package eu.seatter.homemeasurement.messageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MessageProcessor {
    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessageProcessor.class, args);
    }
}