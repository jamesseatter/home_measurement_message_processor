package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 29/02/2020
 * Time: 08:52
 */
@Service
@Slf4j
public class MessagingService implements CommandLineRunner {
    final MessageProcessorMeasurement measurementMessageProcessor;
    final MessageProcessorMeasurementAlert alertMessageProcessor;
    final MessageProcessorSystemAlert systemAlertMessageProcessor;
    final Environment env;

    private final String MQ_HOST;
    private final int MQ_PORT;
    private final String MQ_VHOST;
    private final String MQ_EXCHANGE_NAME;
    private final String MQ_USERNAME;
    private final String MQ_USERPASSWORD;
    private final String MQ_MEASUREMENT_ENABLED;
    private final String MQ_MEASUREMENT_ALERT_ENABLED;
    private final String MQ_SYSTEM_ALERT_ENABLED;


    public MessagingService(@Value("${RabbitMQService.hostname:localhost}") String hostname,
                            @Value("${RabbitMQService.portnumber:5672}") int portnumber,
                            @Value("${RabbitMQService.vhost:/}") String vhost,
                            @Value("${RabbitMQService.exchangee:}") String exchangename,
                            @Value("${RabbitMQService.username:}") String username,
                            @Value("${RabbitMQService.password:}") String password,
                            @Value("${RabbitMQService.processing.measurement.enabled:false}") String mqMeasurementEnabled,
                            @Value("RabbitMQService.processing.alert.measurement.enabled") String mqMeasurementAlertEnabled,
                            @Value("RabbitMQService.processing.alert.system.enabled") String mqSystemAlertEnabled,
                            MessageProcessorMeasurement measurementMessageProcessor,
                            MessageProcessorMeasurementAlert alertMessageProcessor,
                            MessageProcessorSystemAlert systemAlertMessageProcessor,
                            Environment env) {
        this.MQ_HOST = hostname;
        this.MQ_PORT = portnumber;
        this.MQ_VHOST = vhost;
        this.MQ_EXCHANGE_NAME = exchangename;
        this.MQ_USERNAME = username;
        this.MQ_USERPASSWORD = password;
        this.MQ_MEASUREMENT_ENABLED = mqMeasurementEnabled;
        this.MQ_MEASUREMENT_ALERT_ENABLED = mqMeasurementAlertEnabled;
        this.MQ_SYSTEM_ALERT_ENABLED = mqSystemAlertEnabled;
        this.measurementMessageProcessor = measurementMessageProcessor;
        this.alertMessageProcessor = alertMessageProcessor;
        this.systemAlertMessageProcessor = systemAlertMessageProcessor;
        this.env = env;

        log.debug("Host       :" + MQ_HOST);
        log.debug("Port       :" + MQ_PORT);
        log.debug("VHost      :" + MQ_VHOST);
        log.debug("ExName     :" + MQ_EXCHANGE_NAME);
    }

    @Override
    public void run(String... args) {
        log.info("MQ waiting for measurement messages");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQ_HOST);
        factory.setPort(MQ_PORT);
        factory.setUsername(MQ_USERNAME);
        factory.setPassword(MQ_USERPASSWORD);
        factory.setVirtualHost(MQ_VHOST);

        if(MQ_MEASUREMENT_ENABLED.equals("true")) {
            setupCallbacks(factory, "measurement");
        }
        if(MQ_MEASUREMENT_ALERT_ENABLED.equals("true")) {
            setupCallbacks(factory, "alertmeasurement");
        }
        if(MQ_SYSTEM_ALERT_ENABLED.equals("true")) {
            setupCallbacks(factory,"alertsystem");
        }
        log.info("Initialisation complete");
    }

    private void setupCallbacks(ConnectionFactory factory,String messageType) {
        String MQ_QUEUE_NAME="CHANGE_ME";
        String MQ_ROUTING_KEY="CHANGE_ME";
        switch (messageType) {
            case "measurement" : {
                MQ_QUEUE_NAME = env.getProperty("RabbitMQService.queue.measurement");
                MQ_ROUTING_KEY = env.getProperty("RabbitMQService.routing_key.measurement");
                break;
            }
            case "alertmeasurement" : {
                MQ_QUEUE_NAME = env.getProperty("RabbitMQService.queue.alert.measurement");
                MQ_ROUTING_KEY = env.getProperty("RabbitMQService.routing_key.alert.measurement");
                break;
            }
            case "alertsystem" : {
                MQ_QUEUE_NAME = env.getProperty("RabbitMQService.queue.alert.system");
                MQ_ROUTING_KEY = env.getProperty("RabbitMQService.routing_key.alert.system");
                break;
            }
        }

        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            log.debug("Connecting to MQ Server - " + MQ_QUEUE_NAME + "/" + MQ_VHOST + "/" + MQ_HOST + ":" + MQ_PORT);
            try {
                channel.basicQos(1); //tell channel to send one message at a time.
                //channel.queueDeclarePassive(MQ_QUEUE_NAME);
                channel.queueBind(MQ_QUEUE_NAME, MQ_EXCHANGE_NAME, MQ_ROUTING_KEY);
            } catch (IOException ex) {
                String errorMessage = "ERROR: Failed to connect to MQ Server";
                messageReadFailed(errorMessage);
                //todo Throw an error
                return;
            }
            log.debug("Connected to MQ Server");

//            String finalMQ_ROUTING_KEY = MQ_ROUTING_KEY;
//            String finalMQ_QUEUE_NAME = MQ_QUEUE_NAME;
            switch (messageType) {
                case "measurement": {
                    try {
                        log.debug("Setting up measurement message callback");
                        DeliverCallback callback = defineCallBack(channel, measurementMessageProcessor, MQ_ROUTING_KEY);
                        channel.basicConsume(MQ_QUEUE_NAME, false, callback, consumerTag -> {
                        });
                    } catch (Exception ex) {
                        String errorMessage = "Failed to setup Measurement Callback : " + ex.getMessage();
                        messageReadFailed(errorMessage);
                    }
                    break;
                }
                case "alertmeasurement": {
                    try {
                        log.debug("Setting up measurement alert message callback");
                        DeliverCallback callback = defineCallBack(channel, alertMessageProcessor, MQ_ROUTING_KEY);
                        channel.basicConsume(MQ_QUEUE_NAME, false, callback, consumerTag -> {});
                    } catch (Exception ex) {
                        String errorMessage = "Failed to setup measurement alert Callback : " + ex.getMessage();
                        messageReadFailed(errorMessage);
                    }
                    break;
                }
                case "alertsystem": {
                    try {
                        log.debug("Setting up system message callback");
                        DeliverCallback callback = defineCallBack(channel, systemAlertMessageProcessor, MQ_ROUTING_KEY);
                        channel.basicConsume(MQ_QUEUE_NAME, false, callback, consumerTag -> {
                        });
                    } catch (Exception ex) {
                        String errorMessage = "Failed to setup system alert Callback : " + ex.getMessage();
                        messageReadFailed(errorMessage);
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            String errorMessage = "Failed to connect to RabbitMQ host : " + MQ_HOST + " on port " + MQ_PORT;
            messageReadFailed(errorMessage);
        }
    }

    private DeliverCallback defineCallBack(Channel channel, MessageProcessor messageProcessor, String routing_key) {
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            if (delivery.getEnvelope().getRoutingKey().matches(Objects.requireNonNull(routing_key))) {
                int rowCount = messageProcessor.processMessage(message);
                if (rowCount > 0) {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    log.info("Message inserted into DB");
                } else if(rowCount == -1) {
                    //-1 = the message is badly formatted and must be rejected
                    log.warn("The message was rejected and will not be processed further");
                    channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                } else {
                    // if 0 the message will simply be requeued
                    log.warn("The message will be requeued");
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), true,false);
                }
            } else {
                log.error("Queue " + routing_key + " has a bad message with routing key : " + delivery.getEnvelope().getRoutingKey());
            }
        };
    }

    private void messageReadFailed(String message) throws MessagingException {
        log.error(message);
//        messageStatus.update(MessageStatusType.ERROR);
//        alertSystemCache.add(message);
//        alertService.sendAlert(sensorRecord,message);
    }

}
