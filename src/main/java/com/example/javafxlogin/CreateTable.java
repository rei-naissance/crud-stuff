package com.example.javafxlogin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void create() {
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "taskid INT AUTO_INCREMENT PRIMARY KEY, " +
                    "taskdescription VARCHAR(100) NOT NULL," +
                    "taskstatus BOOLEAN NOT NULL," +
                    "user_id INT, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
