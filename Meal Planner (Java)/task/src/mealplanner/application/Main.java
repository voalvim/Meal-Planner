package mealplanner.application;

import mealplanner.entities.Meal;
import mealplanner.entities.enums.MealEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        List<Meal> mealList = new ArrayList<>();

        while (running) {
            System.out.println("What would you like to do (add, show, exit)?");
            String option = sc.nextLine();
            switch (option) {
                case "add":
                    boolean addMeal = true;
                    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
                    while (addMeal) {
                        String meal = sc.nextLine();
                        switch (meal) {
                            case "breakfast":
                            case "lunch":
                            case "dinner":
                                System.out.println("Input the meal's name: ");
                                String mealName = sc.nextLine();
                                if (isInvalidMealName(mealName)) {
                                    do {
                                        System.out.println("Wrong format. Use letters only!");
                                        mealName = sc.nextLine();
                                    } while (isInvalidMealName(mealName));
                                }

                                System.out.println("Input the ingredients: ");
                                String ingredientsString = sc.nextLine();
                                List<String> untrimmedIngredients = Arrays.stream(ingredientsString.split(",")).toList();

                                if (isInvalidIngredientList(untrimmedIngredients)) {
                                    do {
                                        System.out.println("Wrong format. Use letters only!");
                                        ingredientsString = sc.nextLine();
                                        untrimmedIngredients = Arrays.stream(ingredientsString.split(",")).toList();
                                    } while (isInvalidIngredientList(untrimmedIngredients));
                                }
                                List<String> ingredients = trimIngredients(untrimmedIngredients);

                                Meal meal1 = new Meal(MealEnum.valueOf(meal.toUpperCase()), mealName, ingredients);

                                System.out.println("The meal has been added!");

                                mealList.add(meal1);
                                addMeal = false;
                                break;
                            default:
                                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                                break;
                        }
                    }
                    break;

                case "show":
                    if (mealList.isEmpty()) {
                        System.out.println("No meals saved. Add a meal first.");
                    } else {
                        for (Meal m : mealList) {
                            System.out.println(m.toString());
                            System.out.println();
                        }
                    }
                    break;

                case "exit":
                    System.out.println("Bye!");
                    running = false;
                    break;

                default:
                    break;
            }
        }
        sc.close();
    }

    static boolean isInvalidMealName(String input) {
        if (input == null || input.isEmpty() || input.isBlank()) {
            return true;
        }

        for (char letter : input.toCharArray()) {
            if (!Character.isLetter(letter) && !Character.isWhitespace(letter)) {
                return true;
            }
        }

        return false;
    }

    static boolean isInvalidIngredientList(List<String> list) {
        for (String ingredient : list) {
            if (ingredient.isBlank() || !ingredientContainsOnlyLetters(ingredient)) {
                return true;
            }
        }
        return false;
    }

    static boolean ingredientContainsOnlyLetters(String str) {
        String trimmedStr = str.trim();
        for (char c : trimmedStr.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    static List<String> trimIngredients(List<String> ingredients) {
        List<String> trimmedIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            String trimmedIngredient = ingredient.trim();
            trimmedIngredients.add(trimmedIngredient);
        }
        return trimmedIngredients;
    }

}


