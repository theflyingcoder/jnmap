package net.jnmap.parser;

/**
 * Created by lucas.
 */
public class NMapResultParsingException extends RuntimeException {
    private String outputAttempted;

    public NMapResultParsingException(String message, String outputAttempted) {
        super(message);
        this.outputAttempted = outputAttempted;
    }

    public NMapResultParsingException(String message, String outputAttempted, Throwable cause) {
        super(message, cause);
        this.outputAttempted = outputAttempted;
    }

    public String getOutputAttempted() {
        return outputAttempted;
    }
}
