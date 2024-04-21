package com.ylab.app.dbService.migration;

import com.ylab.app.exception.dbException.DatabaseConnectionException;
import com.ylab.app.exception.dbException.LiquibaseMigrationException;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.ylab.app.util.DataResultDatabase.*;
import static com.ylab.app.util.DataResultLiquibaseMigration.getDefaultSchemaName;

/**
 * The LiquibaseMigration class provides a method for performing Liquibase migration on the database.
 * It includes a method for executing Liquibase migration using the specified database connection details and schema information.
 * @author razlivinsky
 * @since 31.01.2024
 */
public class LiquibaseMigration {

    /**
     * Performs Liquibase migration on the database using the provided database connection details and schema information.
     */
    public void performLiquibaseMigration(String url, String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = conn.createStatement()) {String defaultSchemaName = getDefaultSchemaName();
                statement.execute("CREATE SCHEMA IF NOT EXISTS " + defaultSchemaName);

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
                new CommandScope(UpdateCommandStep.COMMAND_NAME)
                        .addArgumentValue("url", url)
                        .addArgumentValue("username", username)
                        .addArgumentValue("password", password)
                        .addArgumentValue("changeLogFile", "db/changelog/liquibase-changelog.xml")
                        .addArgumentValue("defaultSchemaName", defaultSchemaName)
                        .execute();

                System.out.println("Migration successfully executed.");
            } catch (Exception e) {
                throw new LiquibaseMigrationException("Error executing migration: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database: " + e.getMessage());
        }
    }
}