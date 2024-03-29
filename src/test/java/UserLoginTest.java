import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class UserLoginTest {

    String email = "nadezhda100@yandex.ru";
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
    @Description("Check that email correctly return after authorization")
    public void checkEmailReturnCorrectly() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        String response = UserAPI.authorizedUser(user1).body().asString();
        String[] splited = response.split(";");
        String savedEmail = splited[0].substring(326, 347);


        assertEquals ("Логин некорректно вернулся", email.toLowerCase(), savedEmail.toLowerCase());
    }

    @Test
    @Description("Check that name is correclty return after authorization")
    public void checkNameReturnCorrectly() {
        //UserPOJO user = new UserPOJO(email, password, "Надежда");

        String response = UserAPI.authorizedUser(user1).body().asString();
        String[] splited1 = response.split(";");
        String savedName = splited1[0].substring(357,364);

        System.out.println(savedName);

        assertEquals ("Имя некорректно сохранено", "Надежда", savedName);
    }



    @Test
    @Description("Check that impossible to take access to the site with incorrect password")
    public void checkImpossibleAuthorizationWithPassword() {
        UserPOJO user = new UserPOJO (email,"L"+password,name);
        UserAPI.authorizedUser(user).then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }


}
