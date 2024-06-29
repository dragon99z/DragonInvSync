package de.dragon99z.dragoninvsync.Func;

import java.sql.*;

import static de.dragon99z.dragoninvsync.DragonInvSync.config;
import static de.dragon99z.dragoninvsync.DragonInvSync.LOGGER;

/**
 * Represents a utility class for establishing, managing, and validating connections to a MySQL database using JDBC driver.
 * Provides methods for connecting to the database, disconnecting from it, checking the connection status, and validating records in a specified table.
 */
public class MySQL {

    public static Connection con;

    /**
     * Establishes a connection to a MySQL database using JDBC driver.
     * Constructs connection URL from configuration parameters and attempts to connect.
     * Logs success or error messages based on connection status.
     */
    public static void connect() {
        if (!config.validateMySQL()) {
            LOGGER.error("Invalid MySQL configuration - Stopping");
            return;
        }

        String url = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8&useSSL=false&autoReconnect=true",
                config.mysql.address, config.mysql.port, config.database.name);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, config.mysql.username, config.mysql.password);
            LOGGER.info("MySQL connected!");
        } catch (Exception e) {
            LOGGER.error("Could not connect to MySQL{}", config.mysql.debug ? ": " + e : "!");
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes the connection to the MySQL database.
     * Logs a debug message indicating the closure of the connection.
     * If an SQLException occurs during the closing process, it logs an error message.
     */
    public static void disconnect() {
        try {
            LOGGER.debug("Closing MySQL connection...");
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Error while disconnecting from MySQL: {}", e);
        } finally {
            con = null;
        }
    }

    /**
     * Checks the status of the connection to the MySQL database.
     * Logs the connection status (connected or disconnected) using the LOGGER in debug mode.
     * Returns true if the connection is not null and is not closed, false otherwise.
     * If an SQLException occurs during the check, logs an error message and returns false.
     *
     * @return true if the connection is open, false if it is closed or null
     */
    public static synchronized boolean hasConnection() {
        LOGGER.debug("Connection status: {}", con != null ? "Connected" : "Disconnected");
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            LOGGER.error("Error while checking connection status: {}", e);
            return false;
        }
    }

    /**
     * Validates the existence of records in a specified table within the connected MySQL database.
     *
     * @param table the name of the table to validate records in
     * @throws SQLException if an error occurs while accessing the database or executing the query
     */
    public static void validate(String table) throws SQLException {
        if (!hasConnection()) {
            return;
        }

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {

            rs.next();
        }
    }


}
