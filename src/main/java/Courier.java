import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Courier {
    private String login;
    private String password;
    private String firstName;

    // Конструктор с логином и паролем
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
