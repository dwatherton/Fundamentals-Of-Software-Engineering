import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class LoginService
{
    // NOT SURE ABOUT ANY OF THIS TBH
    /*
    @PersistenceContext
    private EntityManager em;
    
    public List getAllUsers()
    {
        try
        {
            return em.createQuery("use login_application; select * from users order by userID").getResultList();
        } 
        catch(Exception ex)
        {
            System.out.println("Not Found!");
        }
        
        return null;
    }
    
    public User getUser(String userID)
    {
        User user = em.find(User.class, userID);
        
        if (user == null) 
        {
            System.out.println("Not Found!");
        }
        return user;
    }
    
    public boolean checkUserIdAndPassword(String userID, User user)
    {
        User user1 = em.find(User.class, userID);
        
        if (user == null) 
        {
            System.out.println("Not Found!");
        }
        else
        {
            return user.getPassword() == user1.getPassword();
        }
        return false;
    }
    */
}
