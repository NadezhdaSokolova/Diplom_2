import io.restassured.RestAssured;
import org.junit.Before;

public class UserCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth/register";
    }


}
