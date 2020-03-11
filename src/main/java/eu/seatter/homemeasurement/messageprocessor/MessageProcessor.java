package eu.seatter.homemeasurement.messageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MessageProcessor {
    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(MessageProcessor.class, args);
    }
//    public static void main(String... args) throws InterruptedException {
//        ApplicationContext ctx = SpringApplication.run(MessageProcessor.class, args);
//        final CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                closeLatch.countDown();
//            }
//        });
//        closeLatch.await();
//    }
//
//    @Bean
//    public CountDownLatch closeLatch() {
//        return new CountDownLatch(1);
//    }
//
//    @Autowired
//    MessagingService rabbitMQService;
//
//    @Override
//    public void run(String... strings) {
//        rabbitMQService.readMeasurement();
//    }
}