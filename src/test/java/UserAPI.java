import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

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


    @Step("Make request to make logout")
    public static ValidatableResponse makeLogout(String token){
        RefreshTokenPOJO refreshToken = new RefreshTokenPOJO(token);

        return given()
                .header("Content-type", "application/json")
                .body(refreshToken)
                .post(USERLOGOUT)
                .then();
    }

    @Step("Make post-request to getRefreshTokeh after authorization")
    public static ValidatableResponse ResponseToGetRefreshToken(UserPOJO user){

        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USERLOGIN)
                .then();
    }


}
