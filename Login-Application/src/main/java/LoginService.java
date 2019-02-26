import java.util.Optional;

public class LoginService
{
    public Optional<User> getUser(int userID)
    {
        switch (userID)
        {
            case 1:
                return Optional.of(new User("A", "ZZZ", "ZZZ", "ZZZ"));
            case 2:
                return Optional.of(new User("B", "AAA", "AAA", "AAA"));
            default:
                return Optional.empty();
        }
    }    
}
