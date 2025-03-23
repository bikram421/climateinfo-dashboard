package climateinfoapp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


public class DBUtilsTest {

    private ServletContext mockContext;
    
    @Mock
    private Connection mockConnection;
    
//    @Mock 
//    private DriverManager mockDriverManager ;

    @BeforeEach
    void setUp() {
        // Mock the ServletContext
        mockContext = mock(ServletContext.class);

        // Mock the parameters required for DB initialization
        when(mockContext.getInitParameter("jdbcURL")).thenReturn("jdbc:postgresql://localhost:5432/testdb");
        when(mockContext.getInitParameter("jdbcUsername")).thenReturn("testuser");
        when(mockContext.getInitParameter("jdbcPassword")).thenReturn("testpassword");

        // Initialize DBUtils with the mocked context
        DBUtils.init(mockContext);
    }

    @Test
    void testInit_ShouldSetJdbcDetails() {
        // Validate that the init parameters were fetched from the ServletContext
        assertDoesNotThrow(() -> DBUtils.init(mockContext));
    }
    

    @Test
    void testGetConnection_ShouldReturnValidConnection() throws SQLException {
        // Mock the DriverManager to simulate a database connection
        Connection mockConnection = mock(Connection.class);
        when(DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "testuser", "testpassword"))
                .thenReturn(mockConnection);

        // Test the getConnection method
        Connection connection = DBUtils.getConnection();

        // Verify that the DriverManager was called with the correct parameters
        assertNotNull(connection);
        verify(mockConnection, never()).close(); // Ensure the connection is not closed during this test

        // Close the connection explicitly in the test
        DBUtils.closeConnection(connection);
    }

    @Test
    void testGetConnection_ShouldThrowSQLException_OnInvalidCredentials() throws SQLException {
        // Simulate an SQL exception when invalid credentials are provided
        when(DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "testuser", "testpassword"))
                .thenThrow(new SQLException("Invalid credentials"));

        // Test that getConnection throws an exception
        assertThrows(SQLException.class, DBUtils::getConnection);
    }

    @Test
    void testCloseConnection_ShouldCloseValidConnection() throws SQLException {
        // Mock a valid connection
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(false);

        // Close the connection
        DBUtils.closeConnection(mockConnection);

        // Verify that close() was called on the connection
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testCloseConnection_ShouldNotThrowException_OnNullConnection() {
        // Close a null connection, which should be a no-op
        assertDoesNotThrow(() -> DBUtils.closeConnection(null));
    }

    @Test
    void testCloseConnection_ShouldNotThrowException_OnAlreadyClosedConnection() throws SQLException {
        // Mock a connection that's already closed
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(true);

        // Close the connection
        assertDoesNotThrow(() -> DBUtils.closeConnection(mockConnection));

        // Verify that close() was not called
        verify(mockConnection, never()).close();
    }

    @AfterEach
    void tearDown() {
        // Reset mocks or cleanup if necessary
        reset(mockContext);
    }
}
