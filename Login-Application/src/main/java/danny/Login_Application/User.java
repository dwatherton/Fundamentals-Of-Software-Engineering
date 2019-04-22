package danny.Login_Application;

import lombok.Data;

@Data
public class User
{
    private String userID;
    private String password;
    private String name;
    private String securityQuestion;
    private String securityAnswer;
}
