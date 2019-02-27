import java.io.IOException;
import java.io.PrintWriter;
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Temporary method filler - Proof Of Concept - REMOVE IN FINAL PRODUCT!!!
        PrintWriter out = response.getWriter();
        String parameterValue = request.getParameter("userID");
        out.write("userID: " + parameterValue + "\n");
        parameterValue = request.getParameter("password");
        out.write("password: " + parameterValue + "\n");
        parameterValue = request.getParameter("name");
        out.write("name: " + parameterValue + "\n");
        parameterValue = request.getParameter("securityQuestion");
        out.write("securityQuestion: " + parameterValue + "\n");
        parameterValue = request.getParameter("submit");
        out.write("submit: " + parameterValue + "\n");
        parameterValue = request.getRequestURI();
        out.write("request URI: " + parameterValue + "\n");
    }
}
