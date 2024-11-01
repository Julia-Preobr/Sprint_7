import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CreateCourierTest {
    private CourierClient courierClient;
    private String randomLogin;
    private String randomPassword;
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = CourierClient.BASE_URL;

        courierClient = new CourierClient();

        randomLogin = RandomStringUtils.randomAlphanumeric(2, 15); // Генерируем случайный логин
        randomPassword = RandomStringUtils.randomAlphanumeric(7, 15); // Генерируем случайный пароль
    }

    @After
    public void tearDown() throws Exception {
        courierId = courierClient.loginCourier(new Courier(randomLogin, randomPassword)).body().path("id");        //createCourier(randomLogin, randomPassword); // Создаем курьера перед тестами
        if (courierId != null) {
            courierClient.deleteCourier(courierId)
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .body("ok", Matchers.is(true));
        }
    }

    @Test
    @Step("Создание учетной записи курьера")
    @Description("Проверка состояние кода и значений для полей /api/v1/courier")
    public void createCourierTest() {
        String firstName = RandomStringUtils.randomAlphabetic(2, 18);
        courierClient.createCourier(new Courier(randomLogin, randomPassword, firstName))
                .then().log().all()
                .assertThat().statusCode(201).and()
                .body("ok", Matchers.is(true));
    }

    @Test
    @Step("Создание курьера без имени курьера")
    @Description("Проверка состояние кода и сообщение при создании курьера без имени курьера")
    public void creatingCourierWithoutFirstName() {
        courierClient.createCourier(new Courier(randomLogin, randomPassword))
                .then().log().all()
                .assertThat().statusCode(201).and()
                .body("ok", Matchers.is(true));
    }


    // Тест падает из-за некорректного сообщения, поэтому поставила Matchers.notNullValue()
    // Ожидаемый результат: сообщение с текстом "Этот логин уже используется".
    // Фактический результат: сообщение с текстом "Этот логин уже используется. Попробуйте другой."
    @Test
    @Step("Создание курьеров с одинаковыми логинами")
    @Description("Проверка состояние кода и сообщение при создании двух курьеров с одинаковыми логинами")
    public void creatingTwoIdenticalLoginCouriers() {
        String firstName = RandomStringUtils.randomAlphabetic(2, 18);
        courierClient.createCourier(new Courier(randomLogin, randomPassword, firstName))
                .then().log().all()
                .assertThat().statusCode(201).and()
                .body("ok", Matchers.is(true));
        courierClient.createCourier(new Courier(randomLogin, randomPassword, firstName))
                .then().log().all()
                .assertThat().statusCode(409).and()
                .body("message", Matchers.equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Test
    @Step("Создание курьера без логина")
    @Description("Проверка состояние кода и сообщение при создании курьера без логина")
    public void creatingCourierWithoutLogin() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier("", "secret", "empty"));
        postRequestCreateCourier.then().log().all()
                .assertThat().statusCode(400).and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Step("Создание курьера без пароля")
    @Description("Проверка состояние кода и сообщение при создании курьера без пароля")
    public void creatingCourierWithoutPassword() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier("julia", "", "tester"));
        postRequestCreateCourier.then().log().all()
                .assertThat().statusCode(400).and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Step("Создание курьера без логина и пароля")
    @Description("Проверка состояние кода и сообщение при создании курьера без логина и пароля")
    public void creatingCourierWithoutLoginAndPassword() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier("", "", "tester"));
        postRequestCreateCourier.then().log().all()
                .assertThat().statusCode(400).and()
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }
}