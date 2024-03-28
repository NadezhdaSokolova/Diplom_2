import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserAPI {

    final static String USERREGISTERED = "/api/auth/register";
    final static String USERDATA = "/api/auth/user";
    final static String USERLOGIN = "/api/auth/login";


    @Step("Make post-request to create user")
    public static Response createUser(UserPOJO user) {

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USERREGISTERED);

        return response;

    }

    @Step("Make post-request to login of courier")
    public static Response authorizedUser(UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USERLOGIN);

        return response;

    }

    @Step("Make delete-request to delete of courier")
    public static Response deleteUser(UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .delete(USERDATA);

        return response;

    }


}
