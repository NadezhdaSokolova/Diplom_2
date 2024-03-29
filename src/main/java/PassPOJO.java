public class PassPOJO {

    private String password;

    private String code;

    public PassPOJO(String password, String code){
        this.password = password;
        this.code = code;
    }

    public PassPOJO() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
