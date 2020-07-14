package eu.seatter.homemeasurement.messageprocessor.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 30/03/2020
 * Time: 23:18
 */
@Setter
@Getter
@Builder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
public class SystemAlert implements Comparable<SystemAlert> {
    private UUID alertUID;
    private String title;

    private LocalDateTime alertTimeUTC; // equals the measurement measurementTimeUTC
    private String message;

    public String loggerFormat() {
        return "[" + title + "]";
    }

    @Override
    public String toString() {
        return "systemAlert{" +
                "title='" + title + '\'' +
                ", message=" + message +
                ", measureTimeUTC=" + alertTimeUTC +
                '}';
    }

    @Override
    public int compareTo(@NotNull SystemAlert that) {
        return this.alertTimeUTC.compareTo(that.alertTimeUTC);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || (this.getClass() != obj.getClass()))
        {
            return false;
        }

        SystemAlert guest = (SystemAlert) obj;
        return (this.alertUID == guest.alertUID) &&
                (this.alertTimeUTC != null && alertTimeUTC.equals(guest.alertTimeUTC)) &&
                (this.message != null &&
                        message.equals(guest.message));
    }
}
