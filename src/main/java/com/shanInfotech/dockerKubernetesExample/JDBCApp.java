package com.shanInfotech.dockerKubernetesExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCApp {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/worklogdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Root@007";

    public static void main(String[] args) {
        String instanceId = System.getenv("INSTANCE_ID");
        String greeting = System.getenv("APP_GREETING");

        if (instanceId == null) instanceId = "LOCAL_INSTANCE";
        if (greeting == null) greeting = "Hello from replica!";

        logger.info("Starting DockerKubernetesExampleApplication: {}", instanceId);
        logger.info("Greeting: {}", greeting);

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            logger.info("Connected to the database");

            String createTableSQL = "CREATE TABLE  instance_log ("
                                  + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                  + "instance_id VARCHAR(100), "
                                  + "greeting TEXT, "
                                  + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }

            String insertSQL = "INSERT INTO instance_log (instance_id, greeting) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, instanceId);
                pstmt.setString(2, greeting);
                pstmt.executeUpdate();
                logger.info("Logged instance data");
            }

            logger.info("📋 Current log entries:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM instance_log")) {
                while (rs.next()) {
                    logger.info("➡ ID: {}, Instance: {}, Greeting: {}, Timestamp: {}",
                        rs.getInt("id"),
                        rs.getString("instance_id"),
                        rs.getString("greeting"),
                        rs.getTimestamp("created_at"));
                }
            }

        } catch (Exception e) {
            logger.error("DB Error", e);
        }

        while (true) {
            try {
                Thread.sleep(10000);
                logger.info("Running... Instance ID: {}", instanceId);
            } catch (InterruptedException e) {
                logger.error("Interrupted: ", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
