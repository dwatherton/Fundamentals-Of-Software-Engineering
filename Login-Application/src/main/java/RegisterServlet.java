import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        
        // Check user clicked Submit AND NONE OF THE FIELDS ARE LEFT EMPTY/NULL!
        if (registrationSubmitted)
        {
            // Create a user from the registration information
            User user = new User();
            user.setUserID(request.getParameter("userID"));
            user.setPassword(request.getParameter("password"));
            user.setName(request.getParameter("name"));
            user.setSecurityQuestion(request.getParameter("securityQuestion"));
            user.setSecurityAnswer(request.getParameter("securityAnswer"));
                
            try
            {                
                // Establish Database Connection - Creates Database & Table If not already Created
                Connection connection = DatabaseConnection.initializeDatabase();
                
                // Create Template for Statement with 4 Columns in user table
                PreparedStatement sql = connection.prepareStatement("insert into `users` values(?, ?, ?, ?, ?)");
                
                // Add the four fields inserted to the registration into the insert statement
                sql.setString(1, user.getUserID());
                sql.setString(2, user.getPassword());
                sql.setString(3, user.getName());
                sql.setString(4, user.getSecurityQuestion());
                sql.setString(5, user.getSecurityAnswer());
                
                // Execute the sql
                sql.executeUpdate();
                
                // Close connections
                sql.close();
                connection.close();
                
                // Temporary for checking the values successfully inserted into Database
                out.println("<html><body><h1>Successfully Inserted</h1>");
                String parameterValue = request.getParameter("userID");
                out.write("userID: " + parameterValue + "<br/>");
                parameterValue = request.getParameter("password");
                out.write("password: " + parameterValue + "<br/>");
                parameterValue = request.getParameter("name");
                out.write("name: " + parameterValue + "<br/>");
                parameterValue = request.getParameter("securityQuestion");
                out.write("securityQuestion: " + parameterValue + "<br/>");
                parameterValue = request.getParameter("securityAnswer");
                out.write("securityAnswer: " + parameterValue + "<br/></body></html>");
                
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
            
            // Create an HttpSession
            HttpSession session = request.getSession();
            
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
    }
}
