import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserDataChangingTest {

    String email = "nadezhda21@yandex.ru";
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
    public void checkSuccessfullyNameChangingWhenAuthorized(){

        String token = UserAPI.authorizedUser(user1).body().asString();
            String[] split = token.split(" ");
            String tokenNumber = "Bearer " + split[1].substring(0, split[1].length() - 1);
            System.out.println(tokenNumber);

        UserPOJO user2 = new UserPOJO(null, null, name+"1");

        UserAPI.changingDataUser(tokenNumber, user2).then().statusCode(200)
                .and()
                .body("success", equalTo(true));

        UserAPI.authorizedUser(user2).then().body("success", equalTo(true));
    }
}
