package net.jnmap.data.dao.mysql;

import com.mysql.jdbc.Statement;
import net.jnmap.data.Port;
import net.jnmap.data.ScanJob;
import net.jnmap.data.ScanPortResult;
import net.jnmap.scanner.Job;
import net.jnmap.scanner.Result;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ScanJob
 * <p/>
 * Created by lucas.
 */
public class MySqlScanJobDAO implements net.jnmap.data.dao.ScanJobDAO {


    private final long DAYS_IN_MS = 7L * 24 * 3600 * 1000;

    private DataSource dataSource;

    public MySqlScanJobDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Create scan job entry and returns scan job id
     *
     * @param target
     * @param command
     * @return
     * @throws SQLException
     */
    public ScanJob create(String target, String command) {
        if (StringUtils.isEmpty(target)) {
            System.err.println("Unable to create scan job due to missing target.");
            return null;
        }

        ScanJob job = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            stmt = connection.prepareStatement("INSERT INTO scan_job (target, target_hash, command, create_time) VALUES (?,MD5(?),?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, target);
            stmt.setString(2, target);
            stmt.setString(3, command.substring(0, Math.min(command.length(), 300)));
            stmt.setTimestamp(4, createTime);
            stmt.executeUpdate();

            resultSet = stmt.getGeneratedKeys();
            long newScanJobId = 0;
            if (resultSet.next()) {
                newScanJobId = resultSet.getLong(1);
            }
            job = new ScanJob();
            job.setId(newScanJobId);
            job.setCommand(command);
            job.setTarget(target);
            job.setCreateTime(createTime);
        } catch (SQLException e) {
            // We can choose to throw it up but for now, since we are not doing any recovery etc,
            // print it out to stderr and return null
            System.err.println("Failed to create scan job");
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

            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return job;
    }

    /**
     * Update scan job object
     *
     * @param job
     */
    public void update(Job job) {
        if (null == job) {
            System.err.println("Missing job object when attempt to update it to database.");
            return;
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("UPDATE scan_job SET elapsed_secs=?, target_status=?,update_time=? WHERE scan_job_id=?");

            stmt.setFloat(1, job.getElapsedSecs());
            stmt.setString(2, job.getTargetStatus());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(4, job.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to update job to database: " + job);
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

    /**
     * Retrieves scan results by target and report day count
     *
     * @param target
     * @param reportDayCount
     * @return
     */
    @Override
    public List<Job> get(String target, int reportDayCount) {
        System.out.println("Retrieving scan history for " + target + "...");
        long startTime = System.currentTimeMillis();
        List<Job> jobList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT " +
                            "  j.scan_job_id, " +
                            "  j.target_status, " +
                            "  j.create_time, " +
                            "  r.port, " +
                            "  r.state, " +
                            "  r.service " +
                            "FROM scan_port_result r, scan_job j " +
                            "WHERE r.scan_job_id = j.scan_job_id AND " +
                            "      j.target_hash = MD5(?) AND " +
                            "      j.create_time >= ? " +
                            "ORDER BY r.scan_job_id");

            stmt.setString(1, target);
            stmt.setDate(2, new Date(System.currentTimeMillis() - DAYS_IN_MS));

            resultSet = stmt.executeQuery();

            long currentJobId;
            long previousJobId = 0;
            if (null != resultSet) {
                ScanJob job = null;
                while (resultSet.next()) {
                    currentJobId = resultSet.getLong(1);
                    if (currentJobId != previousJobId) {
                        job = new ScanJob(target);
                        job.setId(currentJobId);
                        job.setTargetStatus(resultSet.getString(2));
                        job.setCreateTime(resultSet.getTimestamp(3));
                        previousJobId = currentJobId;

                        Result result = new ScanPortResult();
                        job.setResult(result);
                        jobList.add(job);
                    }

                    Port port = new Port();
                    port.setPort(resultSet.getInt(4));
                    port.setState(resultSet.getString(5));
                    port.setService(resultSet.getString(6));
                    if (null != job) job.getResult().addPort(port);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve job from database for target: " + target + "ms");
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

            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Retrieving scan history for [" + target + "] took " + (System.currentTimeMillis() - startTime) + "ms");
        return jobList;
    }
}
