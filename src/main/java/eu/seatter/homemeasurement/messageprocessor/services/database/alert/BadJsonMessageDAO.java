package eu.seatter.homemeasurement.messageprocessor.services.database.alert;

import lombok.extern.slf4j.Slf4j;
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
public class BadJsonMessageDAO {
    final JdbcTemplate jdbcTemplate;

    @Value("${database.alert.badjson.table:CHANGE_ME}")
    private String db_table;

    public BadJsonMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertRecord(String title, String message) {
        log.debug("Inserting json bad message into DB");
        if(message == null) { message = "";}
        final String sql = "INSERT INTO " + db_table + " (title, message) VALUES (?,?)";
        jdbcTemplate.update(sql, title, message);
    }

}
