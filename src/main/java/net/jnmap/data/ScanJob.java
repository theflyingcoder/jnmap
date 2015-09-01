package net.jnmap.data;

import net.jnmap.scanner.Job;
import net.jnmap.scanner.Result;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;


/**
 * Object representing nmaprun tag
 *
 * Created by lucas.
 */
public class ScanJob implements Job {

    private static final long serialVersionUID = 5578169796817414274L;

    private long id;
    private String target;

    private transient String command;
    private String targetStatus;
    private float elapsedSecs;
    private Timestamp createTime;
    private Result result;

    // Raw result strings
    private String outputs;
    private String errors;

    public ScanJob() {
    }

    public ScanJob(String target) {
        this.target = target;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    @Override
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public float getElapsedSecs() {
        return elapsedSecs;
    }

    public void setElapsedSecs(float elapsedSecs) {
        this.elapsedSecs = elapsedSecs;
    }

    public String getOutputs() {
        return outputs;
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScanJob scanJob = (ScanJob) o;

        return new EqualsBuilder()
                .append(id, scanJob.id)
                .append(target, scanJob.target)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(target)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ScanJob{" +
                "id=" + id +
                ", target='" + target + '\'' +
                ", command='" + command + '\'' +
                ", targetStatus='" + targetStatus + '\'' +
                ", elapsedSecs=" + elapsedSecs +
                ", createTime=" + createTime +
                ", result=" + result +
                ", outputs='" + outputs + '\'' +
                ", errors='" + errors + '\'' +
                '}';
    }
}
