package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import eu.seatter.homemeasurement.messageprocessor.model.MeasurementAlert;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 06/03/2020
 * Time: 14:01
 */
@Repository
@Slf4j
public class MeasurementAlertDAO {
    final JdbcTemplate jdbcTemplate;

    @Value("${database.alert.measurement.table:CHANGE_ME}")
    private String db_alert_table;

    public MeasurementAlertDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertRecord(@NotNull final MeasurementAlert ma) {
        String validateResult = validateRecord(ma);
        if (validateResult == null) {
            log.debug("Inserting measurement alert into DB");
            final String sql = "INSERT INTO " + db_alert_table + " (alert_uid, date_alert_utc, title, value, measurement_unit, message, email_sent, email_sent_to) VALUES (?,?,?,?,?,?,?,?)";
            return jdbcTemplate.update(sql, ma.getAlertUID().toString(), ma.getAlertTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), ma.getTitle(), ma.getValue(), ma.getMeasurementUnit().toString(), ma.getMessage(), ma.isAlertSentEmail(), ma.getAlertSentEmailTO());
        } else {
            throw new IllegalArgumentException("Provided measurement value : " + validateResult);
        }
    }

    private String validateRecord(@NotNull MeasurementAlert record) {
        if ((record.getAlertUID() == null) || (record.getAlertUID().toString() == "")) {return "AlertUID";}
        if ((record.getAlertTimeUTC() == null) || (record.getAlertTimeUTC().toString() == "")) {return "AlertTimeUTC";}
        if ((record.getTitle() == null) || (record.getTitle().toString() == "")) {record.setTitle("");}
        if ((record.getMessage() == null) || (record.getMessage().toString() == "")) {record.setMessage("");}
        return null;
    }
}
