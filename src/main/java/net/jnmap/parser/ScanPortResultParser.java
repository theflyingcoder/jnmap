package net.jnmap.parser;

import net.jnmap.scanner.Job;

/**
 * Scan Port Result Parser
 *
 * Created by lucas.
 */
public interface ScanPortResultParser {
    Job parse(Job job);
}
