import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAPI {

    final static String ORDERS = "/api/orders";

    @Step("Make get-request to get user's orders")
    public static Response getListOfOrdersOfUser(String token) {

        Response response = given()
                .auth().oauth2(token)
                .get(ORDERS);
        return response;
    }

}
