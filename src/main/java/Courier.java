
public class Courier {
    private String login;
    private String password;
    private String firstName;

    // Конструктор с полями логина, пароля и имени
    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // Конструктор с логином и паролем
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Переопределение метода toString для удобства отображения
    @Override
    public String toString() {
        return "Courier{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
