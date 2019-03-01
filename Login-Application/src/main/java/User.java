import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class User
{
    @Getter
    @Setter
    private String userID;
    
    @Getter
    @Setter
    private String password;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String securityQuestion;
    
    @Getter
    @Setter
    private String securityAnswer;
}
