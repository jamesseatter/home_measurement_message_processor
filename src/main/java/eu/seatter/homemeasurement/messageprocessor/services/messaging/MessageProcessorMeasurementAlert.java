package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.seatter.homemeasurement.messageprocessor.model.MeasurementAlert;
import eu.seatter.homemeasurement.messageprocessor.services.database.alert.MeasurementAlertDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 09/03/2020
 * Time: 13:07
 */
@Slf4j
@Component
public class MessageProcessorMeasurementAlert implements MessageProcessor {
    private final MeasurementAlertDAO alertDAO;

    public MessageProcessorMeasurementAlert(MeasurementAlertDAO alertDAO) throws IllegalArgumentException {
        this.alertDAO = alertDAO;
    }

    public int processMessage(String json) {
        log.debug("New message received - " + json);
        if(json == null) {
            log.error("The message is null and cannot be processed. It will be removed from the queue.");
            return -1;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        MeasurementAlert measurementAlert;

        try {
            measurementAlert = mapper.readValue(json, MeasurementAlert.class);
            log.debug("Measurement Alert record object : " + measurementAlert.toString());
            try {
                log.debug("Writing to the DB");
                return alertDAO.insertRecord(measurementAlert);
            } catch (DataAccessException ex) {
                log.error("Error writing to the DB : " + ex.getMessage());
                return 0; // requeue the message
            } catch (NullPointerException ex) {
                log.error("A value in the message is NULL and cannot be processed. It will be removed from the queue. " + ex.getMessage());
                return -1; //reject the message
            }
        } catch (JsonProcessingException ex) {
            log.error("Unable to convert received message : " + ex.getLocalizedMessage());
            return 0;
        }
    }
}
