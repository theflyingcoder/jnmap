package net.jnmap.scanner;

import net.jnmap.data.Port;

import java.io.Serializable;
import java.util.List;

/**
 * Scan result interface
 *
 * Created by lucas.
 */
public interface Result extends Serializable{
    List<Port> getPorts();

    void addPort(Port port);
}
