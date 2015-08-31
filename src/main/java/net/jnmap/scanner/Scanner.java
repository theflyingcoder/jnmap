package net.jnmap.scanner;

/**
 * Created by lucas on 8/29/15.
 */
public interface Scanner {
    /**
     * Executes scan. This method should be implemented in thread safe manner.
     *
     * @param job
     * @return
     */
    Job scan(Job job);
}
