package net.jnmap.scanner;

import java.io.Serializable;

/**
 * Scan Job interface
 *
 * Created by lucas on 8/29/15.
 */
public interface Job extends Serializable {
    String ATTR_ELAPSED = "elapsed";
    String ATTR_HOST_STATUS_STATE = "state";
    String TAG_HOST_STATUS = "status";
    String TAG_FINISHED = "finished";

    String getTarget();

    Result getResult();

    void setResult(Result result);

    void setOutputs(String outputs);

    String getOutputs();

    void setErrors(String errors);

    String getErrors();

    void setTargetStatus(String value);

    void setElapsedSecs(float elapsed);

    long getId();

    float getElapsedSecs();

    String getTargetStatus();
}
