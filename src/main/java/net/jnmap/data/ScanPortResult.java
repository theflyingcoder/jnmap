package net.jnmap.data;

import net.jnmap.scanner.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Scan result containing ports information and error message if applicable.
 *
 * Created by lucas on 8/29/15.
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
}
