package net.jnmap.net.jnmap.data.dao.mysql;

import net.jnmap.TestConstants;
import net.jnmap.data.ScanJob;
import net.jnmap.data.dao.mysql.MySqlScanJobDAO;
import net.jnmap.scanner.Job;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static net.jnmap.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by lucas.
 */
public class MySqlScanJobDAOTest extends DAOTest {

    @Test
    public void testCreateNull() {
        MySqlScanJobDAO sqlScanJobDAO = new MySqlScanJobDAO(dataSource);
        Assert.assertNull(sqlScanJobDAO.create(StringUtils.EMPTY, StringUtils.EMPTY));
    }

    @Test
    public void testCreate() throws SQLException {

        when(resultSet.getLong(Mockito.anyInt())).thenReturn(TEST_JOB_ID);
        when(resultSet.next()).thenReturn(Boolean.TRUE);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);

        MySqlScanJobDAO sqlScanJobDAO = new MySqlScanJobDAO(dataSource);
        Job job = sqlScanJobDAO.create(TEST_TARGET, TestConstants.TEST_COMMAND_PREFIX);
        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        // Verify statement setting parameters
        verify(statement, Mockito.times(3)).setString(indexCaptor.capture(), valueCaptor.capture());
        assertEquals(
                Arrays.asList(1, 2, 3),
                indexCaptor.getAllValues());
        assertEquals(
                Arrays.asList(TEST_TARGET, TEST_TARGET, TestConstants.TEST_COMMAND_PREFIX),
                valueCaptor.getAllValues());

        // Verify that timestamp returned is timestamp used to set to database
        ArgumentCaptor<Timestamp> timestampArgumentCaptor = ArgumentCaptor.forClass(Timestamp.class);
        verify(statement).setTimestamp(indexCaptor.capture(), timestampArgumentCaptor.capture());
        Timestamp expectedTimestamp = timestampArgumentCaptor.getValue();

        verify(statement).executeUpdate();

        // Verify created job having expected values
        assertEquals(TEST_JOB_ID, job.getId());
        assertEquals(TestConstants.TEST_COMMAND_PREFIX, job.getCommand());
        assertEquals(TEST_TARGET, job.getTarget());
        assertEquals(expectedTimestamp, job.getCreateTime());
    }

    @Test
    public void testUpdate() throws SQLException {
        ScanJob job = new ScanJob(TEST_TARGET);
        job.setTargetStatus(TEST_TARGET_STATUS_UP);
        job.setId(TEST_JOB_ID);
        job.setElapsedSecs(TEST_ELAPSED);

        MySqlScanJobDAO sqlScanJobDAO = new MySqlScanJobDAO(dataSource);
        sqlScanJobDAO.update(job);

        ArgumentCaptor<Float> elapsedCaptor = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement).setFloat(indexCaptor.capture(), elapsedCaptor.capture());
        verify(statement).setLong(indexCaptor.capture(), idCaptor.capture());
        verify(statement).setString(indexCaptor.capture(), statusCaptor.capture());
        verify(statement).executeUpdate();

        assertEquals(TEST_ELAPSED, elapsedCaptor.getValue(), 0);
        assertEquals(TEST_JOB_ID, idCaptor.getValue(), 0);
        assertEquals(TEST_TARGET_STATUS_UP, statusCaptor.getValue());
    }

    @Test
    public void testGet() throws SQLException {
        // Setup for 4 records which will produce 3 scan job because 2 of the ids are the same
        Long[] jobIds = {
                TEST_JOB_ID1,
                TEST_JOB_ID,
                TEST_JOB_ID,
                TEST_JOB_ID2
        };

        String[] targets = {
                TEST_TARGET_STATUS_UP,
                TEST_TARGET_STATUS_DOWN,
                TEST_TARGET_STATUS_UP,
        };

        Timestamp[] timestamps = {
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        };

        int port[] = {80,8080,443,8443};
        String[] portStates = {"filtered", "open", "closed", "open"};
        String[] portServices = {"http", "http-tomcat", "https", "https-tomcat"};

        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(resultSet.getLong(1))
                .thenReturn(jobIds[0])
                .thenReturn(jobIds[1])
                .thenReturn(jobIds[2])
                .thenReturn(jobIds[3]);

        when(resultSet.getString(2))
                .thenReturn(targets[0])
                .thenReturn(targets[1])
                .thenReturn(targets[2]);

        when(resultSet.getTimestamp(3))
                .thenReturn(timestamps[0])
                .thenReturn(timestamps[1])
                .thenReturn(timestamps[2]);

        when(resultSet.getInt(4))
                .thenReturn(port[0])
                .thenReturn(port[1])
                .thenReturn(port[2])
                .thenReturn(port[3]);

        when(resultSet.getString(5))
                .thenReturn(portStates[0])
                .thenReturn(portStates[1])
                .thenReturn(portStates[2])
                .thenReturn(portStates[3]);

        when(resultSet.getString(6))
                .thenReturn(portServices[0])
                .thenReturn(portServices[1])
                .thenReturn(portServices[2])
                .thenReturn(portServices[3]);

        MySqlScanJobDAO scanJobDAO = new MySqlScanJobDAO(dataSource);
        List<Job> returnedJobList = scanJobDAO.get(TEST_TARGET, 10);

        assertEquals(new HashSet<>(Arrays.asList(jobIds)).size(), returnedJobList.size());

        assertEquals(TEST_JOB_ID1, returnedJobList.get(0).getId());
        assertEquals(TEST_JOB_ID, returnedJobList.get(1).getId());
        assertEquals(TEST_JOB_ID2, returnedJobList.get(2).getId());

        assertEquals(TEST_TARGET_STATUS_UP, returnedJobList.get(0).getTargetStatus());
        assertEquals(TEST_TARGET_STATUS_DOWN, returnedJobList.get(1).getTargetStatus());
        assertEquals(TEST_TARGET_STATUS_UP, returnedJobList.get(2).getTargetStatus());

        assertEquals(timestamps[0], returnedJobList.get(0).getCreateTime());
        assertEquals(timestamps[1], returnedJobList.get(1).getCreateTime());
        assertEquals(timestamps[2], returnedJobList.get(2).getCreateTime());

        assertEquals(port[0], returnedJobList.get(0).getResult().getPorts().get(0).getPort());
        assertEquals(port[1], returnedJobList.get(1).getResult().getPorts().get(0).getPort());
        assertEquals(port[2], returnedJobList.get(1).getResult().getPorts().get(1).getPort());
        assertEquals(port[3], returnedJobList.get(2).getResult().getPorts().get(0).getPort());

        assertEquals(portStates[0], returnedJobList.get(0).getResult().getPorts().get(0).getState());
        assertEquals(portStates[1], returnedJobList.get(1).getResult().getPorts().get(0).getState());
        assertEquals(portStates[2], returnedJobList.get(1).getResult().getPorts().get(1).getState());
        assertEquals(portStates[3], returnedJobList.get(2).getResult().getPorts().get(0).getState());

        assertEquals(portServices[0], returnedJobList.get(0).getResult().getPorts().get(0).getService());
        assertEquals(portServices[1], returnedJobList.get(1).getResult().getPorts().get(0).getService());
        assertEquals(portServices[2], returnedJobList.get(1).getResult().getPorts().get(1).getService());
        assertEquals(portServices[3], returnedJobList.get(2).getResult().getPorts().get(0).getService());

    }
}
