package eu.seatter.homemeasurement.messageprocessor.services.messaging;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 30/03/2020
 * Time: 23:33
 */
public interface MessageProcessor {
    public int processMessage(String json);
}
