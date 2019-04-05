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
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/register.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current session
        HttpSession session = request.getSession();
        
        // Set registrationFieldEmpty for making sure user entered ALL fields
        Boolean registrationFieldEmpty = (Boolean)session.getAttribute("registrationFieldEmpty");
        request.setAttribute("registrationFieldEmpty", registrationFieldEmpty);
        
        // Set userIdTaken for making sure the userID entered is not already taken
        Boolean userIdTaken = (Boolean)session.getAttribute("userIdTaken");
        request.setAttribute("userIdTaken", userIdTaken);
        
        // Invalidate session if user failed to login then came to register (Clears the variables set for ERROR messages on Login form )
        if (session.getAttribute("loginFieldEmpty") != null || session.getAttribute("incorrectIdOrPassword") != null)
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
        PrintWriter out = response.getWriter();
        boolean registrationSubmitted = request.getParameter("register").equals("Register");
        // Create an HttpSession for Registration
        HttpSession session = request.getSession();
        
        // Check user clicked Register
        if (registrationSubmitted)
        {
            session.invalidate();
            
            // Create a user from the registration information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            user.setName(request.getParameter("name"));
            user.setSecurityQuestion(request.getParameter("securityQuestion"));
            user.setSecurityAnswer(request.getParameter("securityAnswer"));
            
            // Check that the user hasn't left any fields empty (REDIRECT TO REGISTER AND PROMPT USER TO ENTER ENTER ALL FIELDS)
            if (user.getUserID().equals("") || user.getPassword().equals("")
                || user.getName().equals("") || user.getSecurityQuestion().equals("")
                || user.getSecurityAnswer().equals(""))
            {
                // Get the current session
                session = request.getSession();
                
                // Set an attribute for empty registration fields (For prompting user to fill out entire form)
                session.setAttribute("registrationFieldEmpty", true);
                
                response.sendRedirect("/Register");
                
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
                    
                    // Create Template for Statement with 1 entry for userID
                    PreparedStatement sqlCheckUniqueUser = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the select statement
                    sqlCheckUniqueUser.setString(1, user.getUserID());
                    
                    // Execute the sql query and get the results
                    ResultSet results = sqlCheckUniqueUser.executeQuery();
                    
                    // If no results were found, the userID has not been taken (USERID IS UNIQUE - PROCEED)
                    if (!results.next()) 
                    {
                        // Close sqlCheckUniqeUser connection
                        sqlCheckUniqueUser.close();
                    
                        // Create Template for Statement with 5 Columns in user table
                        PreparedStatement sqlAddUser = connection.prepareStatement("insert into `users` values(?, ?, ?, ?, ?)");
                        
                        // Add the five fields inserted to the registration into the insert statement
                        sqlAddUser.setString(1, user.getUserID());
                        sqlAddUser.setString(2, user.getPassword());
                        sqlAddUser.setString(3, user.getName());
                        sqlAddUser.setString(4, user.getSecurityQuestion());
                        sqlAddUser.setString(5, user.getSecurityAnswer());
                        
                        // Execute the sqlAddUser statement
                        sqlAddUser.executeUpdate();
                        
                        // Close sqlAddUser connection
                        sqlAddUser.close();
                        
                        // Get the current session
                        session = request.getSession();
                        
                        // Set account Attribute to the users unique userID and registered/loggedin Attribute to true
                        session.setAttribute("account", request.getParameter("userID"));
                        session.setAttribute("registered", true);
                        session.setAttribute("loggedin", true);
                        session.setAttribute("userID", user.getUserID());
                        session.setAttribute("password", user.getPassword());
                        session.setAttribute("name", user.getName());
                        session.setAttribute("securityQuestion", user.getSecurityQuestion());
                        session.setAttribute("securityAnswer", user.getSecurityAnswer());
                        
                        // Redirect user to /Home
                        response.sendRedirect("/Home");
                    }
                    // The UserID entered is already taken (REDIRECT TO REGISTER AND PROMPT USER TO ENTER A DIFFERENT USERID)
                    else
                    {
                        // Create a new session for the registered user
                        session = request.getSession(true);
                        
                        // Set an attribute for user ID taken (For prompting user to enter a new userID)
                        session.setAttribute("userIdTaken", true);
                        
                        response.sendRedirect("/Register");
                        
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
    }
}
