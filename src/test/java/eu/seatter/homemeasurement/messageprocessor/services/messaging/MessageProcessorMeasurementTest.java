package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import eu.seatter.homemeasurement.messageprocessor.model.Measurement;
import eu.seatter.homemeasurement.messageprocessor.services.database.measurement.MeasurementDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 03/04/2020
 * Time: 11:05
 */
@ExtendWith(MockitoExtension.class)
class MessageProcessorMeasurementTest {
    @Mock
    MeasurementDAO measurementDAO;

    MessageProcessorMeasurement messageProcessorMeasurement;

    @BeforeEach
    void setUp() {
        messageProcessorMeasurement = new MessageProcessorMeasurement(measurementDAO);
    }

    @Test
    void whenProcessMessage_givenGoodJson_thenReturn1() {
        //given
        String json = "{\"recordUID\":\"3182b3d8-291b-43ca-8c09-40fd272494da\",\"sensorid\":\"28-000008d2fdb9\",\"title\":\"Température de l'eau à l'arrivée\",\"measureTimeUTC\":\"2020-04-04T08:38:00Z\",\"value\":47.3,\"description\":\"Returns the temperature of the hot water entering the house from the central heating system.\",\"measurementUnit\":\"C\",\"sensorType\":\"ONEWIRE\",\"familyid\":40,\"low_threshold\":45.0,\"high_threshold\":60.0,\"measurementSentToMq\":true,\"alertUID\":null,\"alertgroup\":\"temperature_threshold_alerts_private\",\"alertdestination\":\"BORRY\"}";

        //when
        when(measurementDAO.insertRecord(any(Measurement.class))).thenReturn(1);

        //then
        int rows = messageProcessorMeasurement.processMessage(json);
        assertEquals(1,rows);

    }

    @Test
    void whenProcessMessage_givenBadFieldNameInJson_thenReturn0() {
        //given
        String json = "{\"BADFIELDNAME\":\"3182b3d8-291b-43ca-8c09-40fd272494da\",\"sensorid\":\"28-000008d2fdb9\",\"title\":\"Température de l'eau à l'arrivée\",\"measureTimeUTC\":\"2020-04-04T08:38:00Z\",\"value\":47.3,\"description\":\"Returns the temperature of the hot water entering the house from the central heating system.\",\"measurementUnit\":\"C\",\"sensorType\":\"ONEWIRE\",\"familyid\":40,\"low_threshold\":45.0,\"high_threshold\":60.0,\"measurementSentToMq\":true,\"alertUID\":null,\"alertgroup\":\"temperature_threshold_alerts_private\",\"alertdestination\":\"BORRY\"}";

        //when
        lenient().when(measurementDAO.insertRecord(any(Measurement.class))).thenReturn(1);

        //then
        int rows = messageProcessorMeasurement.processMessage(json);
        assertEquals(0,rows);
    }

    @Test
    void whenProcessMessage_givenNullValueInJson_thenReturnMinus1() {
        //given
        String json = "{\"recordUID\":null,\"sensorid\":\"28-000008d2fdb9\",\"title\":\"Température de l'eau à l'arrivée\",\"measureTimeUTC\":\"2020-04-04T08:38:00Z\",\"value\":47.3,\"description\":\"Returns the temperature of the hot water entering the house from the central heating system.\",\"measurementUnit\":\"C\",\"sensorType\":\"ONEWIRE\",\"familyid\":40,\"low_threshold\":45.0,\"high_threshold\":60.0,\"measurementSentToMq\":true,\"alertUID\":null,\"alertgroup\":\"temperature_threshold_alerts_private\",\"alertdestination\":\"BORRY\"}";

        //when
        when(measurementDAO.insertRecord(any(Measurement.class))).thenThrow(NullPointerException.class);

        //then
        int rows = messageProcessorMeasurement.processMessage(json);
        assertEquals(-1,rows);
    }

    @Test
    void whenProcessMessage_givenDBNotAccessible_thenReturn0() {
        //given
        String json = "{\"recordUID\":null,\"sensorid\":\"28-000008d2fdb9\",\"title\":\"Température de l'eau à l'arrivée\",\"measureTimeUTC\":\"2020-04-04T08:38:00Z\",\"value\":47.3,\"description\":\"Returns the temperature of the hot water entering the house from the central heating system.\",\"measurementUnit\":\"C\",\"sensorType\":\"ONEWIRE\",\"familyid\":40,\"low_threshold\":45.0,\"high_threshold\":60.0,\"measurementSentToMq\":true,\"alertUID\":null,\"alertgroup\":\"temperature_threshold_alerts_private\",\"alertdestination\":\"BORRY\"}";

        //when
        when(measurementDAO.insertRecord(any(Measurement.class))).thenThrow(Mockito.mock(DataAccessException.class));

        //then
        int rows = messageProcessorMeasurement.processMessage(json);
        assertEquals(0,rows);
    }

    @Test
    void whenProcessMessage_givenNullJson_thenReturn0() throws IllegalArgumentException{
        //given
        String json = null;

        //when

        //then
        int rows = messageProcessorMeasurement.processMessage(json);
        assertEquals(-1,rows);
    }

}