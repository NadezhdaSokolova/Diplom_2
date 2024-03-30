import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.lang.reflect.Array;

import static io.restassured.RestAssured.given;



public class UserAPI {

    final static String USERREGISTERED = "/api/auth/register";
    final static String USERDATA = "/api/auth/user";
    final static String USERLOGIN = "/api/auth/login";
    final static String USERLOGOUT = "api/auth/logout";
    final static String DROPPASSWORD = "api/password-reset";
    final static String CHANGEPASSWORD = "password-reset/reset/";



    @Step("Make post-request to create user")
    public static Response createUser(UserPOJO user) {

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USERREGISTERED);
        return response;
    }

    @Step("Make post-request to login of user")
    public static Response authorizedUser(UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USERLOGIN);
        return response;
    }

    @Step("Make delete-request to delete of user")
    public static Response deleteUser(UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .delete(USERDATA);
        return response;
    }

    @Step("Make patch-request to user's data changing")
    public static Response changingDataUser(String tokenNumber,UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .auth().oauth2(tokenNumber)
                .and()
                .body(user)
                .patch(USERDATA);
        return response;
    }

    @Step("Make post-request to drop password")
    public static Response dropPassword(UserPOJO user){

        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(DROPPASSWORD);
        return response;
    }

    @Step("Make request to make logout")
    public static Response makeLogout(String token){
        String newToken = "{{" + token + "}}";
        Response response = given()
                .header("Content-type", "application/json")
                .body(newToken)
                .post(USERLOGOUT);
        return response;

    }

    @Step("Make post-request to change password")
    public static Response changePasswordWithoutAuthorized(PassPOJO pass, String code){

        Response response = given()
                .header("Content-type", "application/json")
                .body(pass)
                .post(CHANGEPASSWORD+"reset-password/"+code);
        return response;
    }

}
