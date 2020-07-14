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
    private String queueMeasurement;
    private String queueAlertMeasurement;
    private String queueAlertSystem;
    private String routingKeyMeasurement;
    private String routingKeyAlertMeasurement;
    private String routingKeyAlertSystem;

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

    public String getQueueMeasurement() {
        return queueMeasurement;
    }

    public void setQueueMeasurement(String queueMeasurement) {
        this.queueMeasurement = queueMeasurement;
    }

    public String getQueueAlertMeasurement() {
        return queueAlertMeasurement;
    }

    public void setQueueAlertMeasurement(String queueAlertMeasurement) {
        this.queueAlertMeasurement = queueAlertMeasurement;
    }

    public String getQueueAlertSystem() {
        return queueAlertSystem;
    }

    public void setQueueAlertSystem(String queueAlertSystem) {
        this.queueAlertSystem = queueAlertSystem;
    }

    public String getRoutingKeyMeasurement() {
        return routingKeyMeasurement;
    }

    public void setRoutingKeyMeasurement(String routingKeyMeasurement) {
        this.routingKeyMeasurement = routingKeyMeasurement;
    }

    public String getRoutingKeyAlertMeasurement() {
        return routingKeyAlertMeasurement;
    }

    public void setRoutingKeyAlertMeasurement(String routingKeyAlertMeasurement) {
        this.routingKeyAlertMeasurement = routingKeyAlertMeasurement;
    }

    public String getRoutingKeyAlertSystem() {
        return routingKeyAlertSystem;
    }

    public void setRoutingKeyAlertSystem(String routingKeyAlertSystem) {
        this.routingKeyAlertSystem = routingKeyAlertSystem;
    }
}
