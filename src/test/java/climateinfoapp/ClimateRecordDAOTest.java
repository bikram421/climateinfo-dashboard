package climateinfoapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClimateRecordDAOTest {

    private ClimateRecordDAO climateRecordDAO;

    @Mock 
    private ServletContext mockContext;

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        climateRecordDAO = new ClimateRecordDAO(mockConnection); 
//        climateRecordDAO.jdbcConnection = mockConnection;
    }

    @Test
    void testInsertClimateRecord() throws SQLException, DatabaseException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord("2024-11-24", "New York", 25.5f, 12.5f);

        // Mock the PreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        boolean result = climateRecordDAO.insertClimateRecord(record);

        // Then
        assertTrue(result);
        verify(mockPreparedStatement).setDate(1, Date.valueOf(record.getDate()));
        verify(mockPreparedStatement).setString(2, record.getLocation());
        verify(mockPreparedStatement).setFloat(3, record.getTemperature());
        verify(mockPreparedStatement).setFloat(4, record.getWind());
    }

    @Test
    void testInsertClimateRecordSQLException() throws SQLException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord("2024-11-24", "New York", 25.5f, 12.5f);

        // Mock SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        // When & Then
        try {
            climateRecordDAO.insertClimateRecord(record);
        } catch (DatabaseException e) {
            assertEquals("Failed to insert climate record", e.getMessage());
        }
    }

    @Test
    void testListAllClimateRecords() throws SQLException, DatabaseException, InvalidArgumentsException {
        // Mock the result set
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Two rows in result set
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("date")).thenReturn("2024-11-24", "2024-11-25");
        when(mockResultSet.getString("location")).thenReturn("New York", "Los Angeles");
        when(mockResultSet.getFloat("temp")).thenReturn(25.5f, 26.5f);
        when(mockResultSet.getFloat("wind")).thenReturn(12.5f, 14.5f);

        // When
        List<ClimateRecord> result = climateRecordDAO.listAllClimateRecords();

        // Then
        assertEquals(2, result.size());
        verify(mockStatement).executeQuery(any(String.class));
        verify(mockResultSet, times(3)).next();
    }

    @Test
    void testListAllClimateRecordsSQLException() throws SQLException, InvalidArgumentsException {
        // Mock SQLException
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        // When & Then
        try {
            climateRecordDAO.listAllClimateRecords();
        } catch (DatabaseException e) {
            assertEquals("Failed to retrieve climate records", e.getMessage());
        }
    }

    @Test
    void testDeleteClimateRecord() throws SQLException, DatabaseException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord(1, "2024-11-24", "New York", 25.5f, 12.5f);

        // Mock the PreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        boolean result = climateRecordDAO.deleteClimateRecord(record.getId());

        // Then
        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, record.getId());
    }

    @Test
    void testDeleteClimateRecordSQLException() throws SQLException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord(1, "2024-11-24", "New York", 25.5f, 12.5f);

        // Mock SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        // When & Then
        try {
            climateRecordDAO.deleteClimateRecord(record.getId());
        } catch (DatabaseException e) {
            assertEquals("Failed to delete climate record", e.getMessage());
        }
    }

    @Test
    void testUpdateClimateRecord() throws SQLException, DatabaseException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord(1, "2024-11-24", "New York", 27.5f, 14.5f);

        // Mock the PreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        boolean result = climateRecordDAO.updateClimateRecord(record);

        // Then
        assertTrue(result);
        verify(mockPreparedStatement).setDate(1, Date.valueOf(record.getDate()));
        verify(mockPreparedStatement).setString(2, record.getLocation());
        verify(mockPreparedStatement).setFloat(3, record.getTemperature());
        verify(mockPreparedStatement).setFloat(4, record.getWind());
        verify(mockPreparedStatement).setInt(5, record.getId());
    }

    @Test
    void testUpdateClimateRecordSQLException() throws SQLException, InvalidArgumentsException {
        ClimateRecord record = new ClimateRecord(1, "2024-11-24", "New York", 27.5f, 14.5f);

        // Mock SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        // When & Then
        try {
            climateRecordDAO.updateClimateRecord(record);
        } catch (DatabaseException e) {
            assertEquals("Failed to update climate record", e.getMessage());
        }
    }

    @Test
    void testGetClimateRecord() throws SQLException, DatabaseException, InvalidArgumentsException {
        int recordId = 1;
        ClimateRecord expectedRecord = new ClimateRecord(recordId, "2024-11-24", "New York", 25.5f, 12.5f);

        // Mock the PreparedStatement and ResultSet
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(recordId);
        when(mockResultSet.getString("date")).thenReturn("2024-11-24");
        when(mockResultSet.getString("location")).thenReturn("New York");
        when(mockResultSet.getFloat("temp")).thenReturn(25.5f);
        when(mockResultSet.getFloat("wind")).thenReturn(12.5f);

        // When
        ClimateRecord result = climateRecordDAO.getClimateRecord(recordId);

        // Then
        assertNotNull(result);
        assertEquals(expectedRecord.getId(), result.getId());
        assertEquals(expectedRecord.getDate(), result.getDate());
        assertEquals(expectedRecord.getLocation(), result.getLocation());
        assertEquals(expectedRecord.getTemperature(), result.getTemperature());
        assertEquals(expectedRecord.getWind(), result.getWind());
    }

    @Test
    void testGetClimateRecordNotFound() throws SQLException, DatabaseException, InvalidArgumentsException {
        int recordId = 1;

        // Mock the PreparedStatement and ResultSet for no matching record
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);  // No matching record

        // When
        ClimateRecord result = climateRecordDAO.getClimateRecord(recordId);

        // Then
        assertEquals(null, result);  // No record should be returned
    }

    @Test
    void testGetClimateRecordSQLException() throws SQLException, InvalidArgumentsException {
        int recordId = 1;

        // Mock SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Database error"));

        // When & Then
        try {
            climateRecordDAO.getClimateRecord(recordId);
        } catch (DatabaseException e) {
            assertEquals("Failed to retrieve climate record", e.getMessage());
        }
    }
}
