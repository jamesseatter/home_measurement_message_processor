package eu.seatter.homemeasurement.messageprocessor.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 07/12/2018
 * Time: 13:19
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class Measurement implements Comparable<Measurement> {
    private UUID recordUID;
    private String sensorid;
    private String title;

    private LocalDateTime measureTimeUTC;
    private Double value;

    private String description;
    private SensorMeasurementUnit measurementUnit;
    private SensorType sensorType;
    private int familyid;

    private Double low_threshold;
    private Double high_threshold;

    private Boolean measurementSentToMq = false;

    private UUID alertID;
    private String alertgroup;
    private String alertdestination;


    public String loggerFormat() {
        return "[" + sensorid + "/" + sensorType + "/" + familyid + "]";
    }

    @Override
    public String toString() {
        return "measurement{" +
                "sensorid='" + sensorid + '\'' +
                ", sensorType=" + sensorType +
                ", measureTimeUTC=" + measureTimeUTC +
                ", value=" + value + " " + measurementUnit +
                '}';
    }

    @Override
    public int compareTo(Measurement that) {
        return this.measureTimeUTC.compareTo(that.measureTimeUTC);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || (this.getClass() != obj.getClass()))
        {
            return false;
        }

        Measurement guest = (Measurement) obj;
        return (this.recordUID == guest.recordUID) &&
                (this.measureTimeUTC != null && measureTimeUTC.equals(guest.measureTimeUTC)) &&
                (this.sensorid != null && sensorid.equals(guest.sensorid));
    }
}
