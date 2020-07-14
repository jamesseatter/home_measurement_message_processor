package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import eu.seatter.homemeasurement.messageprocessor.model.SystemAlert;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 30/03/2020
 * Time: 23:18
 */
@Repository
@Slf4j
public class SystemAlertDAO {
    final JdbcTemplate jdbcTemplate;

    @Value("${database.alert.system.table:CHANGE_ME}")
    private String dbAlertTable;

    public SystemAlertDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertRecord(@NotNull final SystemAlert systemAlert) {
        log.warn("System alert logging to DB not available");
        log.debug("Inserting system alert into DB");
        final String sql = "INSERT INTO " + dbAlertTable + " (alert_uid,date_alert_utc, title, message) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql,  systemAlert.getAlertUID().toString(), systemAlert.getAlertTimeUTC(),systemAlert.getTitle(), systemAlert.getMessage());
    }
}
