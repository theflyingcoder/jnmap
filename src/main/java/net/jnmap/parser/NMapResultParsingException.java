package net.jnmap.parser;

/**
 * Created by lucas on 8/28/15.
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
