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

@WebServlet(name = "ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet
{
    private static final long serialVersionUID = 5L;

    public static final String JSP_VIEW_PATH = "/WEB-INF/jsp/forgotpassword.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();

        // Set forgotPasswordFieldEmpty for making sure User entered ALL fields in ForgotPassword Form (Changes ForgotPassword page content)
        Boolean forgotPasswordFieldEmpty = (Boolean)session.getAttribute("forgotPasswordFieldEmpty");
        request.setAttribute("forgotPasswordFieldEmpty", forgotPasswordFieldEmpty);
        
        // Set incorrectRecoveryInfo for making sure User entered the correct User info (Changes ForgotPassword page content)
        Boolean incorrectRecoveryInfo = (Boolean)session.getAttribute("incorrectRecoveryInfo");
        request.setAttribute("incorrectRecoveryInfo", incorrectRecoveryInfo);
        
        // Set passwordsDoNotMatch for making sure the User entered matching passwords for reset (Changes ForgotPassword page content)
        Boolean passwordsDoNotMatch = (Boolean)session.getAttribute("passwordsDoNotMatch");
        request.setAttribute("passwordsDoNotMatch", passwordsDoNotMatch);
        
        // Invalidate HttpSession if User failed to login or register then came to reset password (Clear any HttpSession Attributes set)
        if (session.getAttribute("loginFieldEmpty") != null || session.getAttribute("incorrectIdOrPassword") != null
            || session.getAttribute("registrationFieldEmpty") != null || session.getAttribute("userIdTaken") != null)
        {
            session.invalidate();
        }
        
        // Forward HttpResquests and HttpResponses to forgotpassword.jsp (Tie this Servlet to it's respective JSP View)
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
        
        // Create resetPasswordSubmitted variable for checking if the User clicked the Rest Password Button
        boolean resetPasswordSubmitted = request.getParameter("resetPassword").equals("Reset Password");
        
        // Create userInformationMatches variable for checking if the User entered information matches information in the Database
        boolean userInformationMatches = false;
        
        // If the User clicked the Reset Password Button
        if (resetPasswordSubmitted)
        {
            // Invalidate HttpSession (Clear any HttpSession Attributes set)
            session.invalidate();
            
            // Create a User object from the Forgot Password form information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setSecurityQuestion(request.getParameter("securityQuestion"));
            user.setSecurityAnswer(request.getParameter("securityAnswer"));
            
            // Check if the user has left any Forgot Password fields empty
            if (user.getUserID().equals("") || user.getSecurityQuestion().equals("") || user.getSecurityAnswer().equals("")
                || request.getParameter("newPassword").equals("") || request.getParameter("confirmPassword").equals(""))
            {
                // Get the current HttpSession
                session = request.getSession();
                
                // Set an HttpSession attribute forgotPasswordFieldEmpty to True (For prompting the User to fill out entire form)
                session.setAttribute("forgotPasswordFieldEmpty", true);

                // Redirect the User back to /ForgotPassword to try Resetting their password again               
                response.sendRedirect("/ForgotPassword");
                
                // Return (Don't allow them to Reset Password unless all fields were entered - bare minimum)
                return;
            }
            // User entered information into ALL Forgot Password fields, proceed with Resetting password
            else
            {  
                try
                {                
                    // Establish Database Connection - Creates Database & Table If not already Created
                    Connection connection = DatabaseConnection.initializeDatabase();
                    
                    // Create Template Statement for Querying the Database for the User's information (To check if User entered information matches Database information)
                    PreparedStatement sqlCheckUserInfo = connection.prepareStatement("select * from `users` where userID = ?");
                    
                    // Add the userID field to the Query statement
                    sqlCheckUserInfo.setString(1, user.getUserID());
                    
                    // Execute the SQL Query and get the results
                    ResultSet results = sqlCheckUserInfo.executeQuery();
                    
                    // If the Query returned results, try to authenticate the User's information (a Registered User exists - proceed with password Resetting)
                    if (results.next())
                    {
                        // Set userInformationMatches if the User entered information matches information stored in the Database
                        userInformationMatches = checkUserInfo(user, results);
                        
                        // Close sqlCheckUserInfo connection
                        sqlCheckUserInfo.close();
                    }
                   
                    // If the User entered information matches the information in the Database
                    if (userInformationMatches)
                    {
                        // If the newPassword and confirmPassword fields the User entered are the same (new password fields match - proceed with password Resetting)
                        if (request.getParameter("newPassword").equals(request.getParameter("confirmPassword")))
                        {
                            // Create Template Statement for Updating the User's password in the Database (Reset User's password)
                            PreparedStatement sqlUpdateUserPassword = connection.prepareStatement("update `users` set password = ? where userID = ?");
                            
                            // Add the confirmed new password field to the SQL Update Statement
                            sqlUpdateUserPassword.setString(1, request.getParameter("newPassword"));
                            
                            // Add the User's userID to the SQL Update Statement
                            sqlUpdateUserPassword.setString(2, user.getUserID());
                            
                            // Execute the SQL Update Statement to set the User's password to the confirmed new password the User entered
                            sqlUpdateUserPassword.executeUpdate();
                            
                            // Close sqlUpdateUserPassword connection
                            sqlUpdateUserPassword.close();
                            
                            // Redirect the User to /Login now that the User's password has been reset to try Logging In now
                            response.sendRedirect("/Login");
                        }
                        // The two User entered passwords do NOT match
                        else
                        {
                            // Get the current HttpSession
                            session = request.getSession();
                            
                            // Set an HttpSession Attribute for passwords do not match (For prompting User to make sure to enter matching passwords)
                            session.setAttribute("passwordsDoNotMatch", true);
                            
                            // Redirect the User back to /ForgotPassword to try Resetting their password again
                            response.sendRedirect("/ForgotPassword");
                        }
                    }
                    // User entered information does NOT match information in the Database
                    else
                    {
                        // Get the current HttpSession
                        session = request.getSession();
                        
                        // Set an HttpSession Attribute for incorrect recovery info (For prompting User to retry Resetting password with the correct User info)
                        session.setAttribute("incorrectRecoveryInfo", true);
                        
                        // Redirect the User back to /ForgotPassword to try Resetting their password again
                        response.sendRedirect("/ForgotPassword");
                
                        // Return (Don't allow them to Reset their password unless the User entered information matches the information in the Database)
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
    
    /**
    * Checks if the User's userID, securityQuestion, and securityAnswer matches the corresponding values in the ResultSet results.
    *
    * @param user The User to check if their information matches the information in the ResultSet results.  
    *                Should be the User created with information entered into the Forgot Password form.
    * @param results The ResultSet from an SQL Query of the login_application.users table.
    *                Should be the ResultSet of a Select Statement by userID entered into the Forgot Password form.
    * @return true If the user's userID, securityQuestion, and securityAnswer match the corresponding values in results, otherwise false.
    */
    private Boolean checkUserInfo(User user, ResultSet results) throws SQLException
    {
        // Get the information for the User from the SQL Query of the Database by userID
        String userID = results.getString("userId");
        String securityQuestion = results.getString("securityQuestion");
        String securityAnswer = results.getString("securityAnswer");
        
        // Get the information for the User created with the information entered into the Forgot Password form.
        String id = user.getUserID();
        String question = user.getSecurityQuestion();
        String answer = user.getSecurityAnswer();
        
        // Return True if ALL three fields (userID, securityQuestion, and securityAnswer) match for the User user and the ResultSet results
        return userID.equals(id) && securityQuestion.equals(question) && securityAnswer.equals(answer);
    }
}
