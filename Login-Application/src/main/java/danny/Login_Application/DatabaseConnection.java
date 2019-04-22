package danny.Login_Application;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.SQLException; 

public class DatabaseConnection
{
    protected static Connection initializeDatabase() throws SQLException, ClassNotFoundException
    {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://db:3306/";
        
        final String USER = "user";
        final String PASSWORD = "pass";
        
        // Establish Database Connection
        Class.forName(JDBC_DRIVER);
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        
        // Create 'login_application' Database
        PreparedStatement createDatabase = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS `login_application`;");
        createDatabase.executeUpdate();
        createDatabase.close();
        
        // Use 'login_application' Database
        PreparedStatement userDatabase = connection.prepareStatement("USE `login_application`;");
        userDatabase.executeUpdate();
        userDatabase.close();
        
        // Create Table 'users' in 'login_application' Database
        PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (userID VARCHAR(120) NOT NULL, password VARCHAR(120) NOT NULL, name VARCHAR(120) NOT NULL, securityQuestion VARCHAR(120) NOT NULL, securityAnswer VARCHAR(120) NOT NULL, PRIMARY KEY(userID));");
        createTable.executeUpdate();
        createTable.close();
        
        // Potential Solution for persistence - Upon Registration, Save User info to file, then parse the file here
        // looping through each line, and create an sql statement to insert a user into the db with the values
        // read in from file

        return connection;
    }
}
