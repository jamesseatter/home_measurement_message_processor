package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import eu.seatter.homemeasurement.messageprocessor.model.MeasurementAlert;
import eu.seatter.homemeasurement.messageprocessor.model.SensorMeasurementUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 03/04/2020
 * Time: 22:33
 */
@ExtendWith(MockitoExtension.class)
class MeasurementAlertDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private MeasurementAlertDAO measurementAlertDAO;

    private final String db_table = "alertmeasurement";

    @BeforeEach
    public void initUseCase() {
        measurementAlertDAO = new MeasurementAlertDAO(jdbcTemplate);
        ReflectionTestUtils.setField(measurementAlertDAO, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(measurementAlertDAO, "db_alert_table", db_table);
    }

    @Test
    void whenInsertRecord_thenReturnNumberRowsAffected() {
        //given
        MeasurementAlert ma = MeasurementAlert.builder()
                .alertUID(UUID.randomUUID())
                .title("Température de l'eau de chaudière")
                .alertTimeUTC(getTimeDateNowInUTC())
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .message("Test alert")
                .alertSentEmail(true)
                .alertSentEmailTO("james")
                .build();

        final String sql = "INSERT INTO alertmeasurement (alert_uid, date_alert_utc, title, value, measurement_unit, message, email_sent, email_sent_to) VALUES (?,?,?,?,?,?,?,?)";

        //when
        when(jdbcTemplate.update(sql,
                ma.getAlertUID().toString(),
                ma.getAlertTimeUTC(),
                ma.getTitle(),
                ma.getValue(),
                ma.getMeasurementUnit().toString(),
                ma.getMessage(),
                ma.isAlertSentEmail(),
                ma.getAlertSentEmailTO())
        ).thenReturn(1);

        //then
        assertEquals(1, measurementAlertDAO.insertRecord(ma));

    }

    @Test
    void whenInsertRecord_withEmptymeasurementAlertRecord_thenThrowNullPointerException() {
        //given
        MeasurementAlert ma = new MeasurementAlert();

        final String sql = "INSERT INTO alertmeasurement (alert_uid, date_alert_utc, title, value, measurement_unit, message, email_sent, email_sent_to) VALUES (?,?,?,?,?,?,?,?)";

        //when

        //then
        Assertions.assertThrows(NullPointerException.class, () -> jdbcTemplate.update(sql, jdbcTemplate.update(sql, ma.getAlertUID(), ma.getAlertTimeUTC(), ma.getTitle(), ma.getValue(), ma.getMeasurementUnit().toString(), ma.getMessage(), ma.isAlertSentEmail(), ma.getAlertSentEmailTO())));
    }

    @Test
    public void whenValidateRecord_givenAlertUIDisNull_thenReturnAlertUID() {
        MeasurementAlert ma = MeasurementAlert.builder()
                .alertUID(null)
                .title("Température de l'eau de chaudière")
                .alertTimeUTC(getTimeDateNowInUTC())
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .message("Test alert")
                .alertSentEmail(true)
                .alertSentEmailTO("james")
                .build();

        //MeasurementAlert ma = new MeasurementAlert();

        assertTrue(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma).equals("AlertUID"));
    }

    @Test
    public void whenValidateRecord_givenAlertTimeUTCisNull_thenReturnAlertTimeUTC() {
        MeasurementAlert ma = MeasurementAlert.builder()
                .alertUID(UUID.randomUUID())
                .title("Température de l'eau de chaudière")
                .alertTimeUTC(null)
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .message("Test alert")
                .alertSentEmail(true)
                .alertSentEmailTO("james")
                .build();

        //MeasurementAlert ma = new MeasurementAlert();

        assertTrue(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma).equals("AlertTimeUTC"));
    }

    @Test
    public void whenValidateRecord_givenTitleisNull_thenReturnNull() {
        MeasurementAlert ma = MeasurementAlert.builder()
                .alertUID(UUID.randomUUID())
                .title(null)
                .alertTimeUTC(getTimeDateNowInUTC())
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .message("Test alert")
                .alertSentEmail(true)
                .alertSentEmailTO("james")
                .build();

        //MeasurementAlert ma = new MeasurementAlert();

        //assertTrue(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma).equals("AlertUID"));
        assertNull(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma));
    }

    @Test
    public void whenValidateRecord_givenMessageisNull_thenReturnNull() {
        MeasurementAlert ma = MeasurementAlert.builder()
                .alertUID(UUID.randomUUID())
                .title(null)
                .alertTimeUTC(getTimeDateNowInUTC())
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .message(null)
                .alertSentEmail(true)
                .alertSentEmailTO("james")
                .build();

        //MeasurementAlert ma = new MeasurementAlert();

        //assertTrue(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma).equals("AlertUID"));
        assertNull(ReflectionTestUtils.invokeMethod(measurementAlertDAO, "validateRecord",ma));
    }

    private static LocalDateTime getTimeDateNowInUTC() {
        LocalDateTime ldt = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
        return ldt;
    }
}