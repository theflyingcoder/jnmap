package net.jnmap.data.dao;

import net.jnmap.scanner.Result;

import java.sql.SQLException;

/**
 *
 * Created by lucas.
 */
public interface ScanPortResultDAO {
    void create(long jobId, Result result);
}
