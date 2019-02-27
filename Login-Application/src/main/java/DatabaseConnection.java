import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.SQLException; 

public class DatabaseConnection
{
    protected static Connection initializeUsersDatabase() throws SQLException, ClassNotFoundException
    {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://db:3306/";
        
        final String USER = "daniel_atherton";
        final String PASSWORD = "password";
        
        Class.forName(JDBC_DRIVER);
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        
        // Create 'users' Database
        PreparedStatement createDatabase = connection.prepareStatement("create database if not exists users;");
        createDatabase.executeUpdate();
        createDatabase.close();
        
        // Use 'users' Database
        PreparedStatement userDatabase = connection.prepareStatement("use users;");
        userDatabase.executeUpdate();
        userDatabase.close();
        
        // Create Table 'user' in users Database
        PreparedStatement createTable = connection.prepareStatement("create table if not exists user (userID varchar(120) NOT NULL, password varchar(120) NOT NULL, name varchar(120) NOT NULL, securityQuestion varchar(120) NOT NULL, PRIMARY KEY(userID));");
        createTable.executeUpdate();
        createTable.close();

        return connection;
    }
}
