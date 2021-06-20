package com.analyze.fileanalyzer;

import java.sql.*;

public class DatabaseQuery {

    public DatabaseQuery() {}

    public void getAllResults(Connection connection) {
        String query = "select * from fileanalyzer.tika;";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.printf("%s \t\t%s \t\t\t%s \n",
                        resultSet.getString("id"),
                        resultSet.getString("filename"),
                        resultSet.getString("metadata"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertQuery(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
