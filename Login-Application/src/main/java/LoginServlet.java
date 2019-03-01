import java.io.IOException;

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
    
    private static LoginService loginService = new LoginService();
 
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
        // Create a user from the login information
        User user = new User();
        user.setUserID(request.getParameter("userID"));
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name"));
        user.setSecurityQuestion(request.getParameter("securityQuestion"));
        user.setSecurityAnswer(request.getParameter("securityAnswer"));
        
        // Verify User Credentials HERE!!!
        /*
        boolean authenticated = loginService.checkUserIdAndPassword(user.getUserID(), user);
        */
       
        // Create an HttpSession
        HttpSession session = request.getSession();
        
        // Set account Attribute to the users unique userID and registered/loggedin Attribute to true
        session.setAttribute("account", request.getParameter("userID"));
        session.setAttribute("loggedin", true);
       
        // Send User To /Home After Identity Verified!!!
        if (request.getParameter("login").equals("Login"))
        {
            response.sendRedirect("/Home");        
        }
    }
}
