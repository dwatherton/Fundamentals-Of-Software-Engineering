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

@WebServlet(name = "ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet
{
    static final long serialVersionUID = 5L;
    
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/forgotpassword.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current session
        HttpSession session = request.getSession();

        // Set forgotPasswordFieldEmpty for making sure user entered ALL fields
        Boolean forgotPasswordFieldEmpty = (Boolean)session.getAttribute("forgotPasswordFieldEmpty");
        request.setAttribute("forgotPasswordFieldEmpty", forgotPasswordFieldEmpty);
        
        // Set incorrectRecoveryInfo for making sure user entered the correct user info
        Boolean incorrectRecoveryInfo = (Boolean)session.getAttribute("incorrectRecoveryInfo");
        request.setAttribute("incorrectRecoveryInfo", incorrectRecoveryInfo);
        
        // Set passwordsDoNotMatch for making sure the user entered matching passwords for reset
        Boolean passwordsDoNotMatch = (Boolean)session.getAttribute("passwordsDoNotMatch");
        request.setAttribute("passwordsDoNotMatch", passwordsDoNotMatch);
        
        // Invalidate session if user failed to login then came to reset password (Clears the variables set for ERROR messages on Login form)
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
        // Create an HttpSession for Forgot Password
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        boolean resetPasswordSubmitted = request.getParameter("resetPassword").equals("Reset Password");
        boolean userInformationMatches = false;
        
        // Check the user clicked Reset Password
        if (resetPasswordSubmitted)
        {
            session.invalidate();
            
            // Create a user from the Forgot Password information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setSecurityQuestion(request.getParameter("securityQuestion"));
            user.setSecurityAnswer(request.getParameter("securityAnswer"));
            
            // Check that the user hasn't left any fields empty (REDIRECT TO FORGOT PASSWORD AND PROMPT USER TO ENTER ENTER ALL FIELDS)
            if (user.getUserID().equals("") || user.getSecurityQuestion().equals("") || user.getSecurityAnswer().equals("")
                || request.getParameter("newPassword").equals("") || request.getParameter("confirmPassword").equals(""))
            {
                // Get the current session
                session = request.getSession();
                
                // Set an attribute for empty forgot password fields (For prompting user to fill out entire form)
                session.setAttribute("forgotPasswordFieldEmpty", true);
                
                response.sendRedirect("/ForgotPassword");
                
                // Return (Don't allow them to reset password unless all fields were entered and passwords match)
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
                    PreparedStatement sqlCheckUserInfo = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the select statement
                    sqlCheckUserInfo.setString(1, user.getUserID());
                    
                    // Execute the sql query and get the results
                    ResultSet results = sqlCheckUserInfo.executeQuery();
                    
                    // If the query returned results, get the info for the user
                    if (results.next())
                    {
                        // Set userInformationMatches if the user entered info matches info in the DB
                        userInformationMatches = checkUserInfo(user, results);
                        
                        // Close sqlCheckUserPassword connection
                        sqlCheckUserInfo.close();
                    }
                   
                    // Check if the user information matches the information in the DB
                    if (userInformationMatches)
                    {
                        // Check if the newPassword and confirmPassword entered are the same
                        if (request.getParameter("newPassword").equals(request.getParameter("confirmPassword")))
                        {
                            // Create Template Statement for updating the users password in the DataBase
                            PreparedStatement sqlUpdateUserPassword = connection.prepareStatement("update `users` set password = ? where userID = ?");
                            
                            // Add the password field to the update statement
                            sqlUpdateUserPassword.setString(1, request.getParameter("newPassword"));
                            
                            // Add the userID field to the update statement
                            sqlUpdateUserPassword.setString(2, user.getUserID());
                            
                            // Execute the sql query to update the users password
                            sqlUpdateUserPassword.executeUpdate();
                            
                            // Close sqlUpdateUserPassword connection
                            sqlUpdateUserPassword.close();
                            
                            // Send the user to login now that password has been reset
                            response.sendRedirect("/Login");
                        }
                        // The two entered passwords don't match
                        else
                        {
                            // Create a new session for
                            session = request.getSession(true);
                            
                            // Set an attribute for passwordsDoNotMatch (For prompting user to make sure passwords match)
                            session.setAttribute("passwordsDoNotMatch", true);
                            
                            response.sendRedirect("/ForgotPassword");
                        }
                    }
                    else
                    {
                        // Get the current session
                        session = request.getSession();
                        
                        // Set an attribute for incorrectRecoveryInfo (For prompting user to retry resetting password with the correct info)
                        session.setAttribute("incorrectRecoveryInfo", true);
                        
                        response.sendRedirect("/ForgotPassword");
                
                        // Return (Don't allow them to reset password unless entered user info matches DB)
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
    
    private Boolean checkUserInfo(User user, ResultSet results) throws SQLException
    {
        // Get the user information stored in the database
        String userID = results.getString("userId");
        String securityQuestion = results.getString("securityQuestion");
        String securityAnswer = results.getString("securityAnswer");
        
        // Get the information the user entered
        String id = user.getUserID();
        String question = user.getSecurityQuestion();
        String answer = user.getSecurityAnswer();
        
        return userID.equals(id) && securityQuestion.equals(question) && securityAnswer.equals(answer);
    }
}
