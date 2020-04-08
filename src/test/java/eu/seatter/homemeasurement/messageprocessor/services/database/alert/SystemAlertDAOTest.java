package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import eu.seatter.homemeasurement.messageprocessor.model.SystemAlert;
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
 * Date: 04/04/2020
 * Time: 13:54
 */
@ExtendWith(MockitoExtension.class)
class SystemAlertDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private SystemAlertDAO systemAlertDAO;

    private final String db_table = "alertsystem";

    @BeforeEach
    public void initUseCase() {
        systemAlertDAO = new SystemAlertDAO(jdbcTemplate);
        ReflectionTestUtils.setField(systemAlertDAO, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(systemAlertDAO, "db_alert_table", db_table);
    }

    @Test
    void whenInsertRecord_thenReturnNumberRowsAffected() {
        //given
        SystemAlert systemAlert = SystemAlert.builder()
                .alertUID(UUID.randomUUID())
                .title("Température de l'eau de chaudière")
                .message("Returns the temperature of the hot water in the boiler")
                .alertTimeUTC(ZonedDateTime.now(ZoneId.of("Etc/UTC")).truncatedTo(ChronoUnit.MINUTES))
                .build();

        String sql = "INSERT INTO " + db_table + " (alert_uid,date_alert_utc, title, message) VALUES (?,?,?,?)";

        //when
        when(jdbcTemplate.update(sql,  systemAlert.getAlertUID().toString(), systemAlert.getAlertTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(),systemAlert.getTitle(), systemAlert.getMessage())).thenReturn(1);

        //then
        assertEquals(1, systemAlertDAO.insertRecord(systemAlert));

    }

    @Test
    void whenInsertRecordWithEmptyMeasurementRecord_thenThrowNullPointerException() {
        //given
        SystemAlert systemAlert = new SystemAlert();

        String sql = "INSERT INTO " + db_table + " (alert_uid,date_alert_utc, title, message) VALUES (?,?,?,?)";

        //when

        //then
        Assertions.assertThrows(NullPointerException.class, () -> jdbcTemplate.update(sql,  systemAlert.getAlertUID().toString(), systemAlert.getAlertTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(),systemAlert.getTitle(), systemAlert.getMessage()));
    }
}