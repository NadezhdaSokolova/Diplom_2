public class OrderPOJO {

    // поля соответствуют ключам json
    private String[] ingredients;

    // добавляем конструкторы: для инициализации полей + пустой для библиотеки

    public OrderPOJO(String[] ingredients){
        this.ingredients = ingredients;
    }

    public OrderPOJO(){
    }

    // устанавливаем гетерры и сеттеры

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

}
