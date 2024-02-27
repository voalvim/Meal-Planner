package mealplanner.entities;

import mealplanner.entities.enums.MealEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Meal {
    private MealEnum meal;
    private String mealName;
    private List<String> ingredients;

    public Meal(MealEnum meal, String mealName, List<String> ingredients) {
        this.meal = meal;
        this.mealName = mealName;
        this.ingredients = ingredients;
    }

    public MealEnum getMeal() {
        return meal;
    }

    public void setMeal(MealEnum meal) {
        this.meal = meal;
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


        return "Category: " + getMeal().toString().toLowerCase() + "\n"
                + "Name: " + getMealName() + "\n"
                + "Ingredients:\n" + String.join(System.lineSeparator(), ingredients);
    }
}
