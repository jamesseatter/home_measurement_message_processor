package eu.seatter.homemeasurement.messageprocessor.services.database.measurement;

import eu.seatter.homemeasurement.messageprocessor.model.Measurement;
import eu.seatter.homemeasurement.messageprocessor.model.SensorMeasurementUnit;
import eu.seatter.homemeasurement.messageprocessor.model.SensorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 03/04/2020
 * Time: 13:23
 */
@ExtendWith(MockitoExtension.class)
class MeasurementDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private MeasurementDAO measurementDAO;

    @BeforeEach
    public void initUseCase() {
        measurementDAO = new MeasurementDAO(jdbcTemplate);
        ReflectionTestUtils.setField(measurementDAO, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(measurementDAO, "db_measurement_table", "measurement");
    }

    @Test
    void whenInsertRecord_thenReturnNumberRowsAffected() {
        //given
        Measurement measurement = Measurement.builder()
                .recordUID(UUID.randomUUID())
                .sensorid("28-000000000002")
                .title("Température de l'eau de chaudière")
                .description("Returns the temperature of the hot water in the boiler")
                .measureTimeUTC(ZonedDateTime.now(ZoneId.of("Etc/UTC")).truncatedTo(ChronoUnit.MINUTES))
                .familyid(40)
                .sensorType(SensorType.ONEWIRE)
                .measurementUnit(SensorMeasurementUnit.C)
                .value(56.09)
                .low_threshold(35.0)
                .high_threshold(60.0)
                .alertgroup("temperature_threshold_alerts_private")
                .alertdestination("PRIVATE")
                .alertUID(UUID.randomUUID())
                .build();

        String sql = "INSERT INTO measurement (record_id,date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination, alert_uid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //when
        when(jdbcTemplate.update(sql, measurement.getRecordUID().toString(), measurement.getMeasureTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), measurement.getSensorType().toString(), measurement.getSensorid(), measurement.getTitle(), measurement.getDescription(), measurement.getMeasurementUnit().toString(), measurement.getValue(), measurement.getLow_threshold(), measurement.getHigh_threshold(), measurement.getAlertgroup(), measurement.getAlertdestination(), measurement.getAlertUID().toString())).thenReturn(1);

        //then
        assertEquals(1, measurementDAO.insertRecord(measurement));

    }

    @Test
    void whenInsertRecordWithEmptyMeasurementRecord_thenThrowNullPointerException() {
        //given
        Measurement measurement = new Measurement();

        String sql = "INSERT INTO measurement (record_id,date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination, alert_uid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //when

        //then
        Assertions.assertThrows(NullPointerException.class, () -> jdbcTemplate.update(sql, measurement.getRecordUID().toString(), measurement.getMeasureTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), measurement.getSensorType().toString(), measurement.getSensorid(), measurement.getTitle(), measurement.getDescription(), measurement.getMeasurementUnit().toString(), measurement.getValue(), measurement.getLow_threshold(), measurement.getHigh_threshold(), measurement.getAlertgroup(), measurement.getAlertdestination(), measurement.getAlertUID()));
    }
}