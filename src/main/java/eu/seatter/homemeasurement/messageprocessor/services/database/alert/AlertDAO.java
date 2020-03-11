package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import eu.seatter.homemeasurement.messageprocessor.model.AlertRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 06/03/2020
 * Time: 14:01
 */
@Repository
public class AlertDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${database.alert.table:CHANGE_ME}")
    private String db_alert_table;

    public int insertRecord(@NotNull final AlertRecord ar) {
//        System.out.println("inserting message into DB");
//        //final String sql = "INSERT INTO " + db_alert_table + " (date_measured_utc,sensor_type,sensor_id,title,description,measurement_unit,value,low_threshold,high_threshold,alert_group,alert_destination) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        final String sql = "INSERT INTO " + db_alert_table + " (uid, date_inserted_utc, description, sensor_id, alert_type) VALUES (?,?,?,?,?)";
//        return jdbcTemplate.update(sql, ar.get);
        return 1;
    }
}
