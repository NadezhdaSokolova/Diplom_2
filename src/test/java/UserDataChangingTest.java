import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UserDataChangingTest {
    String email = "nadezhda52@yandex.ru";
    String password = "322222";
    String name = "Надежда";
    String code = "c2ca9439-a396-4b65-a7f7-7542d0b95bc7";
    UserPOJO user1 = new UserPOJO(email, password, name);
    UserPOJO user0 = new UserPOJO("6"+email, "111111", name);
    UserPOJO user8 = new UserPOJO("9"+email, "111111", name);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        //создаем тестового пользователя
        UserAPI.createUser(user1);
        UserAPI.createUser(user0);
        UserAPI.createUser(user8);
    }

    @After
    public void deleteTestUser(){

        try  {
            UserAPI.makeLogout(UserAPI.getToken(user1));
            UserAPI.deleteUser(UserAPI.getToken(user1),user1);

            UserAPI.makeLogout(UserAPI.getToken(user0));
            UserAPI.deleteUser(UserAPI.getToken(user0),user0);

            UserAPI.makeLogout(UserAPI.getToken(user8));
            UserAPI.deleteUser(UserAPI.getToken(user8),user8);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }
    }

    @Test
    @Description("Check that possible change the name in the profile")
    public void checkSuccessfullyNameChangingWhenAuthorized(){

        UserPOJO user2 = new UserPOJO(null, null, name+"1");

        UserAPI.changingDataUser(UserAPI.getToken(user1), user2).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @Description("Check that possible change the email in the profile")
    public void checkSuccessfullyEmailChangingWhenAuthorized(){
        UserPOJO user3 = new UserPOJO("nadezhda1028@yandex.ru", "111111", "Y");
        UserPOJO user10 = new UserPOJO("nadezhda2028@yandex.ru", "111111", "Y");
        UserAPI.createUser(user3);

        ValidatableResponse authorization1 = UserAPI.ResponseToGetRefreshToken(user3);
        String accessTokenWithBearer1 = authorization1.extract().path("accessToken");
        String accessToken = accessTokenWithBearer1.substring(7, accessTokenWithBearer1.length());
        String refreshToken1 = authorization1.extract().path("refreshToken");


        UserAPI.changingDataUser(accessToken, user10).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        UserAPI.makeLogout(refreshToken1);


        // авторизуемся с новым email
        ValidatableResponse authorization2 = UserAPI.ResponseToGetRefreshToken(user10);
        assertEquals("Авторизация с новым email не прошла",200, authorization2.extract().statusCode());

        String newAccessTokenWithBearer2 = authorization2.extract().path("accessToken");
        String newAccessToken = newAccessTokenWithBearer2.substring(7, newAccessTokenWithBearer2.length());

        UserAPI.deleteUser(newAccessToken,user10).then().body("message", equalTo("User successfully removed"));
    }

    @Test
    @Description("Check that impossible change the email if it already present in the system in another user")
    public void checkImpossibleEmailChangingWhenItExistInTheSystem(){

        UserAPI.changingDataUser(UserAPI.getToken(user1), user0).then()
                .statusCode(403)
                .and()
                .body("message", equalTo("User with such email already exists"));
    }


    @Test
    @Description("Check that impossible to change user email and name if unauthorized")
    public void checkImpossibleToChangeUserDataIfUnauthorized(){

        UserAPI.changingDataUser("", user0).then()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }


    @Test
    @Description("Check that impossible to change the password when unauthorized")
    public void checkThatImpossibleToChangeThePasswordWhenUnauthorized(){
        UserAPI.changingDataUser("", user8).then()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }



    @Test
    @Description("Check that possible change the password in the profile")
    public void checkSuccessfullyPasswordChangingWhenAuthorized(){
        UserPOJO user6 = new UserPOJO("nadezhda0018@yandex.ru", "211111", name);
        UserPOJO user7 = new UserPOJO("nadezhda0018@yandex.ru", "6tyhu7", name);
        UserAPI.createUser(user6);

        ValidatableResponse authorization = UserAPI.ResponseToGetRefreshToken(user6);
        String accessTokenWithBearer = authorization.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.substring(7, accessTokenWithBearer.length());
        String refreshToken = authorization.extract().path("refreshToken");

        // проверяем смену пароля

        UserAPI.changingDataUser(accessToken, user7).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        // разлогиниваемся

        UserAPI.makeLogout(refreshToken);

        // авторизуемся с новым паролем
        ValidatableResponse newAuthorization = UserAPI.ResponseToGetRefreshToken(user7);
        assertEquals("Авторизация с новым паролем не прошла",200, newAuthorization.extract().statusCode());

        String newAccessTokenWithBearer = newAuthorization.extract().path("accessToken");
        String newAccessToken = newAccessTokenWithBearer.substring(7, accessTokenWithBearer.length());

        UserAPI.deleteUser(newAccessToken,user7).then().body("message", equalTo("User successfully removed"));
    }
}
