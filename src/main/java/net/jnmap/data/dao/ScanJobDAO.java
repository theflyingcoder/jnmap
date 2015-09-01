package net.jnmap.data.dao;

import net.jnmap.data.ScanJob;
import net.jnmap.scanner.Job;

import java.util.List;

/**
 * Created by lucas.
 */
public interface ScanJobDAO {
    ScanJob create(String target, String fullCommandLine);

    void update(Job completedJob);

    List<Job> get(String target, int reportDayCount);
}
