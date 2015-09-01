package net.jnmap.net.jnmap.data.dao.mysql;

import org.junit.Before;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by lucas.
 */
public abstract class DAOTest {
    protected DataSource dataSource;
    protected Connection connnection;
    protected PreparedStatement statement;
    protected ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        this.dataSource = mock(DataSource.class);
        this.connnection = mock(Connection.class);
        this.statement = mock(PreparedStatement.class);
        this.resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connnection);
        when(connnection.prepareStatement(anyString())).thenReturn(this.statement);
        when(connnection.prepareStatement(anyString(), anyInt())).thenReturn(this.statement);
        when(statement.executeQuery()).thenReturn(resultSet);
    }
}
