package tv.bermu.cloverrpg.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 * Interface for database operations
 */
public class Database {

    private Connection connection;
    private Plugin plugin;

    public Database(Plugin plugin, ConfigurationSection dbConfigSection) {
        // Initialize database based on configuration
        this.plugin = plugin;
        String dbType = dbConfigSection.getString("type", "sqlite").toLowerCase();
        if (dbType.equals("sqlite")) {
            connectToSQLite(dbConfigSection);
        } else if (dbType.equals("mariadb")) {
            connectToMariaDB(dbConfigSection);
        } else {
            plugin.getLogger().warning("Invalid database type specified in config. Defaulting to SQLite.");
            connectToSQLite(dbConfigSection);
        }
    }

    /**
     * Get the database connection
     * 
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Connect to SQLite database
     * 
     * @param dbConfigSection Configuration section for the database
     */
    private void connectToSQLite(ConfigurationSection dbConfigSection) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + plugin.getDataFolder() + "/" + dbConfigSection.getString("database") + ".db");
            plugin.getLogger().info("SQLite database connected.");
            createTables("sqlite");
        } catch (ClassNotFoundException | SQLException e) {
            handleDatabaseError("SQLite", e);
        }
    }

    /**
     * Connect to MariaDB database
     * 
     * @param dbConfigSection Configuration section for the database
     */
    private void connectToMariaDB(ConfigurationSection dbConfigSection) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://" + dbConfigSection.getString("host") + ":" + dbConfigSection.getInt("port")
                    + "/" + dbConfigSection.getString("database");
            connection = DriverManager.getConnection(url, dbConfigSection.getString("username"),
                    dbConfigSection.getString("password"));
            plugin.getLogger().info("MariaDB database connected.");
            createTables("mariadb");
        } catch (ClassNotFoundException | SQLException e) {
            handleDatabaseError("MariaDB", e);
        }
    }

    /**
     * Create tables in the database
     * 
     * @param dbName Name of the db file
     */
    private void createTables(String dbName) {
        try (Statement stmt = connection.createStatement()) {
            // Load SQL file from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/" + dbName + ".sql");
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    StringBuilder sqlStatement = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        // Append each line to the StringBuilder
                        sqlStatement.append(line);
                        sqlStatement.append("\n");
                    }
                    // Execute SQL script
                    stmt.executeUpdate(sqlStatement.toString());
                    plugin.getLogger().info(dbName + " tables created.");
                }
            } else {
                plugin.getLogger().severe("Failed to load " + dbName + " SQL file.");
            }
        } catch (Exception e) {
            handleDatabaseError(dbName, e);
        }
    }

    /**
     * Handle database connection errors
     * 
     * @param dbName Name of the database
     * @param e
     */
    private void handleDatabaseError(String dbName, Exception e) {
        plugin.getLogger().severe("Failed to connect to " + dbName + " database: " + e.getMessage());
        e.printStackTrace(); // Log the stack trace for detailed error analysis
    }

    /**
     * Insert data into a table
     * 
     * @param tableName Table name.
     * @param columns   Columns to insert data into.
     * @param values    Values to insert into the columns.
     * @throws SQLException If an error occurs during the insert operation.
     */
    public void insertData(String tableName, String[] columns, Object[] values) throws SQLException {
        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columns.length; i++) {
            insertSQL.append(columns[i]);
            if (i < columns.length - 1) {
                insertSQL.append(", ");
            }
        }

        insertSQL.append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            insertSQL.append("?");
            if (i < columns.length - 1) {
                insertSQL.append(", ");
            }
        }
        insertSQL.append(")");

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL.toString())) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    /**
     * Update data in a table
     * 
     * @param tableName       Table name.
     * @param columnsToUpdate Columns to update.
     * @param newValues       New values to update the columns with.
     * @param condition       Condition to update the data.
     * @throws SQLException If an error occurs during the update operation.
     */
    public void updateData(String tableName, String[] columnsToUpdate, Object[] newValues, String condition)
            throws SQLException {
        StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < columnsToUpdate.length; i++) {
            updateSQL.append(columnsToUpdate[i]).append(" = ?");
            if (i < columnsToUpdate.length - 1)
                updateSQL.append(", ");
        }

        if (condition != null && !condition.isEmpty()) {
            updateSQL.append(" WHERE ").append(condition);
        }

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL.toString())) {
            for (int i = 0; i < newValues.length; i++) {
                pstmt.setObject(i + 1, newValues[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record updated successfully.");
        }
    }

    /**
     * Select data from a table
     * 
     * @param selectSQL SQL query to select data.
     * @return ResultSet with the selected data.
     * @throws SQLException
     */
    public ResultSet selectData(String selectSQL) throws SQLException {
        return connection.createStatement().executeQuery(selectSQL);
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Database connection closed.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to close database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}