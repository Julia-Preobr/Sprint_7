
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_COURIER_ENDPOINT = "api/v1/courier";
    public static final String DELETE_COURIER_ENDPOINT = "api/v1/courier/{id}";
    public static final String LOGIN_COURIER_ENDPOINT = "api/v1/courier/login";
    public static final String ORDERS_ENDPOINT = "api/v1/orders";
    public static final String CANCEL_ORDER_ENDPOINT = "api/v1/orders/cancel";

    @Step("Создание курьера, проверка кода ответа")
    public Response createCourier(Courier courier) {
        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(BASE_URL + CREATE_COURIER_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Авторизация курьера в системе, получение id, проверка кода ответа")
    public Response loginCourier(Courier courier) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(BASE_URL + LOGIN_COURIER_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Удаление курьера, проверка кода ответа")
    public Response deleteCourier(Integer id) {
        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .pathParam("id", id)
                .when()
                .delete(BASE_URL + DELETE_COURIER_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
