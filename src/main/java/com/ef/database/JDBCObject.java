package com.ef.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author .com Simple Hello World MySQL Tutorial on how to make JDBC
 * connection, Add and Retrieve Data by App Shah
 *
 */
public class JDBCObject {

    static Connection conn = null;
    static PreparedStatement prepareStat = null;

    public static void main(String[] argv) {

        try {
            makeJDBCConnection();

            log("\n---------- Adding company ' LLC' to DB ----------");
            addDataToDB(", LLC.", "NYC, US", 5, "http://.com");
            addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
            addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");

            log("\n---------- Let's get Data from DB ----------");
            getDataFromDB();

            prepareStat.close();
            conn.close(); // connection close

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            log("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
            if (conn != null) {
                log("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                log("Failed to make connection!");
            }
        } catch (SQLException e) {
            log("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }

    }

    public static void addDataToDB(String companyName, String address, int totalEmployee, String webSite) {

        try {
            String insertQueryStatement = "INSERT  INTO  Employee  VALUES  (?,?,?,?)";

            prepareStat = conn.prepareStatement(insertQueryStatement);
            prepareStat.setString(1, companyName);
            prepareStat.setString(2, address);
            prepareStat.setInt(3, totalEmployee);
            prepareStat.setString(4, webSite);

            // execute insert SQL statement
            prepareStat.executeUpdate();
            log(companyName + " added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getDataFromDB() {

        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * FROM employee";

            prepareStat = conn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = prepareStat.executeQuery();

            // Let's iterate through the java ResultSet
            while (rs.next()) {
                String name = rs.getString("Name");
                String address = rs.getString("Address");
                int employeeCount = rs.getInt("EmployeeCount");
                String website = rs.getString("Website");

                // Simply Print the results
                System.out.format("%s, %s, %s, %s\n", name, address, employeeCount, website);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }
}
