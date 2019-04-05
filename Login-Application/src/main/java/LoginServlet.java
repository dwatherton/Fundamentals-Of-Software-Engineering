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
        // Get the current session
        HttpSession session = request.getSession();

        // Set loginFieldEmpty for making sure user entered ALL fields
        Boolean loginFieldEmpty = (Boolean)session.getAttribute("loginFieldEmpty");
        request.setAttribute("loginFieldEmpty", loginFieldEmpty);
        
        // Set incorrectIdOrPassword for making sure user entered the correct login info
        Boolean incorrectIdOrPassword = (Boolean)session.getAttribute("incorrectIdOrPassword");
        request.setAttribute("incorrectIdOrPassword", incorrectIdOrPassword);
        
        // Invalidate session if user failed to register then came to login (Clears the variables set for ERROR messages on Registration form)
        if (session.getAttribute("registrationFieldEmpty") != null || session.getAttribute("userIdTaken") != null)
        {
            session.invalidate();
        }
        
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Create an HttpSession for Login
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        boolean loginSubmitted = false;
        boolean forgotPassword = false;
        boolean authenticated = false;
        String passwordEntered = "";
        
        // Check which button user selected, whichever is selected will not be null, and thus set to true so the request can be handled
        if (request.getParameter("login") != null)
        {
            loginSubmitted = request.getParameter("login").equals("Login");
        }
        else if (request.getParameter("forgotPassword") != null)
        {
            forgotPassword = request.getParameter("forgotPassword").equals("Forgot Password?");
        }
        
        // Check the user clicked Login
        if (loginSubmitted)
        {
            session.invalidate();
            
            // Create a user from the login information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            
            // Check that the user hasn't left any fields empty (REDIRECT TO LOGIN AND PROMPT USER TO ENTER ENTER ALL FIELDS)
            if (user.getUserID().equals("") || user.getPassword().equals(""))
            {
                // Get the current session
                session = request.getSession();
                
                // Set an attribute for empty login fields (For prompting user to fill out entire form)
                session.setAttribute("loginFieldEmpty", true);
                
                response.sendRedirect("/Login");
                
                // Return (Don't allow them to register unless all fields were entered)
                return;
            }
            // User entered information into ALL fields, proceed (ALL FIELDS WERE ENTERED - PROCEED WITH CHECKS)
            else
            {  
                try
                {                
                    // Establish Database Connection - Creates Database & Table If not already Created
                    Connection connection = DatabaseConnection.initializeDatabase();
                    
                    // Create Template Statement for querying the Database for the user's information based on userID
                    PreparedStatement sqlCheckUserPassword = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the select statement
                    sqlCheckUserPassword.setString(1, user.getUserID());
                    
                    // Execute the sql query and get the results
                    ResultSet results = sqlCheckUserPassword.executeQuery();
                    
                    // If the query returned results, get the password for the user
                    if (results.next())
                    {
                        // Set authenticated to true if the password entered matches the password in DB
                        authenticated = user.getPassword().equals(results.getString("password"));
                        
                        // Close sqlCheckUserPassword connection
                        sqlCheckUserPassword.close();
                    }
                   
                    // Check if the user has been authenticated to login
                    if (authenticated)
                    {
                        // Create a new session for the logged in user
                        session = request.getSession(true);
                        
                        // Set account Attribute to the users unique userID and registered/loggedin Attribute to true
                        session.setAttribute("account", request.getParameter("userID"));
                        session.setAttribute("loggedin", true);
                        
                        response.sendRedirect("/Home");        
                    }
                    else
                    {
                        // Get the current session
                        session = request.getSession();
                        
                        // Set an attribute for incorrect userID or Password (For prompting user to retry logging in with correct info)
                        session.setAttribute("incorrectIdOrPassword", true);
                        
                        response.sendRedirect("/Login");
                
                        // Return (Don't allow them to register unless all fields were entered)
                        return;
                    }
                    
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
            }
        }
        // If the user didn't click login, check if they hit the Forgot Password? button
        else if (forgotPassword)
        {
            // Send the user to the Forgot Password Page
            response.sendRedirect("/Home");
        }
    }
}
