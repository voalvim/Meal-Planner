package mealplanner.application;

import mealplanner.entities.Meal;
import mealplanner.entities.enums.MealEnum;

import java.sql.*;
import java.util.*;

public abstract class MealApplication {
    public static void startMealApplication(Scanner sc, List<Meal> mealList, Connection conn) {
        boolean running = true;

        while (running) {
            System.out.println("What would you like to do (add, show, exit)?");
            String option = sc.nextLine();
            switch (option) {
                case "add":
                    addMeal(sc, mealList, conn);
                    break;
                case "show":
                    try {
                        showMeal(sc, mealList, conn);
                    } catch (Exception e) {
                        e.printStackTrace();
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
    }
    private static void addMeal(Scanner sc, List<Meal> mealList ,Connection conn) {
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

                    Meal createdMeal = new Meal(MealEnum.valueOf(meal.toUpperCase()), mealName, ingredients);

                    String insertIntoMeals = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";

                    String insertIntoIngredients = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id ) VALUES (?, ?, ?)";

                    try(PreparedStatement mealStatement = conn.prepareStatement(insertIntoMeals);
                        PreparedStatement ingredientStatement = conn.prepareStatement(insertIntoIngredients)){

                        mealStatement.setString(1, createdMeal.getMealCategory().toString());
                        mealStatement.setString(2, createdMeal.getMealName());
                        int mealId = idGenerator();
                        mealStatement.setInt(3, mealId);

                        mealStatement.executeUpdate();


                        for (String ingredient : createdMeal.getIngredients()) {
                            ingredientStatement.setString(1, ingredient);
                            ingredientStatement.setInt(2, idGenerator());
                            ingredientStatement.setInt(3, mealId);
                            ingredientStatement.executeUpdate();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("The meal has been added!");

                    mealList.add(createdMeal);
                    addMeal = false;
                    break;
                default:
                    System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                    break;
            }
        }
    }

    private static void showMeal(Scanner sc, List<Meal> mealList, Connection conn) {

        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String categoryChoice = sc.next();
        sc.nextLine();

        while(!categoryChoice.equals("breakfast") && !categoryChoice.equals("lunch") && !categoryChoice.equals("dinner")) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            categoryChoice = sc.next();
            sc.nextLine();
        }
        String uppercaseCategoryChoice = categoryChoice.toUpperCase(); //categories are stored as enums which are uppercase

        String categoryQuery = "SELECT * FROM meals WHERE category = ?";

        try(PreparedStatement statement = conn.prepareStatement(categoryQuery)) {
            statement.setString(1, uppercaseCategoryChoice);
            ResultSet categoryResultSet = statement.executeQuery();

            if (!categoryResultSet.next()) {
                System.out.println("No meals found.");
            } else {
                mealList.clear();
                do {
                    int mealId = categoryResultSet.getInt("meal_id");
                    String category = categoryResultSet.getString("category");
                    String mealName = categoryResultSet.getString("meal");
                    List<String> ingredientList = new ArrayList<>();

                    try(PreparedStatement ingredientStatement = conn.prepareStatement("SELECT ingredient FROM ingredients WHERE meal_id = ?")) {
                        ingredientStatement.setInt(1, mealId);
                        ResultSet ingredientResultSet = ingredientStatement.executeQuery();
                        while (ingredientResultSet.next()) {
                            ingredientList.add(ingredientResultSet.getString("ingredient"));
                        }
                    }
                    mealList.add(new Meal(MealEnum.valueOf(category), mealName, ingredientList));
                } while (categoryResultSet.next());

                System.out.println("Category: " + mealList.get(0).getMealCategory().toString().toLowerCase() + "\n");
                for (Meal m : mealList) {
                    System.out.println(m.toString());
                    System.out.println();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isInvalidMealName(String input) {
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

    private static boolean isInvalidIngredientList(List<String> list) {
        for (String ingredient : list) {
            if (ingredient.isBlank() || !ingredientContainsOnlyLetters(ingredient)) {
                return true;
            }
        }
        return false;
    }

    private static boolean ingredientContainsOnlyLetters(String str) {
        String trimmedStr = str.trim();
        for (char c : trimmedStr.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    private static List<String> trimIngredients(List<String> ingredients) {
        List<String> trimmedIngredients = new ArrayList<>();
        for (String ingredient : ingredients) {
            String trimmedIngredient = ingredient.trim();
            trimmedIngredients.add(trimmedIngredient);
        }
        return trimmedIngredients;
    }

    public static int idGenerator() {
        final int MIN_ID = 0;
        final int MAX_ID = 9999;

        Random random = new Random();

        return random.nextInt(MAX_ID - MIN_ID + 1) + MIN_ID;
    }
}
