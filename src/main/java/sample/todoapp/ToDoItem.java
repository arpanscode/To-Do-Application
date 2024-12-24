package sample.todoapp;

import javafx.beans.property.SimpleStringProperty;

public class ToDoItem {
    // this is called the model class. this is the data model of the table data
    private SimpleStringProperty category;
    private SimpleStringProperty description;
    private SimpleStringProperty status;


    public ToDoItem() {
        this.category = new SimpleStringProperty("New");
        this.description = new SimpleStringProperty("");
        this.status = new SimpleStringProperty("T");
    }

    public ToDoItem(String category, String description) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty("T");
    }

    public ToDoItem(String category, String description, String status) {
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        if(status == null) {
            this.status = new SimpleStringProperty("T");
        }  else{
            this.status = new SimpleStringProperty(codifyStatusIntput(status));
        }
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category = new SimpleStringProperty(category);
    }


    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public String getStatus() {
        return codifyStatusOutput(status.get());
    }


    public void setStatus(String status) {
        this.status = new SimpleStringProperty(codifyStatusIntput(status));
    }

    private String codifyStatusOutput(String status)
    {
        String status_long = new String();
        if (status.toUpperCase().equals("T")) {
            status_long = "To Do";
        } else if (status.toUpperCase().equals("P")) {
            status_long = "In Progress";
        } else if (status.toUpperCase().equals("C")) {
            status_long = "Completed";
        }else if (status.toUpperCase().equals("B")) {
            status_long = "Blocked";
        }
        return status_long;
    }

    private String codifyStatusIntput(String status_long)
    {
        String status = new String();
        if (status_long.toLowerCase().equals("to do")) {
            status = "T";
        } else if (status_long.toLowerCase().equals("in progress")) {
            status = "P";
        } else if (status_long.toLowerCase().equals("completed")) {
            status = "C";
        }else if (status_long.toLowerCase().equals("blocked")) {
            status = "B";
        }
        return status;
    }

}
