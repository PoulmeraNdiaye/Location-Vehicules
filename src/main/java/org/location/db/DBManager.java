package org.location.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static Connection connection = null;

    public static Connection getConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbvehicules-hbm?serverTimezone=UTC&useSSL=false",
                    "root",
                    ""
            );
            return connection;
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver Class not found: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Error: Unable to open connection with database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}