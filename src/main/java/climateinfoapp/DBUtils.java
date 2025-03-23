package climateinfoapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.tinylog.Logger;

/**
 * DBUtils is a utility class that handles the database connection setup and management.
 * It provides methods for initializing JDBC connection parameters, obtaining a connection,
 * and closing the connection after use.
 * <p>
 * This class is designed to work with PostgreSQL databases and uses the TinyLog library for logging.
 * It should be initialized using the {@link #init(ServletContext)} method before calling other methods.
 * </p>
 */
public class DBUtils {

    // Database connection parameters
    protected static String jdbcURL;
    protected static String jdbcUsername;
    protected static String jdbcPassword;

    /**
     * Initializes the JDBC settings by retrieving them from the servlet context.
     * It also registers the PostgreSQL JDBC driver.
     * <p>
     * This method should be called once during the servlet initialization phase to
     * set up the database connection settings. The values are expected to be configured
     * in the servlet's context parameters (e.g., web.xml).
     * </p>
     *
     * @param context The servlet context from which to retrieve JDBC connection settings.
     * @throws ExceptionInInitializerError if the JDBC driver class cannot be loaded or if there are other initialization issues.
     */
    public static void init(ServletContext context) {
        try {
            // Retrieve database connection settings from servlet context
            jdbcURL = context.getInitParameter("jdbcURL");
            jdbcUsername = context.getInitParameter("jdbcUsername");
            jdbcPassword = context.getInitParameter("jdbcPassword");

            // Register JDBC driver
            Class.forName("org.postgresql.Driver");

            Logger.info("*********DBUtils initialized with JDBC settings.");
        } catch (ClassNotFoundException e) {
            Logger.error(e, "Database driver not found.");
            throw new ExceptionInInitializerError("Database driver not found.");
        } catch (Exception e) {
            Logger.error(e, "Error initializing DBUtils.");
            throw new ExceptionInInitializerError("Error initializing DBUtils.");
        }
    }

    /**
     * Obtains a connection to the database using the initialized JDBC settings.
     * <p>
     * This method uses the connection parameters set during initialization (e.g., URL, username, and password)
     * to establish a connection to the database. It should be called whenever a connection is needed.
     * </p>
     *
     * @return A {@link Connection} object representing the connection to the database.
     * @throws SQLException if there is an error while establishing the connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Return the connection using stored settings
            return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            Logger.error(e, "Error establishing database connection.");
            throw e;
        }
    }

    /**
     * Closes the provided database connection if it is open.
     * <p>
     * This method ensures that the database connection is properly closed after use, freeing up resources.
     * It also logs an informational message when the connection is closed successfully.
     * </p>
     *
     * @param connection The {@link Connection} to be closed. If the connection is already closed, this method does nothing.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    Logger.info("Database connection closed.");
                }
            } catch (SQLException e) {
                Logger.warn(e, "Error while closing database connection.");
            }
        }
    }
}
