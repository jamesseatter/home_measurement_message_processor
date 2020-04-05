package eu.seatter.homemeasurement.messageprocessor.services.database.measurement;

import eu.seatter.homemeasurement.messageprocessor.model.Measurement;
import lombok.extern.slf4j.Slf4j;
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
public class MeasurementDAO {
    final
    JdbcTemplate jdbcTemplate;

    @Value("${database.measurement.table:CHANGE_ME}")
    private String db_measurement_table;

    public MeasurementDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertRecord(final Measurement sr) {
        String validateResult = validateRecord(sr);
        if (validateResult == null) {
            log.info("inserting measurement message into DB");
            final String sql = "INSERT INTO " + db_measurement_table + " (record_id,date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination, alert_uid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql, sr.getRecordUID().toString(), sr.getMeasureTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), sr.getSensorType().toString(), sr.getSensorid(), sr.getTitle(), sr.getDescription(), sr.getMeasurementUnit().toString(), sr.getValue(), sr.getLow_threshold(), sr.getHigh_threshold(), sr.getAlertgroup(), sr.getAlertdestination(), sr.getAlertUID());
        } else {
            throw new IllegalArgumentException("Provided measurement value : " + validateResult);
        }
    }

    private String validateRecord(Measurement sr) {
        if ((sr.getAlertUID() == null) || (sr.getAlertUID().toString() == "")) {return "AlertUID";}
        if ((sr.getMeasureTimeUTC() == null) || (sr.getMeasureTimeUTC().toString() == "")) {return "MeasureTimeUTC";}
        if ((sr.getTitle() == null) || (sr.getTitle() == "")) {sr.setTitle("");}
        if ((sr.getDescription() == null) || (sr.getDescription() == "")) {sr.setDescription("");}
        return null;
    }
}
