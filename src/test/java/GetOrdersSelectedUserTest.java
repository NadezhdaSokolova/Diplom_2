import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersSelectedUserTest {


    public static String getToken(UserPOJO user){
        String token = UserAPI.authorizedUser(user).body().asString();
        String[] split = token.split(" ");
        String tokenNumber = split[1].substring(0, 171);
        System.out.println(tokenNumber);
        return tokenNumber;
    }

    UserPOJO user1 = new UserPOJO("Nadezhda24@yandex.ru", "111111", "Надежда");

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
    @Description("Check that possible to get list of orders if authorized")
    public void checkImpossibleToGetListOfOrdersIfAuthorized(){

        OrderAPI.getListOfOrdersOfUser(GetOrdersSelectedUserTest.getToken(user1)).
                then().statusCode(200)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @Description("Check that impossible to get list of orders if unauthorized")
    public void checkImpossibleToGetListOfOrdersIfUnauthorized(){

        OrderAPI.getListOfOrdersOfUser("").
                then().statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));

    }

    @Test
    @Description("Check quontaty of orders")
    public void checkquontatyOfOrders(){

        OrderAPI.getListOfOrdersOfUser(GetOrdersSelectedUserTest.getToken(user1)).
                then().statusCode(200)
                .and()
                .body("total", equalTo(46215));

    }



}
