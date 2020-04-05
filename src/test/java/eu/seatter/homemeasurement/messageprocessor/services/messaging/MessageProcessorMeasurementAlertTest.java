package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import eu.seatter.homemeasurement.messageprocessor.model.MeasurementAlert;
import eu.seatter.homemeasurement.messageprocessor.services.database.alert.MeasurementAlertDAO;
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
class MessageProcessorMeasurementAlertTest {

    @Mock
    MeasurementAlertDAO measurementAlertDAO;

    MessageProcessorMeasurementAlert messageProcessorMeasurementAlert;

    @BeforeEach
    void setUp() {
        messageProcessorMeasurementAlert = new MessageProcessorMeasurementAlert(measurementAlertDAO);
    }

    @Test
    void whenProcessMessage_givenGoodJson_thenReturn1() {
        //given
        String json = "{\"alertUID\":\"ab7f06eb-bb1b-4d6f-b739-da0420af9a68\",\"title\":\"Température de l'eau de chaudière\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"value\":35.0,\"measurementUnit\":\"C\",\"message\":null,\"alertSentEmail\":false,\"alertSentEmailTO\":\"temperature_threshold_alerts_private\"}";

        //when
        when(measurementAlertDAO.insertRecord(any(MeasurementAlert.class))).thenReturn(1);

        //then
        int rows = messageProcessorMeasurementAlert.processMessage(json);
        assertEquals(1,rows);

    }

    @Test
    void whenProcessMessage_givenBadFieldNameInJson_thenReturn0() {
        //given
        String json = "{\"BADFIELDNAME\":\"ab7f06eb-bb1b-4d6f-b739-da0420af9a68\",\"title\":\"Température de l'eau de chaudière\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"value\":35.0,\"measurementUnit\":\"C\",\"message\":null,\"alertSentEmail\":false,\"alertSentEmailTO\":\"temperature_threshold_alerts_private\"}";

        //when
        lenient().when(measurementAlertDAO.insertRecord(any(MeasurementAlert.class))).thenReturn(1);

        //then
        int rows = messageProcessorMeasurementAlert.processMessage(json);
        assertEquals(0,rows);
    }

    @Test
    void whenProcessMessage_givenNullValueInJson_thenReturnMinus1() {
        //given
        String json = "{\"alertUID\":null,\"title\":\"Température de l'eau de chaudière\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"value\":35.0,\"measurementUnit\":\"C\",\"message\":null,\"alertSentEmail\":false,\"alertSentEmailTO\":\"temperature_threshold_alerts_private\"}";

        //when
        when(measurementAlertDAO.insertRecord(any(MeasurementAlert.class))).thenThrow(NullPointerException.class);

        //then
        int rows = messageProcessorMeasurementAlert.processMessage(json);
        assertEquals(-1,rows);
    }

    @Test
    void whenProcessMessage_givenDBNotAccessible_thenReturn0() {
        //given
        String json = "{\"alertUID\":\"ab7f06eb-bb1b-4d6f-b739-da0420af9a68\",\"title\":\"Température de l'eau de chaudière\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"value\":35.0,\"measurementUnit\":\"C\",\"message\":null,\"alertSentEmail\":false,\"alertSentEmailTO\":\"temperature_threshold_alerts_private\"}";

        //when
        lenient().when(measurementAlertDAO.insertRecord(any(MeasurementAlert.class))).thenThrow(Mockito.mock(DataAccessException.class));

        //then
        int rows = messageProcessorMeasurementAlert.processMessage(json);
        assertEquals(0,rows);
    }

    @Test
    void whenProcessMessage_givenNullJson_thenReturn0() throws IllegalArgumentException{
        //given
        String json = null;

        //when

        //then
        int rows = messageProcessorMeasurementAlert.processMessage(json);
        assertEquals(-1,rows);
    }
}