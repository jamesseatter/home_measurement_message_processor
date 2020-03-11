package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.seatter.homemeasurement.messageprocessor.model.AlertRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 09/03/2020
 * Time: 13:07
 */
@Slf4j
@Component
public class AlertMessageProcessor {
    public AlertRecord processMessage(String json) throws JsonProcessingException {
        log.debug("New message received - " + json);
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        AlertRecord alertRecord = new AlertRecord();
        try {
            alertRecord = mapper.readValue(json, AlertRecord.class);
            System.out.println(alertRecord.toString());
        } catch (IOException ex) {
            System.out.println("ERROR : Unable to convert received message : " + ex.getLocalizedMessage());
        }
        return alertRecord;

    }
}
