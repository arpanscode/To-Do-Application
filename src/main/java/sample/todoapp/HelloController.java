package sample.todoapp;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class HelloController {
    @FXML
    private TabPane tab_pane;
    @FXML
    private Tab home_tab;
    @FXML
    private TableView<ToDoItem> table;
    @FXML
    private TableView<String> cattab;
    @FXML
    private TableColumn<String, String> catcol;
    @FXML
    private TableColumn<ToDoItem, String> category, description, status;
    @FXML
    private Button prioritize, choice1, choice2, upbtn, downbtn, addbtn, delbtn, exportbtn;
    @FXML
    private ToggleButton editbtn, fltrbtn;
    @FXML
    private TextField filepath;
    @FXML
    private ComboBox catfltr, statfltr;
    @FXML
    private Label notif_label;
    @FXML
    private ToolBar notifbar;
    @FXML
    private ImageView notif_typ;
    @FXML
    private VBox vbox_root;

    private ObservableList<ToDoItem> toDoItems;
    private FilteredList<ToDoItem> toDoItems_fltr;
    private ObservableList<String> categories;
    private int i, j, items;
    private static Stage theStage;

    public static void setStage(Stage stage) {
        theStage = stage;
    }

    @FXML
    public void initialize() {
        choice1.setVisible(false);
        choice2.setVisible(false);
        description.prefWidthProperty().bind(table.widthProperty().subtract(category.widthProperty().add(status.widthProperty())));
        tab_pane.prefHeightProperty().bind(vbox_root.heightProperty().subtract(notifbar.heightProperty()));
        disableAllButtons();
    }

    @FXML
    protected void onUpload() {
        // read excel and push it into the list
        toDoItems = FileAssist.readFile(filepath.getText());
        toDoItems_fltr = new FilteredList<>(toDoItems);
        categories = Settings.getCategoryPropertiees();


        category.setCellValueFactory(new PropertyValueFactory<>("Category")); //This should be same as get method
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        table.setItems(toDoItems_fltr);

        // this is supposed to be the way to put strings into cells.
        catcol.setCellValueFactory(cellData -> SimpleStringProperty.stringExpression(new SimpleStringProperty(cellData.getValue())));
        cattab.setItems(categories);

        // setting editability
        // having status as dropdown
        table.setEditable(true);
        description.setEditable(false);
        category.setCellFactory(ComboBoxTableCell.forTableColumn(Settings.getCategories()));
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setCellFactory(ComboBoxTableCell.forTableColumn(Settings.getStatuses()));

        cattab.setEditable(true);
        catcol.setCellFactory(TextFieldTableCell.forTableColumn());

        statfltr.setItems(Settings.getStatusPropertiees());
        catfltr.setItems(Settings.getCategoryPropertiees());

        //edit handlers:
        description.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ToDoItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ToDoItem, String> event) {
                toDoItems.get(event.getTablePosition().getRow()).setDescription(event.getNewValue());
                description.setEditable(false);
                editbtn.setSelected(false);
            }
        });
        category.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ToDoItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ToDoItem, String> event) {
                toDoItems.get(event.getTablePosition().getRow()).setCategory(event.getNewValue());
            }
        });
        status.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ToDoItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ToDoItem, String> event) {
                toDoItems.get(event.getTablePosition().getRow()).setStatus(event.getNewValue());
            }
        });

        catcol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<String, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<String, String> event) {
                categories.set(event.getTablePosition().getRow(), event.getNewValue());
                Settings.setCategories(categories);
                catfltr.setItems(Settings.getCategoryPropertiees());
                category.setCellFactory(ComboBoxTableCell.forTableColumn(Settings.getCategories()));
            }
        });

        if (toDoItems.size() != 0) {
            enableAllButtons();
        }
    }

    @FXML
    protected void onBrowse() {
        String message = new String();
        FileChooser browse = new FileChooser();
        browse.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = browse.showOpenDialog(theStage);
        if (file != null) {
            message = file.getAbsolutePath();
        }
        filepath.setText(message);
    }

    @FXML
    protected void onExport() {
        if (filepath.getText().isEmpty()) {
            onBrowse();
        }
        if ((!toDoItems.isEmpty()) && (!filepath.getText().isEmpty())) {
            FileAssist.writeFile(toDoItems, filepath.getText());
            sendAlert("File exported Successfully!", 'S');
        } else {
            sendAlert("Could not export the file.", 'E');
        }

    }

    @FXML
    protected void onPrioritize() {
        if (toDoItems != null) {
            fltrbtn.setSelected(false);
            FilterToggle();
            switch (prioritize.getText()) {
                case "Start Prioritizer":
                    items = toDoItems.size();
                    ToDoItem item = null;
                    for (int num = 0; num < items; num++) {
                        if (toDoItems.get(num).getStatus().toLowerCase().equals("completed")) {
                            item = toDoItems.get(num);
                            toDoItems.remove(num);
                            toDoItems.add(item);
                        }
                    }

                    i = 0;
                    j = i + 1;
                    choice1.setText(toDoItems.get(i).getDescription());
                    choice2.setText(toDoItems.get(j).getDescription());
                    choice1.setVisible(true);
                    choice2.setVisible(true);
                    prioritize.setText("Cancel Prioritization");
                    break;

                case "Cancel Prioritization":
                    choice1.setVisible(false);
                    choice2.setVisible(false);
                    i = 0;
                    j = 0;
                    choice1.setText("Choice 1");
                    choice2.setText("Choice 2");
                    sendAlert("Prioritization cancelled by user", 'W');
                    prioritize.setText("Start Prioritizer");
            }
        }

    }

    @FXML
    protected void onChoice(ActionEvent action) {
        ToDoItem temp_item;
        String id = ((Button) action.getSource()).getId();
        if (id.equals("choice2")) {
            temp_item = toDoItems.get(j);
            toDoItems.remove(j);
            toDoItems.add(i, temp_item);
        }
        changeAnimation();
        iteratePrioritizer();

    }

    @FXML
    protected void moveUp() {
        ToDoItem item = null;
        int i = table.getSelectionModel().getFocusedIndex();
        if (i != 0) {
            item = toDoItems.get(i);
            toDoItems.set(i, toDoItems.get(i - 1));
            toDoItems.set(i - 1, item);
            table.getSelectionModel().select(i - 1);
        }

    }

    @FXML
    protected void moveDown() {
        ToDoItem item = null;
        int i = table.getSelectionModel().getFocusedIndex();
        if (i != (toDoItems.size() - 1)) {
            item = toDoItems.get(i);
            toDoItems.set(i, toDoItems.get(i + 1));
            toDoItems.set(i + 1, item);
            table.getSelectionModel().select(i + 1);
        }

    }

    @FXML
    protected void addRow() {
        if (toDoItems != null) {
            toDoItems.add(0, new ToDoItem());
            editbtn.setSelected(true);
            editRow();
        }

    }

    @FXML
    protected void editRow() {
        if (editbtn.isSelected()) {
            description.setEditable(true);
        } else {
            description.setEditable(false);
        }
    }

    @FXML
    protected void deleteRow() {
        ToDoItem item = table.getSelectionModel().getSelectedItem();
        toDoItems.remove(item);

    }

    @FXML
    protected void addNewCat() {
        if (categories != null) {
            categories.add(0, new String());
        }
        fltrbtn.setSelected(false);
        FilterToggle();
    }

    @FXML
    protected void deleteCat() {
        String cat = cattab.getSelectionModel().getSelectedItem();
        categories.remove(cat);
        Settings.setCategories(categories);
        catfltr.setItems(Settings.getCategoryPropertiees());
        category.setCellFactory(ComboBoxTableCell.forTableColumn(Settings.getCategories()));
        fltrbtn.setSelected(false);
        FilterToggle();

    }

    @FXML
    protected void onTemplateDownload() {
        String exportpath = new String();
        if (exportpath.isEmpty()) {
            exportpath = onBrowseFolder();
        }
        if (!exportpath.isEmpty()) {
            FileAssist.downloadTemplate(exportpath);
            sendAlert("Template Downloaded successfully!", 'S');
        } else {
            sendAlert("Could not download template.", 'E');
        }

    }

    @FXML
    protected void onFilter(ActionEvent action) {
        // code to filter
        if (catfltr.getValue() == null && statfltr.getValue() == null) {
            toDoItems_fltr.setPredicate(toDoItem -> {
                return true;
            });
        } else if (catfltr.getValue() != null && statfltr.getValue() == null) {
            toDoItems_fltr.setPredicate(toDoItem -> {
                return toDoItem.getCategory().equals(catfltr.getValue());
            });
        } else if (statfltr.getValue() != null && catfltr.getValue() == null) {
            toDoItems_fltr.setPredicate(toDoItem -> {
                return toDoItem.getStatus().equals(statfltr.getValue());
            });
        } else if (statfltr.getValue() != null && catfltr.getValue() != null) {
            toDoItems_fltr.setPredicate(toDoItem -> {
                return toDoItem.getStatus().equals(statfltr.getValue()) && toDoItem.getCategory().equals(catfltr.getValue());
            });
        }


    }

    @FXML
    protected void FilterToggle() {
        if (fltrbtn.isSelected()) {
            catfltr.setDisable(false);
            statfltr.setDisable(false);
        } else {
            statfltr.getSelectionModel().clearSelection();
            catfltr.getSelectionModel().clearSelection();
            catfltr.setDisable(true);
            statfltr.setDisable(true);
        }
    }

    private void iteratePrioritizer() {
        if (i == (items - 2)) {
            choice1.setVisible(false);
            choice2.setVisible(false);
            i = 0;
            j = 0;
            choice1.setText("Choice 1");
            choice2.setText("Choice 2");
            sendAlert("Prioritization Completed successfully!", 'S');
            prioritize.setText("Start Prioritizer");
            return;
        }

        if (j < (items - 1)) {
            j++;
        } else if (j == (items - 1)) {
            i++;
            j = i + 1;
        }
        if (toDoItems.get(i).getStatus().toLowerCase().equals("completed")) {
            choice1.setVisible(false);
            choice2.setVisible(false);
            i = 0;
            j = 0;
            choice1.setText("Choice 1");
            choice2.setText("Choice 2");
            sendAlert("Prioritization Completed successfully!", 'S');
            prioritize.setText("Start Prioritizer");
        } else if (toDoItems.get(j).getStatus().toLowerCase().equals("completed")) {
            iteratePrioritizer();
        } else {
            choice1.setText(toDoItems.get(i).getDescription());
            choice2.setText(toDoItems.get(j).getDescription());
        }
    }

    private void changeAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), choice1);
        st.setByX(-1f);
        st.setByY(-1f);
        st.setCycleCount(2);
        st.setAutoReverse(true);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(150), choice2);
        st2.setByX(-1f);
        st2.setByY(-1f);
        st2.setCycleCount(2);
        st2.setAutoReverse(true);
        st.play();
        st2.play();
    }

    private void messageAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), notif_label);
        st.setByX(-1f);
        st.setCycleCount(2);
        st.setAutoReverse(true);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(150), notif_typ);
        st2.setByX(-1f);
        st2.setCycleCount(2);
        st2.setAutoReverse(true);
        st.play();
        st2.play();
    }

    private void sendAlert(String message, char type) {
        messageAnimation();
        switch (type) {
            case 'S':
                notif_label.setText(message);
                File file = new File("src/main/resources/success.png");
                notif_typ.setImage(new Image(file.toURI().toString()));
                tab_pane.getSelectionModel().select(home_tab);
                table.getSelectionModel().clearSelection();
                break;
            case 'I':
                notif_label.setText(message);
                file = new File("src/main/resources/info.png");
                notif_typ.setImage(new Image(file.toURI().toString()));
                tab_pane.getSelectionModel().select(home_tab);
                table.getSelectionModel().clearSelection();
                break;

            case 'W':
                notif_label.setText(message);
                file = new File("src/main/resources/warning.png");
                notif_typ.setImage(new Image(file.toURI().toString()));
                tab_pane.getSelectionModel().select(home_tab);
                table.getSelectionModel().clearSelection();
                break;

            case 'E':
                notif_label.setText(message);
                file = new File("src/main/resources/error.png");
                notif_typ.setImage(new Image(file.toURI().toString()));
                tab_pane.getSelectionModel().select(home_tab);
                table.getSelectionModel().clearSelection();
                break;
        }
    }

    private String onBrowseFolder() {
        String path = new String();
        DirectoryChooser browse = new DirectoryChooser();
        File file = browse.showDialog(theStage);
        if (file != null) {
            path = file.getAbsolutePath() + "\\template.xlsx";
            System.out.println(path);
        }
        return path;
    }

    private void enableAllButtons() {
        prioritize.setDisable(false);
        upbtn.setDisable(false);
        downbtn.setDisable(false);
        addbtn.setDisable(false);
        editbtn.setDisable(false);
        delbtn.setDisable(false);
        exportbtn.setDisable(false);
        fltrbtn.setDisable(false);

    }

    private void disableAllButtons() {
        prioritize.setDisable(true);
        upbtn.setDisable(true);
        downbtn.setDisable(true);
        addbtn.setDisable(true);
        editbtn.setDisable(true);
        delbtn.setDisable(true);
        exportbtn.setDisable(true);
        catfltr.setDisable(true);
        statfltr.setDisable(true);
        fltrbtn.setDisable(true);

    }
}
