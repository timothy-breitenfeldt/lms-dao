package com.smoothstack.lms.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:mysql://localhost:3306/library?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DatabaseUtil.DRIVER);
        Connection connection = DriverManager.getConnection(DatabaseUtil.URL, DatabaseUtil.USERNAME, DatabaseUtil.PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }

}
