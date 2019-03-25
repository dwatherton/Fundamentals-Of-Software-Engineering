import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet
{
    static final long serialVersionUID = 3L;
    
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/login.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        session.invalidate();
        
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        boolean loginSubmitted = request.getParameter("login").equals("Login");
        boolean authenticated = false;
        String passwordEntered = "";
        
        // Make sure the userID and password have been filled in
        if (!request.getParameter("userID").isEmpty() && !request.getParameter("password").isEmpty())
        {
            // Create a user from the login information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            
            try
            {                
                // Establish Database Connection - Creates Database & Table If not already Created
                Connection connection = DatabaseConnection.initializeDatabase();
                
                // Create Template Statement for querying the Database for the user's information based on userID
                PreparedStatement sql = connection.prepareStatement("select * from `users` where userID = ?");
                
                // Add the userID field to the select statement
                sql.setString(1, user.getUserID());
                
                // Execute the sql query and get the results
                ResultSet results = sql.executeQuery();
                
                while (results.next())
                {
                    // Get the users password from the results of the query
                    passwordEntered = results.getString("password");
                    out.write("GOT THE PASSWORD FOR USER => " + user.getUserID() + " WITH PASSWORD => " + passwordEntered + "\n");
                    out.write("THEY ENTERED THE PASSWORD => " + request.getParameter("password") + "\n");
                }
                
                // Close connections
                sql.close();
                connection.close();
            }
            catch (SQLException e)
            {
                out.write("Exception Thrown: " + e.getMessage() + "\n");
                out.write("Stack Trace: " + e.getStackTrace() + "\n");
            }            
            catch (ClassNotFoundException e)
            {
                out.write("Exception Thrown: " + e.getMessage() + "\n");
                out.write("Stack Trace: " + e.getStackTrace() + "\n");
            }
            
            // Verify User Credentials HERE!!! Make sure the password returned from the database is not empty!
            if (!passwordEntered.isEmpty())
            {
                // If password in DB matches password entered for the userID entered, they are authenticated (for this app anyways..)
                authenticated = passwordEntered.equals(user.getPassword());
            }
            
            // Create an HttpSession
            HttpSession session = request.getSession();
            
            // Set account Attribute to the users unique userID and registered/loggedin Attribute to true
            session.setAttribute("account", request.getParameter("userID"));
            session.setAttribute("loggedin", true);
           
            // Send User To /Home After Identity Verified!!!
            if (loginSubmitted && authenticated)
            {
                response.sendRedirect("/Home");        
            }
            else
            {
                // Now that I can check userID with password entered vs password in DB, check the variables in Login.jsp to make it show failed
                // If the passwords DONT match, but if they do match just let them redirect to home!
                out.write("Passwords do NOT match! Password in DB => " + passwordEntered + " Password entered in Login => " + request.getParameter("password"));
            }
        }
        else
        {
            // If either of the fields, userID or password are empty, log it.
            out.write("PLEASE FILL IN BOTH FIELDS, USERID AND PASSWORD! \n");
        }
    }
}
