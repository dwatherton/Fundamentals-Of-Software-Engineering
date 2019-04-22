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

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet
{
    private static final long serialVersionUID = 2L;

    public static final String JSP_VIEW_PATH = "/WEB-INF/jsp/register.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();
        
        // Set registrationFieldEmpty for making sure User entered ALL fields in Registration Form (Changes Registration page content)
        Boolean registrationFieldEmpty = (Boolean)session.getAttribute("registrationFieldEmpty");
        request.setAttribute("registrationFieldEmpty", registrationFieldEmpty);
        
        // Set userIdTaken for making sure the userID entered is not already taken (Changes Registration page content)
        Boolean userIdTaken = (Boolean)session.getAttribute("userIdTaken");
        request.setAttribute("userIdTaken", userIdTaken);
        
        // Invalidate HttpSession if User failed to login or reset password then came to register (Clear any HttpSession Attributes set)
        if (session.getAttribute("loginFieldEmpty") != null || session.getAttribute("incorrectIdOrPassword") != null
            || session.getAttribute("forgotPasswordFieldEmpty") != null || session.getAttribute("incorrectRecoveryInfo") != null
            || session.getAttribute("passwordsDoNotMatch") != null)
        {
            session.invalidate();
        }
        
        // Forward HttpRequests and HttpResponses to register.jsp (Tie this Servlet to it's respective JSP View)
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
        
        // Create registrationSubmitted variable for checking if the User clicked the Register Button
        boolean registrationSubmitted = request.getParameter("register").equals("Register");
        
        // If the User Clicked the Register Button
        if (registrationSubmitted)
        {
            // Invalidate HttpSession (Clear any HttpSession Attributes set)
            session.invalidate();
            
            // Create a User object from the Registration form information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            user.setName(request.getParameter("name"));
            user.setSecurityQuestion(request.getParameter("securityQuestion"));
            user.setSecurityAnswer(request.getParameter("securityAnswer"));
            
            // Check if the User has left any Registration Fields empty
            if (user.getUserID().equals("") || user.getPassword().equals("")
                || user.getName().equals("") || user.getSecurityQuestion().equals("")
                || user.getSecurityAnswer().equals(""))
            {
                // Get the current HttpSession
                session = request.getSession();
                
                // Set an HttpSession Attribute registrationFieldEmpty to True (For prompting the User to fill out the entire form)
                session.setAttribute("registrationFieldEmpty", true);
                
                // Redirect the User back to /Register to try Registering again
                response.sendRedirect("/Register");
                
                // Return (Don't allow them to Register unless all fields were entered - bare minimum)
                return;
            }
            // User entered information into ALL Registration fields, proceed with Registration
            else
            
            {
                try
                {                
                    // Establish Database Connection - Creates Database & Table If not already Created
                    Connection connection = DatabaseConnection.initializeDatabase();
                    
                    // Create Template Statement for Querying the Database for the userID entered (To check if User entered userID is unique)
                    PreparedStatement sqlCheckUniqueUser = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the Query Statement
                    sqlCheckUniqueUser.setString(1, user.getUserID());
                    
                    // Execute the SQL Query and get the results
                    ResultSet results = sqlCheckUniqueUser.executeQuery();
                    
                    // If the Query returned no results, the userID has not been taken (userID is unique - proceed with Registration)
                    if (!results.next()) 
                    {
                        // Close sqlCheckUniqeUser connection
                        sqlCheckUniqueUser.close();
                    
                        // Create Template Statement for inserting a new User into the Database
                        PreparedStatement sqlAddUser = connection.prepareStatement("insert into `users` values(?, ?, ?, ?, ?)");
                        
                        // Add the five fields inserted into the Registration form to the SQL insert statement
                        sqlAddUser.setString(1, user.getUserID());
                        sqlAddUser.setString(2, user.getPassword());
                        sqlAddUser.setString(3, user.getName());
                        sqlAddUser.setString(4, user.getSecurityQuestion());
                        sqlAddUser.setString(5, user.getSecurityAnswer());
                        
                        // Execute the sqlAddUser statement
                        sqlAddUser.executeUpdate();
                        
                        // Close sqlAddUser connection
                        sqlAddUser.close();
                        
                        // Create a new HttpSession for the Registered User
                        session = request.getSession(true);
                        
                        // Set HttpSession Attributes to the User's information and registered/loggedin Attributes to true
                        session.setAttribute("account", request.getParameter("userID"));
                        session.setAttribute("registered", true);
                        session.setAttribute("loggedin", true);
                        session.setAttribute("userID", user.getUserID());
                        session.setAttribute("password", user.getPassword());
                        session.setAttribute("name", user.getName());
                        session.setAttribute("securityQuestion", user.getSecurityQuestion());
                        session.setAttribute("securityAnswer", user.getSecurityAnswer());
                        
                        // Redirect the User to /Home (New User Registered)
                        response.sendRedirect("/Home");
                    }
                    // The UserID entered is already taken
                    else
                    {
                        // Get the Current HttpSession
                        session = request.getSession();
                        
                        // Set an HttpSession Attribute userID taken (For prompting User to enter a new userID and try Registering again)
                        session.setAttribute("userIdTaken", true);
                        
                        // Redirect the User back to /Register to try Registering again 
                        response.sendRedirect("/Register");
                        
                        // Return (Don't allow them to Register unless userID was unique)
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
    }
}
