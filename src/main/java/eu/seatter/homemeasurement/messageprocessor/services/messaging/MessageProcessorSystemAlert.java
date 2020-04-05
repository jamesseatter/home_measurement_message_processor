package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.seatter.homemeasurement.messageprocessor.model.SystemAlert;
import eu.seatter.homemeasurement.messageprocessor.services.database.alert.SystemAlertDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 12/03/2020
 * Time: 17:23
 */
@Slf4j
@Component
public class MessageProcessorSystemAlert implements MessageProcessor {
    private final SystemAlertDAO systemAlertDAO;

    public MessageProcessorSystemAlert(SystemAlertDAO systemAlertDAO) {
        this.systemAlertDAO = systemAlertDAO;
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

        SystemAlert systemAlert;

        try {
            int recordCount = 0;
            systemAlert = mapper.readValue(json, SystemAlert.class);
            log.debug("System Alert record object : " + systemAlert.toString());
            try {
                log.error("Writing to the DB");
                return systemAlertDAO.insertRecord(systemAlert);
            } catch (DataAccessException ex) {
                log.error("Error writing to the DB : " + ex.getMessage());
                return 0; // requeue the message
            } catch (NullPointerException ex) {
                log.error("A value in the message is NULL and cannot be processed : " + ex.getMessage());
                return -1; //reject the message
            } catch (IllegalArgumentException ex) {
                log.error("The message is empty(null) and cannot be processed : " + ex.getMessage());
                return -1; //reject the message
            }
        } catch (JsonProcessingException ex) {
            log.error("Unable to convert received message : " + ex.getLocalizedMessage());
            return 0;
        }
    }
}
