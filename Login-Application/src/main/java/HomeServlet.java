import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HomeServlet") 
public class HomeServlet extends HttpServlet
{
    static final long serialVersionUID = 1L;
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/home.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }
}
