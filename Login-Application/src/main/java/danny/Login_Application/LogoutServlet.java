package danny.Login_Application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet
{
    private static final long serialVersionUID = 4L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get the current HttpSession
        HttpSession session = request.getSession();

        // Invalidate the current HttpSession (Log the User out)
        session.invalidate();

        // Create new HttpSession
        session = request.getSession(true);

        // Redirect the Logged Out User to /Home (Send to Homepage after Log out)
        response.sendRedirect("/Login_Application/Home");
    }
}
