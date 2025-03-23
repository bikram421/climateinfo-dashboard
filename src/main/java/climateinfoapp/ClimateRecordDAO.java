package climateinfoapp;

import org.tinylog.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for performing CRUD operations on climate records
 * in the database. It provides methods to insert, update, delete, and retrieve climate records.
 */
public class ClimateRecordDAO {
    private Connection jdbcConnection;

    /**
     * Default constructor for creating a DAO instance without an existing database connection.
     */
    public ClimateRecordDAO() {}

    /**
     * Constructs a DAO with a specific database connection.
     *
     * @param connection the connection to be used for database operations
     */
    public ClimateRecordDAO(Connection connection) {
        this.jdbcConnection = connection;
    }

    /**
     * Inserts a new climate record into the database.
     *
     * @param record the climate record to be inserted
     * @return true if the record was inserted successfully, false otherwise
     * @throws DatabaseException if an error occurs during the insertion process
     */
    public boolean insertClimateRecord(ClimateRecord record) throws DatabaseException {
        String sql = "INSERT INTO climate_data (date, location, temp, wind) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementParams(statement, record, false);
            boolean rowInserted = statement.executeUpdate() > 0;
            Logger.info("Record inserted successfully: {}", record);
            return rowInserted;

        } catch (SQLException e) {
            Logger.error(e, "Error inserting record: {}", record);
            throw new DatabaseException("Failed to insert climate record", e);
        }
    }

    /**
     * Updates an existing climate record in the database.
     *
     * @param record the climate record to be updated
     * @return true if the record was updated successfully, false otherwise
     * @throws DatabaseException if an error occurs during the update process
     */
    public boolean updateClimateRecord(ClimateRecord record) throws DatabaseException {
        String sql = "UPDATE climate_data SET date = ?, location = ?, temp = ?, wind = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementParams(statement, record, true);
            boolean rowUpdated = statement.executeUpdate() > 0;
            Logger.info("Record updated successfully: {}", record);
            return rowUpdated;

        } catch (SQLException e) {
            Logger.error(e, "Error updating record: {}", record);
            throw new DatabaseException("Failed to update climate record", e);
        }
    }

    /**
     * Deletes a climate record from the database based on its ID.
     *
     * @param id the ID of the record to be deleted
     * @return true if the record was deleted successfully, false otherwise
     * @throws DatabaseException if an error occurs during the deletion process
     */
    public boolean deleteClimateRecord(int id) throws DatabaseException {
        String sql = "DELETE FROM climate_data WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            boolean rowDeleted = statement.executeUpdate() > 0;
            Logger.info("Record deleted with ID: {}", id);
            return rowDeleted;

        } catch (SQLException e) {
            Logger.error(e, "Error deleting record with ID: {}", id);
            throw new DatabaseException("Failed to delete climate record", e);
        }
    }

    /**
     * Retrieves a climate record from the database by its ID.
     *
     * @param id the ID of the climate record to retrieve
     * @return the climate record with the given ID, or null if no record is found
     * @throws DatabaseException if an error occurs while retrieving the record
     * @throws InvalidArgumentsException 
     */
    public ClimateRecord getClimateRecord(int id) throws DatabaseException, InvalidArgumentsException {
        String sql = "SELECT * FROM climate_data WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToClimateRecord(resultSet);
                } else {
                    Logger.warn("No record found for ID: {}", id);
                    return null;
                }
            }

        } catch (SQLException e) {
            Logger.error(e, "Error fetching record with ID: {}", id);
            throw new DatabaseException("Failed to retrieve climate record", e);
        }
    }

    /**
     * Retrieves all climate records from the database.
     *
     * @return a list of all climate records in the database
     * @throws DatabaseException if an error occurs while retrieving the records
     * @throws InvalidArgumentsException 
     */
    public List<ClimateRecord> listAllClimateRecords() throws DatabaseException, InvalidArgumentsException {
        List<ClimateRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM climate_data";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                records.add(mapResultSetToClimateRecord(resultSet));
            }
            Logger.info("All records retrieved successfully.");
            return records;

        } catch (SQLException e) {
            Logger.error(e, "Error listing records.");
            throw new DatabaseException("Failed to retrieve climate records", e);
        }
    }

    /**
     * Sets the parameters for the PreparedStatement based on the given climate record.
     *
     * @param statement the PreparedStatement to set parameters for
     * @param record the climate record whose data will be used for setting parameters
     * @param includeId whether or not to include the ID in the statement (used for update operations)
     * @throws SQLException if an error occurs while setting the statement parameters
     */
    private void setStatementParams(PreparedStatement statement, ClimateRecord record, boolean includeId) throws SQLException {
        statement.setDate(1, Date.valueOf(record.getDate()));
        statement.setString(2, record.getLocation());
        statement.setFloat(3, record.getTemperature());
        statement.setFloat(4, record.getWind());
        if (includeId) {
            statement.setInt(5, record.getId());
        }
    }

    /**
     * Maps a ResultSet to a ClimateRecord object.
     *
     * @param resultSet the ResultSet containing the climate record data
     * @return the corresponding ClimateRecord object
     * @throws SQLException if an error occurs while mapping the ResultSet
     * @throws InvalidArgumentsException 
     */
    private ClimateRecord mapResultSetToClimateRecord(ResultSet resultSet) throws SQLException, InvalidArgumentsException {
        int id = resultSet.getInt("id");
        String date = resultSet.getString("date");
        String location = resultSet.getString("location");
        float temp = resultSet.getFloat("temp");
        float wind = resultSet.getFloat("wind");
        return new ClimateRecord(id, date, location, temp, wind);
    }

    /**
     * Returns a connection to the database. If the connection is null or closed, a new connection is created.
     *
     * @return the database connection
     * @throws SQLException if an error occurs while establishing the connection
     */
    private Connection getConnection() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            jdbcConnection = DBUtils.getConnection();
        }
        return jdbcConnection;
    }

	public List<ClimateRecord> getRecordsByCity(String city) throws DatabaseException, InvalidArgumentsException {
		List<ClimateRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM climate_data WHERE location like ?";

        try (Connection connection = getConnection();
        		PreparedStatement statement = connection.prepareStatement(sql)) {

        	
        	statement.setString(1, city);
        	
        	ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                records.add(mapResultSetToClimateRecord(resultSet));
            }
            Logger.info("All records retrieved successfully based on city.");
            return records;

        } catch (SQLException e) {
            Logger.error(e, "Error listing records based on city");
            throw new DatabaseException("Failed to retrieve climate records based on city", e);
        }
	}
}
