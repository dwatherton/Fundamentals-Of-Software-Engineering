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
    static final long serialVersionUID = 1L;
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/home.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String account = (String)session.getAttribute("account");
        Boolean registered = (Boolean)session.getAttribute("registered");
        Boolean loggedin = (Boolean)session.getAttribute("loggedin");
        request.setAttribute("account", account);
        request.setAttribute("registered", registered);
        request.setAttribute("loggedin", loggedin);

        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }
}
