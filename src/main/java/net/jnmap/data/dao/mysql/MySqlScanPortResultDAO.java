package net.jnmap.data.dao.mysql;

import net.jnmap.data.Port;
import net.jnmap.data.ScanPortResult;
import net.jnmap.data.dao.ScanPortResultDAO;
import net.jnmap.scanner.Result;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


/**
 * Data Access Object for scan port result table
 * <p/>
 * Created by lucas on 8/28/15.
 */
public class MySqlScanPortResultDAO implements ScanPortResultDAO {

    private DataSource dataSource;

    public MySqlScanPortResultDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void create(long jobId, Result result) {
        if (null == result) {
            System.err.println("Missing result object when attempt to create scan port result");
            return;
        }
        Connection connection = null;
        PreparedStatement scanPortResultStmt = null;
        try {
            connection = dataSource.getConnection();

            scanPortResultStmt = connection.prepareStatement("INSERT INTO scan_port_result (scan_job_id, port, state, protocol, service) VALUES (?,?,?,?,?)");
            for (Port port : result.getPorts()) {
                scanPortResultStmt.setLong(1, jobId);
                scanPortResultStmt.setInt(2, port.getPort());
                scanPortResultStmt.setString(3, port.getState());
                scanPortResultStmt.setString(4, port.getProtocol());
                scanPortResultStmt.setString(5, port.getService());
                scanPortResultStmt.addBatch();
            }
            scanPortResultStmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Failed to create scan port result: " + result);
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (null != scanPortResultStmt) {
                try {
                    scanPortResultStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
