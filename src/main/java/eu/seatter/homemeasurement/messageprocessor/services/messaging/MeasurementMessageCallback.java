//package eu.seatter.homemeasurement.messageprocessor.services.messaging;
//
//import com.rabbitmq.client.DeliverCallback;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * Created by IntelliJ IDEA.
// * User: jas
// * Date: 06/03/2020
// * Time: 16:31
// */
//@Service
//public class MeasurementMessageCallback {
//
//    @Autowired MeasurementMessageCallback measurementMessageCallback;
//
//    public DeliverCallback callback() {
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            if(delivery.getEnvelope().getRoutingKey().matches("measurement.*")) {
////                MeasurementMessageCallback measurement = new MeasurementMessageCallback();
//                MeasurementMessageProcessor mp = new MeasurementMessageProcessor();
//                mp.processMessage(message);
//            }
//            //System.out.println(" [x] Received '" + message + "'");
//        };
//
//        return deliverCallback;
//    }
//
//}
