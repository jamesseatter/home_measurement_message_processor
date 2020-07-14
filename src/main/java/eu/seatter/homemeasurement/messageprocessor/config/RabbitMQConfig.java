package eu.seatter.homemeasurement.messageprocessor.config;

import eu.seatter.homemeasurement.messageprocessor.services.MessageReceiver;
import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorMeasurement;
import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorMeasurementAlert;
import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorSystemAlert;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 10/07/2020
 * Time: 14:56
 */
@Configuration
public class RabbitMQConfig {
    private final RabbitMQProperties rabbitMQProperties;
    private final MessageProcessorMeasurement mpMeasurement;
    private final MessageProcessorMeasurementAlert mpMeasurementAlert;
    private final MessageProcessorSystemAlert mpSystemAlert;

    public RabbitMQConfig(RabbitMQProperties rabbitMQProperties, MessageProcessorMeasurement mpMeasurement, MessageProcessorMeasurementAlert mpMeasurementAlert, MessageProcessorSystemAlert mpSystemAlert) {
        this.mpMeasurement = mpMeasurement;
        this.mpMeasurementAlert = mpMeasurementAlert;
        this.mpSystemAlert = mpSystemAlert;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(rabbitMQProperties.getExchange());
    }

    @Bean
    public MessageReceiver receiver() {
        return new MessageReceiver(mpMeasurement,mpMeasurementAlert,mpSystemAlert);
    }

    @Bean
    public Queue measurement() {
        return new Queue(rabbitMQProperties.getQueue_measurement());
    }

    @Bean
    public Queue alertMeasurement() {
        return new Queue(rabbitMQProperties.getQueue_alert_measurement());
    }

    @Bean
    public Queue alertSystem() {
        return new Queue(rabbitMQProperties.getQueue_alert_system());
    }

    @Bean
    public String routing_key_measurement() {
        return rabbitMQProperties.getRouting_key_measurement();
    }

    @Bean
    public String routing_key_alert_measurement() {
        return rabbitMQProperties.getRouting_key_alert_measurement();
    }

    @Bean
    public String routing_key_alert_system() {
        return rabbitMQProperties.getRouting_key_alert_system();
    }


    @Bean
    public Binding bindingMeasurement(TopicExchange topic,
                             Queue measurement) {
        return BindingBuilder.bind(measurement)
                .to(topic)
                .with(routing_key_measurement());
    }

    @Bean
    public Binding bindingAlertMeasurement(TopicExchange topic,
                                           Queue alertMeasurement) {
        return BindingBuilder.bind(alertMeasurement)
                .to(topic)
                .with(routing_key_alert_measurement());
    }

    @Bean
    public Binding bindingAlertSystem(TopicExchange topic,
                             Queue alertSystem) {
        return BindingBuilder.bind(alertSystem)
                .to(topic)
                .with(routing_key_alert_system());
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
