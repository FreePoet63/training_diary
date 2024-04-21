package com.ylab.app.repository;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.dbService.migration.LiquibaseMigration;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.ylab.app.util.TestDataResult.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDaoTest class represents the test suite for testing the functionality of the User DAO (Data Access Object) class.
 * It utilizes Testcontainers for Docker-based integration testing.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
@Testcontainers
public class UserDaoTest {
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(getTestDatabaseVersion())
            .withDatabaseName(getTestDatabaseName())
            .withUsername(getTestUsername())
            .withPassword(getTestPassword());

    private static Connection testConnection;
    private static final LiquibaseMigration liquibaseMigration = new LiquibaseMigration();

    @BeforeAll
    public static void setUp() throws SQLException {
        testConnection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        liquibaseMigration.performLiquibaseMigration(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        ConnectionManager.setContainerCredentials(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        ConnectionManager.setConnectionOverride(testConnection);
    }

    @Test
    @DisplayName("Insert user into database")
    public void testInsertUser() throws SQLException {
        User user = new User("Alice", "1234", UserRole.USER);
        UserDaoImpl userDao = new UserDaoImpl();

        userDao.insertUser(user);

        User retrievedUser = userDao.findUserByNameAndPassword("Alice", "1234");
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getRole(), retrievedUser.getRole());
    }

    @Test
    @DisplayName("Find user by name and password with wrong credentials")
    public void testFindUserByNameAndPasswordWithWrongCredentials() throws SQLException {
        String wrongName = "Jim";
        String wrongPassword = "1111";
        UserDaoImpl userDao = new UserDaoImpl();
        User result = userDao.findUserByNameAndPassword(wrongName, wrongPassword);
        assertNull(result);
    }

    @Test
    @DisplayName("Find user by name and password with valid credentials")
    public void testFindUserByNameAndPasswordWithValidCredentials() throws SQLException {
        String validName = "Alice";
        String validPassword = "1234";
        UserRole validRole = UserRole.USER;
        UserDaoImpl userDao = new UserDaoImpl();

        User expectedUser = new User(validName, validPassword, validRole);
        userDao.insertUser(expectedUser);

        User result = userDao.findUserByNameAndPassword(validName, validPassword);

        assertNotNull(result);
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getPassword(), result.getPassword());
        assertEquals(expectedUser.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Get all users with non-empty database")
    public void testGetAllUsersWithNonEmptyDatabase() throws SQLException {
        User user1 = new User("Alice", "1234", UserRole.USER);
        User user2 = new User("Bob", "4321", UserRole.ADMIN);
        UserDaoImpl userDao = new UserDaoImpl();

        userDao.insertUser(user1);
        userDao.insertUser(user2);

        List<User> result = userDao.getAllUsers();

        assertFalse(result.isEmpty());
        assertThat(result)
                .isNotEmpty()
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsAnyOf(
                        tuple("Alice", "1234", UserRole.USER),
                        tuple("Bob", "4321", UserRole.ADMIN)
                );
    }
}