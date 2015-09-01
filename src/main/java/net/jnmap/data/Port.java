package net.jnmap.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Object to represent port result
 *
 * Created by lucas.
 */
public class Port implements Serializable {

    private static final long serialVersionUID = 4094084106973942793L;

    public static final String TAG_PORT = "port";
    public static final String TAG_PORT_SERVICE = "service";
    public static final String TAG_SERVICE_STATE = "state";

    public static final String ATTR_PROTOCOL = "protocol";
    public static final String ATTR_PORT_ID = "portid";
    public static final String ATTR_SERVICE = "service";
    public static final String ATTR_STATE = "state";
    public static final String ATTR_SERVICE_NAME = "name";

    // Port Attributes
    private int port;
    private String state;
    private String protocol;
    private String service;

    public Port() {
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Port port = (Port) o;

        return new EqualsBuilder()
                .append(this.port, port.port)
                .append(state, port.state)
                .append(protocol, port.protocol)
                .append(service, port.service)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(port)
                .append(state)
                .append(protocol)
                .append(service)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Port{" +
                "port=" + port +
                ", state='" + state + '\'' +
                ", protocol='" + protocol + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
