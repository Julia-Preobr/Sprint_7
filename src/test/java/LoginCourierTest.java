import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private static CourierClient courierClient;
    private static String randomLogin;
    private static String randomPassword;
    private static Integer courierId;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = CourierClient.BASE_URL;

        courierClient = new CourierClient();

        randomLogin = RandomStringUtils.randomAlphanumeric(2, 15); // Генерируем случайный логин
        randomPassword = RandomStringUtils.randomAlphanumeric(7, 15); // Генерируем случайный пароль

        courierClient.createCourier(new Courier(randomLogin, randomPassword))
                .then().log().all()
                .assertThat().statusCode(201);

        courierId = courierClient.loginCourier(new Courier(randomLogin, randomPassword)).body().path("id");        //createCourier(randomLogin, randomPassword); // Создаем курьера перед тестами
        Assert.assertNotNull(courierId);
    }

    @AfterClass
    public static void tearDown() {
        courierClient.deleteCourier(courierId)
                .then().log().all()
                .assertThat().statusCode(200)
                .body("ok", Matchers.is(true));
    }

    @Test
    @Step("Авторизовать курьера")
    public void loginCourierSuccessfully() {
        courierClient.loginCourier(new Courier(randomLogin, randomPassword))
                .then().log().all()
                .assertThat().statusCode(200).and()
                .body("id", notNullValue());
    }

    @Test
    @Step("Не авторизовать курьера с неверными данными")
    public void loginCourierWithInvalidCredentials() {
        courierClient.loginCourier(new Courier(RandomStringUtils.randomAlphanumeric(2, 15), "wrong_password"))
                .then().log().all()
                .assertThat().statusCode(404).and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Авторизация без обязательных полей")
    public void loginCourierWithoutLogin() {
        // Пробуем авторизоваться без логина
        courierClient.loginCourier(new Courier("", randomPassword)) // Пропускаем логин
                .then().log().all()
                .assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Авторизация без обязательных полей")
    public void loginCourierWithoutPassword() {
        // Пробуем авторизоваться без пароля
        courierClient.loginCourier(new Courier(randomLogin, "")) // Пропускаем пароль
                .then().log().all()
                .assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
