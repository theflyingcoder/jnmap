package net.jnmap.scanner;

/**
 * Created by lucas.
 */
public interface Scanner {
    /**
     * Executes scan. This method should be implemented in thread safe manner.
     *
     * @param job
     * @return
     */
    Job scan(Job job);

    String getFullCommandLine(String target);
}
