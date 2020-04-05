package eu.seatter.homemeasurement.messageprocessor.services.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 30/03/2020
 * Time: 23:33
 */
public interface MessageProcessor {
    public int processMessage(String json) throws JsonProcessingException;
}
