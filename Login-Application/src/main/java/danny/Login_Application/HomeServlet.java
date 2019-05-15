package danny.Login_Application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "HomeServlet")
public class HomeServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private static final String JSP_VIEW_PATH = "/WEB-INF/jsp/home.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();

        // Set account for the User (userID if User is Logged In, null if User is not - Changes Home page content)
        String account = (String) session.getAttribute("account");
        request.setAttribute("account", account);

        // Set registered for the User (True if User is Registered, null if User is not - Changes Home page content)
        Boolean registered = (Boolean) session.getAttribute("registered");
        request.setAttribute("registered", registered);

        // Set loggedin for the User (True if User is Logged In. null if User is not - Changes Home page content)
        Boolean loggedin = (Boolean) session.getAttribute("loggedin");
        request.setAttribute("loggedin", loggedin);

        // Invalidate HttpSession if NOT Logged In (Clear any HttpSession Atributes set)
        if (session.getAttribute("loggedin") == null)
        {
            session.invalidate();
        }

        // Forward HttpRequests and HttpResponses to home.jsp (Tie this Servlet to it's respective JSP View)
        request.getRequestDispatcher(JSP_VIEW_PATH).forward(request, response);
    }
}
