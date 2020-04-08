package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import eu.seatter.homemeasurement.messageprocessor.model.SystemAlert;
import eu.seatter.homemeasurement.messageprocessor.services.database.alert.BadJsonMessageDAO;
import eu.seatter.homemeasurement.messageprocessor.services.database.alert.SystemAlertDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 03/04/2020
 * Time: 11:06
 */
@ExtendWith(MockitoExtension.class)
class MessageProcessorSystemAlertTest {
    @Mock
    SystemAlertDAO systemAlertDAO;

    @Mock
    BadJsonMessageDAO badJsonMessageDAO;

    MessageProcessorSystemAlert messageProcessorSystemAlert;

    @BeforeEach
    void setUp() {
        messageProcessorSystemAlert = new MessageProcessorSystemAlert(systemAlertDAO, badJsonMessageDAO);
    }

    @Test
    void whenProcessMessage_givenGoodJson_thenReturn1() {
        //given
        String json = "{\"alertUID\":\"3182b3d8-291b-43ca-8c09-40fd272494da\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"title\":\"Test Alert\",\"message\":\"Test alert message.\"}";

        //when
        when(systemAlertDAO.insertRecord(any(SystemAlert.class))).thenReturn(1);

        //then
        int rows = messageProcessorSystemAlert.processMessage(json);
        assertEquals(1,rows);

    }

    @Test
    void whenProcessMessage_givenBadFieldNameInJson_thenReturn0() {
        //given
        String json = "{\"BADFIELDNAME\":\"3182b3d8-291b-43ca-8c09-40fd272494da\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"title\":\"Test Alert\",\"message\":\"Test alert message.\"}";

        //when
        lenient().when(systemAlertDAO.insertRecord(any(SystemAlert.class))).thenReturn(1);

        //then
        int rows = messageProcessorSystemAlert.processMessage(json);
        assertEquals(-1,rows);
        verify(badJsonMessageDAO, times(1)).insertRecord(anyString(),anyString());
    }

    @Test
    void whenProcessMessage_givenNullValueInJson_thenReturnMinus1() {
        //given
        String json = "{\"alertUID\":null,\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"title\":\"Test Alert\",\"message\":\"Test alert message.\"}";

        //when
        when(systemAlertDAO.insertRecord(any(SystemAlert.class))).thenThrow(NullPointerException.class);

        //then
        int rows = messageProcessorSystemAlert.processMessage(json);
        assertEquals(-1,rows);
        verify(badJsonMessageDAO, times(1)).insertRecord(anyString(),anyString());
    }

    @Test
    void whenProcessMessage_givenDBNotAccessible_thenReturn0() {
        //given
        String json = "{\"alertUID\":\"3182b3d8-291b-43ca-8c09-40fd272494da\",\"alertTimeUTC\":\"2020-03-12T14:20:00Z\",\"title\":\"Test Alert\",\"message\":\"Test alert message.\"}";

        //when
        lenient().when(systemAlertDAO.insertRecord(any(SystemAlert.class))).thenThrow(Mockito.mock(DataAccessException.class));

        //then
        int rows = messageProcessorSystemAlert.processMessage(json);
        assertEquals(0,rows);
    }

    @Test
    void whenProcessMessage_givenNullJson_thenReturn0() throws IllegalArgumentException{
        //given
        String json = null;

        //when

        //then
        int rows = messageProcessorSystemAlert.processMessage(json);
        assertEquals(-1,rows);
        verify(badJsonMessageDAO, times(1)).insertRecord(anyString(),eq(null));
    }
}