import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;

public class OrderCreationTest {
    UserPOJO user1 = new UserPOJO("nadezhda300@yandex.ru", "111111", "Надежда");

    public static String getToken(UserPOJO user){
        String token = UserAPI.authorizedUser(user).body().asString();
        String[] split = token.split(" ");
        String tokenNumber = split[1].substring(0, 171);
        return tokenNumber;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        //создаем тестового пользователя
        UserAPI.createUser(user1);
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
    @Description("Check that possible to make an order with ingredient if unauthorized")
    public void checkPossibilityToMakeAnOrderWithIngredientIfUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        int statusCode = order.extract().statusCode();
        assertEquals("Статус код не соответствует спецификации",200, statusCode);
   }
    @Test
    @Description("Check success parameter in body if make order when unauthorized")
    public void checkSuccessParameterInBodyIfMakeOrderWhenUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        boolean status = order.extract().path("success");
        assertTrue(status);
    }
    @Test
    @Description("Check name parameter in body if make order when unauthorized")
    public void checkNameParameterInBodyIfMakeOrderWhenUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        String name = order.extract().path("name");
        assertEquals("Имя бургера сформировалось не верно", "Экзо-плантаго альфа-сахаридный минеральный space традиционный-галактический флюоресцентный фалленианский люминесцентный spicy метеоритный краторный антарианский бессмертный био-марсианский астероидный бургер", name);
    }
    @Test
    @Description("Check order's number parameter in body if make order when unauthorized")
    public void checkNumberOfOrderParameterInBodyIfMakeOrderWhenUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        HashMap<String, Integer> name = order.extract().path("order");
        assertNotEquals("Номер не присвоен", name.get("number"), null);
    }
    @Test
    @Description("Check message error when making an burger without ingredient when unauthorized")
    public void checkMessageErrorWhenMakingAnBurgerWithoutIngredientWhenUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        String message = order.extract().path("message");
        assertEquals("Сообщение об ошибке не соответствует спецификации", "Ingredient ids must be provided", message);
    }
    @Test
    @Description("Check status when making an burger without ingredient when unauthorized")
    public void checkThatImpossibleMakeAnBurgerWithoutIngredientWhenUnauthorized() {
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        int statusCode = order.extract().statusCode();
        assertEquals("Статус код не соответствует спецификации", 400, statusCode);
    }
    @Test
    @Description("Check success parameter in body making an burger without ingredient when unauthorized")
    public void checkSuccessParameterFalseInBodyMakingAnBurgerWithoutIngredientWhenUnauthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        boolean status = order.extract().path("success");
        assertFalse(status);
    }
    @Test
    @Description("Check that possible to make an order with ingredient if authorized")
    public void checkPossibilityToMakeAnOrderWithIngredientIfAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));
        int statusCode = order.extract().statusCode();
        assertEquals("С авторизованным пользователем не создается",200, statusCode);
    }
    @Test
    @Description("Check success parameter in body if make order when authorized")
    public void checkSuccessParameterInBodyIfMakeOrderWhenAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));
        boolean status = order.extract().path("success");
        assertTrue(status);
    }

    @Test
    @Description("Check name parameter in body if make order when authorized")
    public void checkNameParameterInBodyIfMakeOrderWhenAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));;
        String name = order.extract().path("name");
        assertEquals("Имя бургера сформировалось не верно", "Экзо-плантаго альфа-сахаридный минеральный space традиционный-галактический флюоресцентный фалленианский люминесцентный spicy метеоритный краторный антарианский бессмертный био-марсианский астероидный бургер", name);
    }

    @Test
    @Description("Check order's number parameter in body if make order when authorized")
    public void checkNumberOfOrderParameterInBodyIfMakeOrderWhenAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(OrderAPI.arrayOfIngredients());
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));
        HashMap<String, Integer> name = order.extract().path("order");
        assertNotEquals("Номер не присвоен", name.get("number"), null);
    }
    @Test
    @Description("Check message error when making an burger without ingredient when authorized")
    public void checkMessageErrorWhenMakingAnBurgerWithoutIngredientWhenAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));;
        String message = order.extract().path("message");
        assertEquals("Сообщение об ошибке не соответствует спецификации", "Ingredient ids must be provided", message);
    }
    @Test
    @Description("Check status when making an burger without ingredient when authorized")
    public void checkThatImpossibleMakeAnBurgerWithoutIngredientWhenAuthorized() {
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));;
        int statusCode = order.extract().statusCode();
        assertEquals("Статус код не соответствует спецификации", 400, statusCode);
    }
    @Test
    @Description("Check success parameter in body making an burger without ingredient when authorized")
    public void checkSuccessParameterFalseInBodyMakingAnBurgerWithoutIngredientWhenAuthorized(){
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO();
        ValidatableResponse order = OrderAPI.makeBurgerWithAuthorization(ingredient, OrderCreationTest.getToken(user1));;
        boolean status = order.extract().path("success");
        assertFalse(status);
    }


    @Test
    @Description("Check Internal Server Error when making an burger with invalid hash of ingredient when unauthorized")
    public void checkInternalServerErrorWhenMakingAnBurgerWithInvalidHashOfIngredientWhenUnauthorized(){
        ArrayList<String> incorrectIngredients = new ArrayList<>();
        incorrectIngredients.add("123");
        SelectedIngredientsPOJO ingredient = new SelectedIngredientsPOJO(incorrectIngredients);
        ValidatableResponse order = OrderAPI.makeBurger(ingredient);
        int status = order.extract().statusCode();
        assertEquals("Статус код не соответствует спецификации", 500, status);
    }


}
