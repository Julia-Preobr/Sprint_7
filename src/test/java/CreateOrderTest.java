import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;



@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";
    private final String[] colors;

    public CreateOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] provideColors() {
        return new Object[][] {
                { new String[] {"BLACK"} },
                { new String[] {"GREY"} },
                { new String[] {"BLACK", "GREY"} },
                { new String[] {} }
        };
    }

    @Test
    @Step("Создать заказ с цветами {0}")
    public void createOrderWithColors() {
        // Формируем JSON для тела запроса
        String colorJson = colors.length > 0 ? Arrays.toString(colors).replaceAll("[\\[\\]]", "") : "null";
        String requestBody = String.format("{\"firstName\": \"Naruto\", \"lastName\": \"Uchiha\", \"address\": \"Konoha, 142 apt.\", " +
                "\"metroStation\": 4, \"phone\": \"+7 800 355 35 35\", \"rentTime\": 5, " +
                "\"deliveryDate\": \"2024-11-06\", \"comment\": \"Saske, come back to Konoha\", " +
                "\"color\": [%s]}", colorJson);

        // Выполняем POST запрос и проверяем ответ
        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }}
