import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet
{
    static final long serialVersionUID = 2L;
    
    private LoginService loginService = new LoginService();
    
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/register.jsp";
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(request, response);
    }
}
