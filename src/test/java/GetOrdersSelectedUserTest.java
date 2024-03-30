import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    @Description("Check StatusCode when make request to get list of orders if authorized")
    public void checkStatusCodeMakingRequestToGetListOfOrdersIfAuthorized(){
       ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(GetOrdersSelectedUserTest.getToken(user1));
       int statusCode = lislOfOrders.extract().statusCode();
       assertEquals(statusCode,200);
    }
    @Test
    @Description("Check success parameter in body or not request to get list of orders if authorized")
    public void checkIfSuccessfullyRequestToGetListOfOrdersIfAuthorized(){
        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(GetOrdersSelectedUserTest.getToken(user1));
        boolean status = lislOfOrders.extract().path("success");
        assertTrue(status);
    }

    @Test
    @Description("Check StatusCode when try to get list of orders if unauthorized")
    public void checkStatusCodeWhenTryToGetListOfOrdersIfUnauthorized(){
        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser("");
        int statusCode = lislOfOrders.extract().statusCode();
        assertEquals(statusCode,401);
    }

    @Test
    @Description("Check message when try to get list of orders if unauthorized")
    public void checkMessageWhenTryToGetListOfOrdersIfUnauthorized(){
        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser("");
        String message = lislOfOrders.extract().path("message");
        assertEquals("Сообщение об ошибке не соответствует спецификации", message, "You should be authorised");
    }


    @Test
    @Description("Check quontaty of orders")
    public void checkQuontatyOfOrders(){

        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(GetOrdersSelectedUserTest.getToken(user1));
        int totalToday = lislOfOrders.extract().path("totalToday");
        assertEquals(0, totalToday);
    }

}
