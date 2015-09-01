package net.jnmap.data.dao.mysql;

import net.jnmap.data.Port;
import net.jnmap.data.ScanPortResult;
import net.jnmap.data.dao.ScanPortResultDAO;
import net.jnmap.scanner.Result;
import org.apache.commons.collections.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;


/**
 * Data Access Object for scan port result table
 * <p/>
 * Created by lucas.
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
        if (CollectionUtils.isEmpty(result.getPorts())) {
            System.out.print("No port results for job id:" + jobId);
            return;
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();

            stmt = connection.prepareStatement("INSERT INTO scan_port_result (scan_job_id, port, state, protocol, service, create_time) VALUES (?,?,?,?,?,?)");
            for (Port port : result.getPorts()) {
                stmt.setLong(1, jobId);
                stmt.setInt(2, port.getPort());
                stmt.setString(3, port.getState());
                stmt.setString(4, port.getProtocol());
                stmt.setString(5, port.getService());
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                stmt.addBatch();
            }
            stmt.executeBatch();
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

            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
