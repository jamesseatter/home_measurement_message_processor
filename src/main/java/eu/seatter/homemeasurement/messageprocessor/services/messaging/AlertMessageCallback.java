//package eu.seatter.homemeasurement.messageprocessor.services.messaging;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.rabbitmq.client.DeliverCallback;
//import eu.seatter.homemeasurement.messageprocessor.model.AlertRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
///**
// * Created by IntelliJ IDEA.
// * User: jas
// * Date: 06/03/2020
// * Time: 16:31
// */
//@Service
//public class AlertMessageCallback {
//
//    @Autowired AlertMessageCallback alertMessageCallback;
//
//    public DeliverCallback callback() {
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            if(delivery.getEnvelope().getRoutingKey().matches("alert.*")) {
//                this.processMessage(message);
//            }
//            //System.out.println(" [x] Received '" + message + "'");
//        };
//
//        return deliverCallback;
//    }
//
//    private AlertRecord processMessage(String json) throws JsonProcessingException {
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        AlertRecord alertRecord = new AlertRecord();
//        try {
//            alertRecord = mapper.readValue(json, AlertRecord.class);
//            System.out.println(alertRecord.toString());
//        } catch (IOException ex) {
//            System.out.println("ERROR : Unable to convert received message : " + ex.getLocalizedMessage());
//        }
//        return alertRecord;
//
//    }
//}
