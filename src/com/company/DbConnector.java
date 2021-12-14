package com.company;

import java.sql.*;

/**
 * Database conncetor class
 */
public class DbConnector {
    /**
     * static method, that has the standard connection credentials
     * @return Connection object
     */
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


    /**
     * Gets the id of the row of the result of the query statement in the database
     * @param query SQL query that is used to query in AWS
     * @return integer id
     * @throws SQLException
     */
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


    /**
     * Mutates the rows in the table (Insert/Update)
     * @param query SQL query that is used to query in AWS
     * @throws SQLException
     */
    public void executeInsertUpdateStatement(String query) throws SQLException {
        Connection con = getConnections();
        Statement statement = con.createStatement();
        statement.executeUpdate(query);

    }

}
