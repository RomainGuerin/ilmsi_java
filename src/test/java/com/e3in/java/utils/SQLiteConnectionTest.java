package com.e3in.java.utils;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLiteConnectionTest {

    private SQLiteConnection sqliteConnection;

    @BeforeEach
    void setUp() {
        sqliteConnection = SQLiteConnection.getInstance();
    }

    @Test
    @Order(1)
    void testGetInstance() {
        SQLiteConnection anotherSqliteConnection = SQLiteConnection.getInstance();
        assertSame(sqliteConnection, anotherSqliteConnection);
    }

    @Test
    @Order(2)
    void testGetConnection() throws SQLException {
        Connection connection = sqliteConnection.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test
    @Order(3)
    void testClose() throws SQLException {
        Connection connection = sqliteConnection.getConnection();
        sqliteConnection.finalize();
        assertFalse(connection.isValid(0));
    }
}
