package eu.seatter.homemeasurement.messageprocessor.services.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 06/04/2020
 * Time: 18:59
 */
@Component
@Slf4j
public class DebugLogDB {

        final Environment env;

        public DebugLogDB(Environment env) {
                this.env = env;
        }

        // This class is ONLY called to print debug info about the DB configuration
        public void showDBSetting() {
                log.debug("DB         :" + env.getProperty("spring.datasource.url"));
                log.debug("Table - M  :" + env.getProperty("database.measurement.table"));
                log.debug("Table - AM :" + env.getProperty("database.alert.measurement.table"));
                log.debug("Table - AS :" + env.getProperty("database.alert.system.table"));
        }
}
