import java.util.ArrayList;

public class SelectedIngredientsPOJO {
    private ArrayList<String> ingredients;

    public SelectedIngredientsPOJO(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }

    public SelectedIngredientsPOJO(){
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }


}
