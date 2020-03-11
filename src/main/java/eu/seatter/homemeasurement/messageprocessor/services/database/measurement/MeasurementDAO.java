package eu.seatter.homemeasurement.messageprocessor.services.database.measurement;

import eu.seatter.homemeasurement.messageprocessor.model.SensorRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MeasurementDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${database.measurement.table:CHANGE_ME}")
    private String db_measurement_table;

    public int insertRecord(@NotNull final SensorRecord sr) {
        System.out.println("inserting message into DB");
        final String sql = "INSERT INTO " + db_measurement_table + " (date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, sr.getMeasureTimeUTC().withZoneSameInstant(ZoneId.of("Etc/UTC")).toLocalDateTime(), sr.getSensorType().toString(), sr.getSensorid(), sr.getTitle(), sr.getDescription(), sr.getMeasurementUnit().toString(), sr.getValue(), sr.getLow_threshold(), sr.getHigh_threshold(), sr.getAlertgroup(), sr.getAlertdestination());
    }
}
