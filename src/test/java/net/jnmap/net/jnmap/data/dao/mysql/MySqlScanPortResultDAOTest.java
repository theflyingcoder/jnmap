package net.jnmap.net.jnmap.data.dao.mysql;

import junit.framework.Assert;
import net.jnmap.data.Port;
import net.jnmap.data.ScanPortResult;
import net.jnmap.data.dao.mysql.MySqlScanPortResultDAO;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

import static junit.framework.Assert.*;
import static net.jnmap.TestConstants.TEST_JOB_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by lucas.
 */
public class MySqlScanPortResultDAOTest extends DAOTest {
    @Test
    public void testCreate() throws SQLException {

        Integer ports[] = {80,8080,443};
        String[] portStates = {"filtered", "open", "closed"};
        String[] portServices = {"http", "http-tomcat", "https"};
        String[] portProtocol = {"tcp1", "tcp2", "tcp3"};


        ScanPortResult result = new ScanPortResult();
        Port port1 = new Port();
        port1.setPort(ports[0]);
        port1.setState(portStates[0]);
        port1.setService(portServices[0]);
        port1.setProtocol(portProtocol[0]);

        Port port2 = new Port();
        port2.setPort(ports[1]);
        port2.setState(portStates[1]);
        port2.setService(portServices[1]);
        port2.setProtocol(portProtocol[1]);

        Port port3 = new Port();
        port3.setPort(ports[2]);
        port3.setState(portStates[2]);
        port3.setService(portServices[2]);
        port3.setProtocol(portProtocol[2]);

        result.addPort(port1);
        result.addPort(port2);
        result.addPort(port3);


        MySqlScanPortResultDAO scanPortResultDAO = new MySqlScanPortResultDAO(dataSource);
        scanPortResultDAO.create(TEST_JOB_ID, result);

        ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> portCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Timestamp> timestampCaptor = ArgumentCaptor.forClass(Timestamp.class);

        verify(statement, times(3)).setLong(indexCaptor.capture(), idCaptor.capture());
        verify(statement, times(3)).setInt(indexCaptor.capture(), portCaptor.capture());
        verify(statement, times(9)).setString(indexCaptor.capture(), stringCaptor.capture());
        verify(statement, times(3)).setTimestamp(indexCaptor.capture(), timestampCaptor.capture());

        verify(statement, times(3)).addBatch();

        verify(statement).executeBatch();

        assertEquals(Arrays.asList(new Long[]{TEST_JOB_ID, TEST_JOB_ID, TEST_JOB_ID}), idCaptor.getAllValues());
        assertEquals(Arrays.asList(ports), portCaptor.getAllValues());
        assertTrue(stringCaptor.getAllValues().containsAll(Arrays.asList(portStates)));
        assertTrue(stringCaptor.getAllValues().containsAll(Arrays.asList(portServices)));
        assertTrue(stringCaptor.getAllValues().containsAll(Arrays.asList(portProtocol)));
    }
}
