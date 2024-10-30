import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/courier/login";
    private String randomLogin;
    private String password = "12345"; // Пароль должен совпадать с тем, что использовался при создании курьера

    // Генерация случайного логина
    private String generateRandomLogin(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder login = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            login.append(characters.charAt(index));
        }

        return login.toString();
    }

    @Before
    public void setUp() {
        randomLogin = generateRandomLogin(10); // Генерируем случайный логин
        createCourier(randomLogin, password); // Создаем курьера перед тестами
    }

    @After
    public void tearDown() {
        deleteCourier(randomLogin); // Удаляем курьера после тестов
    }

    private void createCourier(String login, String password) {
        given()
                .contentType("application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

    }

    private void deleteCourier(String login) {
        given()
                .contentType("application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
                .when()
                .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier")
                .then()
                .statusCode(200);
    }

    @Test
    @Step("Авторизовать курьера")
    public void loginCourierSuccessfully() {
        given()
                .contentType("application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", randomLogin, password))
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Step("Не авторизовать курьера с неверными данными")
    public void loginCourierWithInvalidCredentials() {
        given()
                .contentType("application/json")
                .body("{\"login\": \"wrong_login\", \"password\": \"wrong_password\"}")
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Авторизация без обязательных полей")
    public void loginCourierWithoutRequiredFields() {
        // Пробуем авторизоваться без логина
        given()
                .contentType("application/json")
                .body("{\"password\": \"" + password + "\"}") // Пропускаем логин
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(404)
                .body("message", equalTo("Недостаточно данных для входа"));

        // Пробуем авторизоваться без пароля
        given()
                .contentType("application/json")
                .body(String.format("{\"login\": \"%s\"}", randomLogin)) // Пропускаем пароль
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
