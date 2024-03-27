import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreationTest {

    String email = "Nadezhda42" + Math.random() + "@yandex.ru";
    String password = "qwerty1232" + Math.random();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        // создаем тестового пользователя
        UserPOJO user1 = new UserPOJO("Nadezhda5@yandex.ru", "111111", "Надежда");

        String token = UserAPI.createUser(user1).header("Authorization");
        String[] split = token.split(" ");
        String tokenNumber = split[1].substring(0, split[1].length() - 1);
        System.out.println(tokenNumber);
    }

    @After
    public void deleteTestUser(){
        UserPOJO user1 = new UserPOJO("Nadezhda5@yandex.ru", "111111", "Надежда");

        try  {

            String token = UserAPI.autorizedUser(user1).header("Authorization");
            String[] split = token.split(" ");
            String tokenNumber = split[1].substring(0, split[1].length() - 1);
            System.out.println(tokenNumber);

            UserAPI.deleteUser(user1);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }

    }


    @Test
    public void uniqueSuccessUserCreation() {
        UserPOJO user = new UserPOJO(email, password, "Надежда");

        UserAPI.createUser(user).then().statusCode(200)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    public void AttemptUserCreationWithoutRequiredNameField() {
        UserPOJO user = new UserPOJO(email, password, null);

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @Test
    public void AttemptUserCreationWithoutRequiredPasswordField() {
        UserPOJO user = new UserPOJO(email, null, "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void AttemptUserCreationWithoutRequiredEmailField() {
        UserPOJO user = new UserPOJO(null, password, "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @Test
    public void AttemptAlreadyExistedUserCreation() {

        // сперва пользователя нужно создать в предусловии
        UserPOJO user = new UserPOJO("Nadezhda5@yandex.ru", "111111", "Надежда");

        UserAPI.createUser(user).then().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

}

