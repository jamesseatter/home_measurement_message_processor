package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.seatter.homemeasurement.messageprocessor.model.SensorRecord;
import eu.seatter.homemeasurement.messageprocessor.services.database.measurement.MeasurementDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 09/03/2020
 * Time: 12:51
 */
@Slf4j
@Component
public class MeasurementMessageProcessor {
    @Autowired
    private MeasurementDAO measurementDAO = new MeasurementDAO();

    public void processMessage(String json) throws JsonProcessingException {
        log.debug("New message received - " + json);

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        SensorRecord sensorRecord = new SensorRecord();
        try {
            sensorRecord = mapper.readValue(json, SensorRecord.class);
            System.out.println(sensorRecord.toString());
            try {
                measurementDAO.insertRecord(sensorRecord);
            } catch (Exception ex) {
                log.error("MeasurementDao Exception : " + ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("ERROR : Unable to convert received message : " + ex.getLocalizedMessage());
        }
        //return sensorRecord;

    }
}
