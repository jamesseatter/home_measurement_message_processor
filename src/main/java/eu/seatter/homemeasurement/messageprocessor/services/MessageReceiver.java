package eu.seatter.homemeasurement.messageprocessor.services;

import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorMeasurement;
import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorMeasurementAlert;
import eu.seatter.homemeasurement.messageprocessor.services.messaging.MessageProcessorSystemAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 11/07/2020
 * Time: 18:07
 */
@Service
@Slf4j
public class MessageReceiver {
    private final MessageProcessorMeasurement mpMeasurement;
    private final MessageProcessorMeasurementAlert mpMeasurementAlert;
    private final MessageProcessorSystemAlert mpSystemAlert;

    public MessageReceiver(MessageProcessorMeasurement mpMeasurement, MessageProcessorMeasurementAlert mpMeasurementAlert, MessageProcessorSystemAlert mpSystemAlert) {
        this.mpMeasurement = mpMeasurement;
        this.mpMeasurementAlert = mpMeasurementAlert;
        this.mpSystemAlert = mpSystemAlert;
    }

    @RabbitListener(queues = "#{measurement}")
    public void receiveMeasurement(String in) {
        receive(in, 1);
    }

    @RabbitListener(queues = "#{alertMeasurement}")
    public void receiveAlertMeasurement(String in) {
        receive(in, 2);
    }

    @RabbitListener(queues = "#{alertSystem}")
    public void receiveAlertSystem(String in)  {
        receive(in, 3);
    }

    public void receive(String in, int receiver) {
        switch (receiver) {
            case 1 : //measurement
                mpMeasurement.processMessage(in);
                break;
            case 2 : //alertmeasurement
                mpMeasurementAlert.processMessage(in);
                break;
            case 3 : //alertsystem
                mpSystemAlert.processMessage(in);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + receiver);
        }
    }
}
