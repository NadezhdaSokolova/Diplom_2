import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersSelectedUserTest {

    UserPOJO user1 = new UserPOJO("Ннн24@yandex.ru" + RandomStringUtils.randomAlphabetic(4), "111111", "Надежда");
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        //создаем тестового пользователя
        UserAPI.createUser(user1);

        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));
    }

    @After
    public void deleteTestUser(){
        try  {

            UserAPI.deleteUser(UserAPI.getToken(user1),user1);
        }
        catch (Exception e){
            System.out.println ("Удалять нечего. Пользователь не прошел авторизацию.");
        }
    }

    @Test
    @Description("Check StatusCode when make request to get list of orders if authorized")
    public void checkStatusCodeMakingRequestToGetListOfOrdersIfAuthorized(){
       ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(UserAPI.getToken(user1));
       int statusCode = lislOfOrders.extract().statusCode();
       assertEquals(statusCode,200);
    }
    @Test
    @Description("Check success parameter in body or not request to get list of orders if authorized")
    public void checkIfSuccessfullyRequestToGetListOfOrdersIfAuthorized(){
        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(UserAPI.getToken(user1));
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

        ValidatableResponse authorization = UserAPI.ResponseToGetRefreshToken(user1);
        String accessTokenWithBearer = authorization.extract().path("accessToken");
        String accessToken = accessTokenWithBearer.substring(7, accessTokenWithBearer.length());

        ValidatableResponse lislOfOrders =  OrderAPI.getListOfOrdersOfUser(accessToken);
        ArrayList orders = lislOfOrders.extract().path("orders");
        assertEquals(1, orders.size());
    }
}
