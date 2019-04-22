package danny.Login_Application;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable
{
    static final long serialVersionUID = 5L;
    
    @Getter
    @Setter
    @Id
    @Column(name = "userID")
    @NotEmpty
    private String userID;
    
    @Getter
    @Setter
    @Column(name = "password")
    @NotEmpty
    private String password;
    
    @Getter
    @Setter
    @Column(name = "name")
    @NotEmpty
    private String name;
    
    @Getter
    @Setter
    @Column(name = "securityQuestion")
    @NotEmpty
    private String securityQuestion;
    
    @Getter
    @Setter
    @Column(name = "securityAnswer")
    @NotEmpty
    private String securityAnswer;
}
