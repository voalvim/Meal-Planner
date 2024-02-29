package mealplanner.application;

import mealplanner.entities.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
        String USER = "postgres";
        String PASS = "1111";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS meals (" +
                    "category VARCHAR(50)," +
                    "meal VARCHAR(50)," +
                    "meal_id INTEGER);" +

                    "CREATE TABLE IF NOT EXISTS ingredients (" +
                    "ingredient VARCHAR(50)," +
                    "ingredient_id INTEGER," +
                    "meal_id INTEGER);" +

                    "CREATE TABLE IF NOT EXISTS plan(" +
                    "day_of_week VARCHAR(15)," +
                    "meal VARCHAR(50)," +
                    "category VARCHAR(50)," +
                    "meal_id INTEGER);"
            );

            List<Meal> mealList = new ArrayList<>();
            MealApplication.startMealApplication(sc, mealList, connection);
            sc.close();

        }catch (SQLException e) {
            e.getMessage();
        }
   }
}


