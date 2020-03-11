package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 29/02/2020
 * Time: 08:52
 */
@Service
@Slf4j
public class MessagingService implements CommandLineRunner {
    private final String MQ_MEASUREMENT_UNIQUE_ID;
    private final String MQ_ALERT_UNIQUE_ID;
    private final String MQ_HOST;
    private final int MQ_PORT;
    private final String MQ_VHOST;
    private final String MQ_EXCHANGE_NAME;
    private final String MQ_QUEUE_NAME;
    private final String MQ_USERNAME;
    private final String MQ_USERPASSWORD;

    @Autowired
    MeasurementMessageProcessor measurementMessageProcessor;
    @Autowired
    AlertMessageProcessor alertMessageProcessor;

    public MessagingService(@Value("${RabbitMQService.measurement.unique_id:CHANGE_ME}") String m_uniqueid,
                            @Value("${RabbitMQService.alert.unique_id:CHANGE_ME}") String a_uniqueid,
                            @Value("${RabbitMQService.hostname:localhost}") String hostname,
                            @Value("${RabbitMQService.portnumber:5672}") int portnumber,
                            @Value("${RabbitMQService.vhost:/}") String vhost,
                            @Value("${RabbitMQService.exchangee:}") String exchangename,
                            @Value("${RabbitMQService.queue:}") String queuename,
                            @Value("${RabbitMQService.username:}") String username,
                            @Value("${RabbitMQService.password:}") String password) {
        this.MQ_MEASUREMENT_UNIQUE_ID = m_uniqueid;
        this.MQ_ALERT_UNIQUE_ID = a_uniqueid;
        this.MQ_HOST = hostname;
        this.MQ_PORT = portnumber;
        this.MQ_VHOST = vhost;
        this.MQ_EXCHANGE_NAME = exchangename;
        this.MQ_QUEUE_NAME = queuename;
        this.MQ_USERNAME = username;
        this.MQ_USERPASSWORD = password;

        log.debug("Host    :" + MQ_HOST);
        log.debug("Port    :" + MQ_PORT);
        log.debug("VHost   :" + MQ_VHOST);
        log.debug("ExName  :" + MQ_EXCHANGE_NAME);
        log.debug("QName   :" + MQ_QUEUE_NAME);
        log.debug("MeasID  :" + MQ_MEASUREMENT_UNIQUE_ID);
        log.debug("AlerID   :" + MQ_ALERT_UNIQUE_ID);
    }

    @Override
    public void run(String... args) throws Exception{
        log.info("MQ waiting for measurement messages");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQ_HOST);
        factory.setPort(MQ_PORT);
        factory.setUsername(MQ_USERNAME);
        factory.setPassword(MQ_USERPASSWORD);
        factory.setVirtualHost(MQ_VHOST);
        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            try {
                channel.basicQos(1); //tell channel to send one message at a time.
                //channel.queueDeclarePassive(MQ_QUEUE_NAME);
                channel.queueBind(MQ_QUEUE_NAME, MQ_EXCHANGE_NAME, "#");

            } catch (IOException ex) {
                String errorMessage = "ERROR: Failed to connect to RabbitMQ Channel/Queue : " + MQ_QUEUE_NAME;
                messageReadFailed(errorMessage);
                //todo Throw an error
                return;
            }
            log.debug("Connected to MQ Server");

            boolean autoAck = false;
            log.debug("Setting up message callbacks");
            //DeliverCallback alertCallback = alertMessage.callback();
            DeliverCallback deliveryCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                if(delivery.getEnvelope().getRoutingKey().matches(MQ_ALERT_UNIQUE_ID)) {
                    try {
                        alertMessageProcessor.processMessage(message);
                    } finally {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                } else if(delivery.getEnvelope().getRoutingKey().matches(MQ_MEASUREMENT_UNIQUE_ID)) {
                    try {
                        measurementMessageProcessor.processMessage(message);
                    } finally {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(MQ_QUEUE_NAME, autoAck,deliveryCallback,consumerTag -> { });

        } catch (Exception ex) {
            String errorMessage = "Failed to connect to RabbitMQ host : " + MQ_HOST + " on port " + MQ_PORT;
            messageReadFailed(errorMessage);
        }
    }

    private void messageReadFailed(String message) throws MessagingException {
        log.error(message);
//        messageStatus.update(MessageStatusType.ERROR);
//        alertSystemCache.add(message);
//        alertService.sendAlert(sensorRecord,message);
    }

}
