package mealplanner.entities;

import mealplanner.entities.enums.MealEnum;

import java.util.List;

public class Meal {
    private MealEnum mealCategory;
    private String mealName;
    private List<String> ingredients;

    public Meal(MealEnum mealCategory, String mealName, List<String> ingredients) {
        this.mealCategory = mealCategory;
        this.mealName = mealName;
        this.ingredients = ingredients;
    }

    public MealEnum getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(MealEnum mealCategory) {
        this.mealCategory = mealCategory;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public String toString() {


        return  "Name: " + getMealName() + "\n"
                + "Ingredients:\n" + String.join(System.lineSeparator(), ingredients);
    }
}
