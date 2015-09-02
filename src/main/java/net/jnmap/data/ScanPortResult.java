package net.jnmap.data;

import net.jnmap.scanner.Result;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Scan result containing ports information and error message if applicable.
 *
 * Created by lucas.
 */
public class ScanPortResult implements Result, Serializable{
    private static final long serialVersionUID = -1984755640440073668L;

    public final static String TAG_NMAPRUN = "nmaprun";

    private List<Port> ports;

    public ScanPortResult() {
        ports = new ArrayList<>();
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void addPort(Port port) {
        this.ports.add(port);
    }

    @Override
    public String toString() {
        return "ScanPortResult{" +
                "ports=" + ports +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScanPortResult that = (ScanPortResult) o;

        return new EqualsBuilder()
                .append(ports, that.ports)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ports)
                .toHashCode();
    }
}
