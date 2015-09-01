package net.jnmap.scanner;

import net.jnmap.data.Port;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lucas.
 */
public interface Result extends Serializable{
    List<Port> getPorts();

    void addPort(Port port);
}
