package eu.seatter.homemeasurement.messageprocessor.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 16/05/2020
 * Time: 16:51
 */
public class UtilDateTime {
    public static LocalDateTime getTimeDateNowNoSecondsInUTC() {
        LocalDateTime ldt = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
        return ldt;
    }

    public static LocalDateTime convertDateTimeUTCToLocal(@NotNull LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtLocal = zdt.withZoneSameInstant(ZoneId.systemDefault());

        return zdtLocal.toLocalDateTime();
    }
}
