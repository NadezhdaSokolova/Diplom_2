import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataChangingTest {
    String email = "nadezhda22@yandex.ru";
    String password = "322222";
    String name = "Надежда";
    String code = "c2ca9439-a396-4b65-a7f7-7542d0b95bc7";
    UserPOJO user1 = new UserPOJO(email, password, name);
    UserPOJO user0 = new UserPOJO("6"+email, "111111", name);
    UserPOJO user8 = new UserPOJO("9"+email, "111111", name);

    public static String getToken(UserPOJO user){
        String token = UserAPI.authorizedUser(user).body().asString();
        String[] split = token.split(" ");
        String tokenNumber = split[1].substring(0, 171);
        System.out.println(tokenNumber);
        return tokenNumber;
    }

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
            UserAPI.authorizedUser(user1);
            UserAPI.deleteUser(user1);

            UserAPI.authorizedUser(user0);
            UserAPI.deleteUser(user0);

            UserAPI.authorizedUser(user8);
            UserAPI.deleteUser(user8);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }
    }

    @Test
    @Description("Check that possible change the name in the profile")
    public void checkSuccessfullyNameChangingWhenAuthorized(){

        UserPOJO user2 = new UserPOJO(null, null, name+"1");

        UserAPI.changingDataUser(UserDataChangingTest.getToken(user1), user2).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Check that possible change the email in the profile")
    public void checkSuccessfullyEmailChangingWhenAuthorized(){
        UserPOJO user3 = new UserPOJO(RandomStringUtils.randomAlphabetic(10)+email, null, null);
        UserAPI.changingDataUser(UserDataChangingTest.getToken(user1), user3).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Check that impossible change the email if it already present in the system in another user")
    public void checkImpossibleEmailChangingWhenItExistInTheSystem(){

        UserAPI.changingDataUser(UserDataChangingTest.getToken(user1), user0).then()
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
        UserPOJO user6 = new UserPOJO("nadezhda0016@yandex.ru", "211111", name);
        UserPOJO user7 = new UserPOJO("nadezhda0016@yandex.ru", "6tyhu7", name);
        UserAPI.createUser(user6);

        ValidatableResponse authorization = UserAPI.ResponseToGetRefreshToken(user6);
        String accessTokenWithBearer = authorization.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.substring(7, accessTokenWithBearer.length());
        String refreshToken = authorization.extract().path("refreshToken");

        System.out.println(accessToken);
        System.out.println(refreshToken);

        UserAPI.changingDataUser(accessToken, user7).then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        int a = UserAPI.makeLogout(refreshToken).extract().statusCode();

        System.out.println(a);

        //UserPOJO user6WithNewPassword = new UserPOJO("nadezhda0013@yandex.ru", "6tyhu7", name);

       String b = UserAPI.ResponseToGetRefreshToken(user7).extract().asPrettyString();
        System.out.println(b);

        UserAPI.deleteUser(user7).then().body("message", equalTo("z"));
    }
}
