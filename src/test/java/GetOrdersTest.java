import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetOrdersTest {
    @Test
    @Step("Создать заказ с цветами")
    public void createOrderWithColors() {
        String[] colors = {"BLACK", "GREY"};

        // Создание экземпляра Orders с учетом новых параметров
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4, // ID станции метро
                "+7 800 355 35 35",
                5,
                "2024-11-06",
                "Saske, come back to Konoha",
                colors
        );

        // Выполнение POST запроса и проверка ответа
        Response response = given()
                .contentType("application/json")
                .body(order) // Используем объект Orders
                .when()
                .post(CourierClient.BASE_URL + CourierClient.ORDERS_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue()) // Проверяем, что трек не null
                .extract()
                .response();

        System.out.println("Созданный заказ: " + response.body().asString());

        Integer track = response.body().path("id");

        // Отменить заказ
        given()
                .contentType("application/json")
                .body("{\"track\": " + track + "}") // Используем объект Orders
                .when()
                .post(CourierClient.BASE_URL + CourierClient.CANCEL_ORDER_ENDPOINT)
                .then()
                .statusCode(200)
                .body("ok", Matchers.is(true)) // Проверяем, что трек не null
                .extract()
                .response();
    }

    @Test
    @Step("Попытка создать заказ с ошибками")
    public void createOrderWithEmptyBody() {
        // Пример создания заказа без обязательных полей
        given()
                .contentType("application/json")
                .body("{}") // Пустое тело запроса
                .when()
                .post(CourierClient.BASE_URL + CourierClient.ORDERS_ENDPOINT)
                .then()
                .statusCode(400) // Ожидаем ошибку
                .body("message", equalTo("Недостаточно данных для создания заказа"));
    }

    @Test
    @Step("Попытка создать заказ с ошибками")
    public void createOrderWithoutFirstName() {
        // Попытка создания заказа без необходимых полей
        String[] colors = {"BLACK", "GREY"};
        Order order = new Order(
                null, // Пропускаем имя
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2024-11-06",
                "Saske, come back to Konoha",
                colors
        );

        given()
                .contentType("application/json")
                .body(order)
                .when()
                .post(CourierClient.BASE_URL + CourierClient.ORDERS_ENDPOINT)
                .then()
                .statusCode(400) // Ожидаем ошибку
                .body("message", equalTo("Недостаточно данных для создания заказа"));
    }
}
