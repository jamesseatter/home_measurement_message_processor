package eu.seatter.homemeasurement.messageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MessageProcessor {
    public static void main(String... args) {
        @SuppressWarnings({"java:S1854","java:S1481"})
        ConfigurableApplicationContext context = SpringApplication.run(MessageProcessor.class, args);
    }
}