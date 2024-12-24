package sample.todoapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Set;
import java.util.function.IntFunction;

public class Settings {
    private static String[] statuses = {"To Do", "In Progress", "Completed", "Blocked"};
    private static String[] categories = {"App", "Household", "Job"};

    public static String[] getStatuses() {
        return statuses;
    }

    public static ObservableList<String> getStatusPropertiees() {

        return FXCollections.observableArrayList(statuses);
    }

    public static String[] getCategories() {
        return categories;
    }

    public static ObservableList<String> getCategoryPropertiees() {

        return FXCollections.observableArrayList(categories);
    }

    public static void setCategories(String[] categories) {
        Settings.categories = categories;
    }
    public static void setCategories(ObservableList<String> categories) {
        Settings.categories = categories.toArray(new String[0]);
    }
}
