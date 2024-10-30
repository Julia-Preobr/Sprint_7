import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetOrdersTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @Test
    @Step("Создать заказ с цветами")
    public void createOrderWithColors() {
        String[] colors = {"BLACK", "GREY"};

        // Создание экземпляра Orders с учетом новых параметров
        Orders order = new Orders(
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
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("track", notNullValue()) // Проверяем, что трек не null
                .extract()
                .response();

        System.out.println("Созданный заказ: " + response.body().asString());
    }

    @Test
    @Step("Попытка создать заказ с ошибками")
    public void createOrderWithErrors() {
        // Пример создания заказа без обязательных полей
        given()
                .contentType("application/json")
                .body("{}") // Пустое тело запроса
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400) // Ожидаем ошибку
                .body("message", equalTo("Недостаточно данных для создания заказа"));

        // Попытка создания заказа без необходимых полей
        String[] colors = {"BLACK", "GREY"};
        Orders order = new Orders(
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
                .post(BASE_URL)
                .then()
                .statusCode(400) // Ожидаем ошибку
                .body("message", equalTo("Недостаточно данных для создания заказа"));
    }
}
