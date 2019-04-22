package danny.Login_Application;

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
    private static final long serialVersionUID = 3L;

    public static final String JSP_VIEW_PATH = "/WEB-INF/jsp/login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();

        // Set loginFieldEmpty for making sure user entered ALL fields in Login Form (Changes Login page content)
        Boolean loginFieldEmpty = (Boolean)session.getAttribute("loginFieldEmpty");
        request.setAttribute("loginFieldEmpty", loginFieldEmpty);
        
        // Set incorrectIdOrPassword for making sure user entered the correct login info (Changes Login page content)
        Boolean incorrectIdOrPassword = (Boolean)session.getAttribute("incorrectIdOrPassword");
        request.setAttribute("incorrectIdOrPassword", incorrectIdOrPassword);
        
        // Invalidate HttpSession if User failed to register or reset password then came to login (Clear any HttpSession Attributes set) 
        if (session.getAttribute("registrationFieldEmpty") != null || session.getAttribute("userIdTaken") != null
            || session.getAttribute("forgotPasswordFieldEmpty") != null || session.getAttribute("incorrectRecoveryInfo") != null
            || session.getAttribute("passwordsDoNotMatch") != null)
        {
            session.invalidate();
        }
        
        // Forward HttpRequests and HttpResponses to login.jsp (Tie this Servlet to it's respective JSP View)
        request.getRequestDispatcher(JSP_VIEW_PATH).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();
        
        // Get the HttpResponses PrintWriter for printing Servlet and IO Exceptions to screen
        PrintWriter out = response.getWriter();
        
        // Create loginSubmitted variable for checking if the User clicked the Login Button
        boolean loginSubmitted = false;
        
        // Create forgotPasswordSubmitted variable for checking if the User clicked the Forgot Password? Button
        boolean forgotPassword = false;
        
        // Create authenticated variable for checking if the User entered info has been verified with the Server
        boolean authenticated = false;
        
        // Create passwordEntered variable for checking that the User entered password matches the password in the Database
        String passwordEntered = "";
        
        // Check which button the User selected (Selected Button will NOT be null, so the respective variable is set to True)
        if (request.getParameter("login") != null)
        {
            loginSubmitted = request.getParameter("login").equals("Login");
        }
        else if (request.getParameter("forgotPassword") != null)
        {
            forgotPassword = request.getParameter("forgotPassword").equals("Forgot Password?");
        }
        
        // If the User Clicked the Login Button
        if (loginSubmitted)
        {
            // Invalidate HttpSession (Clear any HttpSession Attributes set)
            session.invalidate();
            
            // Create a User object from the Login form information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            
            // Check if the User has left any Login Fields empty
            if (user.getUserID().equals("") || user.getPassword().equals(""))
            {
                // Get the current HttpSession
                session = request.getSession();
                
                // Set an HttpSession Attribute loginFieldEmpty to True (For prompting the User to fill out the entire form)
                session.setAttribute("loginFieldEmpty", true);
                
                // Redirect the User back to /Login to try Logging In again
                response.sendRedirect("/Login");
                
                // Return (Don't allow them to Login unless all fields were entered - bare minimum)
                return;
            }
            // User entered information into ALL Login fields, proceed with Login
            else
            {  
                try
                {                
                    // Establish Database Connection - Creates Database & Table If not already Created
                    Connection connection = DatabaseConnection.initializeDatabase();
                    
                    // Create Template Statement for Querying the Database for the User's information based on userID (To authenticate User)
                    PreparedStatement sqlCheckUserPassword = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the Query Statement
                    sqlCheckUserPassword.setString(1, user.getUserID());
                    
                    // Execute the SQL Query and get the results
                    ResultSet results = sqlCheckUserPassword.executeQuery();
                    
                    // If the Query returned results, try to authenticate the password for the User (a Registered User exists - proceed with Login)
                    if (results.next())
                    {
                        // Set authenticated to True if the password entered matches the password in the Database
                        authenticated = user.getPassword().equals(results.getString("password"));
                        
                        // Close sqlCheckUserPassword connection
                        sqlCheckUserPassword.close();
                    }
                   
                    // If the User has been authenticated to Login
                    if (authenticated)
                    {
                        // Create a new HttpSession for the Logged In User
                        session = request.getSession(true);
                        
                        // Set account HttpSession Attribute to the Users unique userID and loggedin Attribute to True
                        session.setAttribute("account", request.getParameter("userID"));
                        session.setAttribute("loggedin", true);
                        
                        // Redirect the User to /Home (User Logged In)
                        response.sendRedirect("/Home");        
                    }
                    // User has NOT been authenticated to Login
                    else
                    {
                        // Get the current HttpSession
                        session = request.getSession();
                        
                        // Set an HttpSession Attribute for incorrect userID or password (For prompting User to retry Logging In with correct info)
                        session.setAttribute("incorrectIdOrPassword", true);
                        
                        // Redirect the User back to /Login to try Logging In again
                        response.sendRedirect("/Login");
                
                        // Return (Don't allow them to Login unless they have been authenticated)
                        return;
                    }

                    // Close Database Connection                    
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
        // If the user didn't click the Login Button, check if they clicked the Forgot Password? Button
        else if (forgotPassword)
        {
            // Redirect the User to the Forgot Password Page
            response.sendRedirect("/ForgotPassword");
        }
    }
}
