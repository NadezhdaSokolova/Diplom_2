import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class UserCreationTest {


    String email = "nadezhda42" + RandomStringUtils.randomAlphabetic(20) + "@yandex.ru";
    String password = "qwerty1232" + RandomStringUtils.randomAlphanumeric(20);


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

         //создаем тестового пользователя
        UserPOJO user1 = new UserPOJO("Nadezhda21@yandex.ru", "111111", "Надежда");
        UserAPI.createUser(user1);
    }

    @After
    public void deleteTestUser(){
        UserPOJO user1 = new UserPOJO("Nadezhda21@yandex.ru", "111111", "Надежда");

        try  {
            UserAPI.authorizedUser(user1);
            UserAPI.deleteUser(user1);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }
    }


    @Test
    @Description("Check that possible to make registration with correct email/password data")
    public void checkUniqueSuccessUserCreation() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        UserAPI.createUser(user).then().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Check that AccessToken is provided after registration")
    public void checkAccessTokenForUserCreationIsPresent() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        UserAPI.createUser(user).then()
                .assertThat().body("accessToken", notNullValue());
    }

    @Test
    @Description("Check that RefreshToken is provided after registration")
    public void checkRefreshTokenForUserCreationIsPresent() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        UserAPI.createUser(user).then()
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @Description("Check that email is correclty saved after registration")
    public void checkEmailSaveCorrectly() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        String response = UserAPI.createUser(user).body().asString();
        String[] splited = response.split(";");
        String savedEmail = splited[0].substring(33, 73);

        assertEquals ("Логин некорректно сохранен", email.toLowerCase(), savedEmail.toLowerCase());
    }

    @Test
    @Description("Check that name is correclty saved after registration")
    public void checkNameSaveCorrectly() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        String response = UserAPI.createUser(user).body().asString();
        String[] splited1 = response.split(";");
        String savedName = splited1[0].substring(83,90);

        System.out.println(savedName);

        assertEquals ("Имя некорректно сохранено", "Надежда", savedName);
    }



    @Test
    @Description("Check that impossible to make registration with empty required Name field")
    public void checkAttemptUserCreationWithoutRequiredNameField() {
        UserPOJO user = new UserPOJO(email, password, null);

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @Test
    @Description("Check that impossible to make registration with empty required password field")
    public void checkAttemptUserCreationWithoutRequiredPasswordField() {
        UserPOJO user = new UserPOJO(email, null, "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Check that impossible to make registration with empty required email field")
    public void checkAttemptUserCreationWithoutRequiredEmailField() {
        UserPOJO user = new UserPOJO(null, password, "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @Test
    @Description("Check that impossible to make registration if user have already registered with the same email/password data")
    public void checkAttemptAlreadyExistedUserCreation() {

        // сперва пользователя нужно создать в предусловии
        UserPOJO user = new UserPOJO("Nadezhda21@yandex.ru", "111111", "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

}

