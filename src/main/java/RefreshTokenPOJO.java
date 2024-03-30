import java.util.ArrayList;

public class RefreshTokenPOJO {

    private String token;

    public RefreshTokenPOJO(String token){
        this.token = token;
    }

    public RefreshTokenPOJO(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
