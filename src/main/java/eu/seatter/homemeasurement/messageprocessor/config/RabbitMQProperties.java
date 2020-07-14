package eu.seatter.homemeasurement.messageprocessor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 29/06/2020
 * Time: 08:48
 */
@Configuration
@ConfigurationProperties(prefix = "rabbitmqservice")
public class RabbitMQProperties {

    private String hostname;
    private String username;
    private String password;
    private String vhost;
    private String exchange;
    private String queue_measurement;
    private String queue_alert_measurement;
    private String queue_alert_system;
    private String routing_key_measurement;
    private String routing_key_alert_measurement;
    private String routing_key_alert_system;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue_measurement() {
        return queue_measurement;
    }

    public void setQueue_measurement(String queue_measurement) {
        this.queue_measurement = queue_measurement;
    }

    public String getQueue_alert_measurement() {
        return queue_alert_measurement;
    }

    public void setQueue_alert_measurement(String queue_alert_measurement) {
        this.queue_alert_measurement = queue_alert_measurement;
    }

    public String getQueue_alert_system() {
        return queue_alert_system;
    }

    public void setQueue_alert_system(String queue_alert_system) {
        this.queue_alert_system = queue_alert_system;
    }

    public String getRouting_key_measurement() {
        return routing_key_measurement;
    }

    public void setRouting_key_measurement(String routing_key_measurement) {
        this.routing_key_measurement = routing_key_measurement;
    }

    public String getRouting_key_alert_measurement() {
        return routing_key_alert_measurement;
    }

    public void setRouting_key_alert_measurement(String routing_key_alert_measurement) {
        this.routing_key_alert_measurement = routing_key_alert_measurement;
    }

    public String getRouting_key_alert_system() {
        return routing_key_alert_system;
    }

    public void setRouting_key_alert_system(String routing_key_alert_system) {
        this.routing_key_alert_system = routing_key_alert_system;
    }
}
