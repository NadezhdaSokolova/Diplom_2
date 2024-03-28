import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLoginTest {

    String email = "nadezhda0@yandex.ru";
    String password = "111111";
    String name = "Надежда";
    UserPOJO user1 = new UserPOJO(email, password, name);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        //создаем тестового пользователя
        UserAPI.createUser(user1);
    }

    @After
    public void deleteTestUser(){

        try  {
            UserAPI.authorizedUser(user1);
            UserAPI.deleteUser(user1);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }
    }

    @Test
    @Description("Check that possible to take access to the site with correct email/password data")
    public void checkSuccessAuthorization() {
        UserAPI.authorizedUser(user1).then().statusCode(200)
                .and()
                .assertThat().body("accessToken", notNullValue());
    }

    @Test
    @Description("Check that impossible to take access to the site with incorrect email")
    public void checkImpossibleAuthorizationWithIncorrectEmail() {
        UserPOJO user = new UserPOJO ("L"+email,password,name);
        UserAPI.authorizedUser(user).then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Check that success parameter got a true value")
    public void checkSuccessParameterUserLogin() {
        UserAPI.authorizedUser(user1).then().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Check that AccessToken is Present")
    public void checkAccessTokenIsPresentDuringLogin() {
        UserAPI.authorizedUser(user1).then()
                .assertThat().body("accessToken", notNullValue());
    }

    @Test
    @Description("Check that RefreshToken is Present")
    public void checkRefreshTokenIsPresentDuringLogin() {
        UserAPI.authorizedUser(user1).then()
                .assertThat().body("refreshToken", notNullValue());
    }



    @Test
    @Description("Check that impossible to take access to the site with incorrect password")
    public void checkImpossibleAuthorizationWithPassword() {
        UserPOJO user = new UserPOJO (email,"L"+password,name);
        UserAPI.authorizedUser(user).then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }




    //            String token = UserAPI.autorizedUser(user1).header("Authorization");
//            String[] split = token.split(" ");
//            String tokenNumber = split[1].substring(0, split[1].length() - 1);
//            System.out.println(tokenNumber);


}
