package net.jnmap.parser;

import net.jnmap.scanner.Job;

/**
 * Scan Port Result Parser
 *
 * Created by lucas on 8/30/15.
 */
public interface ScanPortResultParser {
    Job parse(Job job);
}
