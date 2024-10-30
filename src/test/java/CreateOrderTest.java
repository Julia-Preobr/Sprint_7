import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;



@RunWith(Parameterized.class)
public class CreateOrderTest {
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
        // Выполняем POST запрос и проверяем ответ
        given()
                .baseUri(CourierClient.BASE_URL + CourierClient.ORDERS_ENDPOINT)
                .contentType(ContentType.JSON)
                .body(new Order(
                        "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
                        "2024-11-06", "Saske, come back to Konoha", colors
                ))
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }}
