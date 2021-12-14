package com.company;

import java.sql.*;

public class DbConnector {
    public static Connection getConnections(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://chessdb.cy4fxyj9rirt.us-east-2.rds.amazonaws.com:3306/chessdb", "admin",
                "adminadmin");
        return con;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getTopIdStatement(String query) throws SQLException {
        try {
        Connection con = getConnections();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next())
            return resultSet.getInt(1);
        return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    public void executeInsertUpdateStatement(String query) throws SQLException {
        Connection con = getConnections();
        Statement statement = con.createStatement();
        statement.executeUpdate(query);

    }

}
