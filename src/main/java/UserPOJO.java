public class UserPOJO {

    // поля соответствуют ключам json

    private String email;
    private String password;
    private String name;

    // добавляем конструкторы: для инициализации полей + пустой для библиотеки

    public UserPOJO(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserPOJO(){

    }

    // устанавливаем гетерры и сеттеры

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }


}
