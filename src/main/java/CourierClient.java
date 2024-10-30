
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private static final String CREATE_COURIER_ENDPOINT = "api/v1/courier";
    private static final String LOGIN_COURIER_ENDPOINT = "api/v1/courier/login";

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
}
