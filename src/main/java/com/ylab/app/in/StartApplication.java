package com.ylab.app.in;

import com.ylab.app.dbService.migration.LiquibaseMigration;
import com.ylab.app.web.servlet.UserServlet;
import com.ylab.app.web.servlet.WorkoutServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import static com.ylab.app.util.DataResultDatabase.*;

/**
 * StartApplication class
 *
 * Entry point for the application providing user registration, login, and workout management functionalities.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class StartApplication {
    private final LiquibaseMigration liquibaseMigration = new LiquibaseMigration();

    /**
     * The entry point of the application. Initializes Liquibase migration, sets up the server, and configures servlets for user and workout handling.
     *
     * @param args the input arguments
     * @throws Exception if an error occurs during application execution
     */
    public static void main(String[] args) throws Exception {
        new StartApplication().initializeApplication();
    }

    /**
     * Initializes the application by performing Liquibase migration, setting up the server, and configuring servlets for user and workout management.
     *
     * @throws Exception if an error occurs during application initialization
     */
    private void initializeApplication() throws Exception {
        liquibaseMigration.performLiquibaseMigration(getUrl() + getDatabaseName(), getUsername(), getPassword());

        Server server = new Server(8099);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new UserServlet()), "/users/*");
        context.addServlet(new ServletHolder(new WorkoutServlet()), "/workout/*");

        server.start();
        System.out.println("\u001B[31m" + "YLAB Server Started" + "\u001B[0m");

        server.join();
    }
}
