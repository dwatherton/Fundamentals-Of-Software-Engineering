import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet
{
    static final long serialVersionUID = 4L;
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get current HttpSession
        HttpSession session = request.getSession();
        
        // Invalidate current session
        session.invalidate();
        
        // Create new session
        session = request.getSession(true);
        
        // Send user to /Home
        response.sendRedirect("/Home"); 
    }
}