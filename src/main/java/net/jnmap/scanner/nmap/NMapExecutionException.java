package net.jnmap.scanner.nmap;

/**
 * Created by xephyr on 8/27/15.
 */
public class NMapExecutionException extends RuntimeException {

    private final String commandAttempted;
    private final NMapConfig propertiesUsed;

    public NMapExecutionException(String message, Throwable cause, String commandAttempted) {
        super(message, cause);
        this.commandAttempted = commandAttempted;
        this.propertiesUsed = null;
    }

    public NMapExecutionException(String message, NMapConfig propertiesUsed) {
        super(message);
        this.propertiesUsed = propertiesUsed;
        this.commandAttempted = null;
    }

    public NMapExecutionException(String message) {
        super(message);
        this.commandAttempted = null;
        this.propertiesUsed = null;
    }

    public NMapExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.commandAttempted = null;
        this.propertiesUsed = null;
    }

    public String getCommandAttempted() {
        return commandAttempted;
    }

    public NMapConfig getPropertiesUsed() {
        return propertiesUsed;
    }
}
