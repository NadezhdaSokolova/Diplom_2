import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

public class OrderAPI {
    final static String ORDERS = "/api/orders";
    final static String INGREDIENTS = "api/ingredients";


    @Step("Make get-request to get user's orders")
    public static ValidatableResponse getListOfOrdersOfUser(String token) {

        return given()
                .auth().oauth2(token)
                .get(ORDERS)
                .then();
    }

    @Step("Make get-request to know about ingredients")
    public static ValidatableResponse getIngredients() {

        return given()
                .header("Content-type", "application/json")
                .get(INGREDIENTS)
                .then();
    }

    @Step("Make post-request to make burger")
    public static ValidatableResponse makeBurger(SelectedIngredientsPOJO ingredient) {

        return given()
                .header("Content-type", "application/json")
                .body(ingredient)
                .post(ORDERS)
                .then();
    }

    @Step("Get id's ingredients")
    public static ArrayList<String> arrayOfIngredients() {

        ArrayList<HashMap<String, String>> Response = OrderAPI.getIngredients().extract().path("data");
        System.out.println(Response.get(1).get("_id"));
        ArrayList<String> listOfIngredients = new ArrayList<>();
        for (int i = 0; i < Response.size(); i++)
        {
            String ing = Response.get(i).get("_id");
            listOfIngredients.add(ing);
        }
        return listOfIngredients;
    }

    @Step("Make post-request to make burger")
    public static ValidatableResponse makeBurgerWithAuthorization(SelectedIngredientsPOJO ingredient, String token) {

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .body(ingredient)
                .post(ORDERS)
                .then();
    }
}
