package eu.seatter.homemeasurement.messageprocessor.services.database.measurement;

import eu.seatter.homemeasurement.messageprocessor.model.Measurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
            log.debug("Inserting measurement into DB");
            final String sql = "INSERT INTO " + db_measurement_table + " (record_id,date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination, alert_uid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql, sr.getRecordUID().toString(), sr.getMeasureTimeUTC(), sr.getSensorType().toString(), sr.getSensorid(), sr.getTitle(), sr.getDescription(), sr.getMeasurementUnit().toString(), sr.getValue(), sr.getLow_threshold(), sr.getHigh_threshold(), sr.getAlertgroup(), sr.getAlertdestination(), sr.getAlertID().toString());
        } else {
            throw new IllegalArgumentException("Provided measurement value : " + validateResult);
        }
    }

    private String validateRecord(Measurement record) {
        if ((record.getMeasureTimeUTC() == null) || (record.getMeasureTimeUTC().toString() == "")) {return "MeasureTimeUTC";}
        if ((record.getAlertID() == null) || (record.getAlertID().toString() == "")) { record.setAlertID(UUID.fromString( "00000000-0000-0000-0000-000000000000" ));}
        if ((record.getTitle() == null) || (record.getTitle() == "")) {record.setTitle("");}
        if ((record.getDescription() == null) || (record.getDescription() == "")) {record.setDescription("");}
        return null;
    }
}
