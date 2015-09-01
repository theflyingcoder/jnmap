package net.jnmap.scanner;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Scan Job interface
 *
 * Created by lucas.
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

    String getCommand();

    Timestamp getCreateTime();
}
