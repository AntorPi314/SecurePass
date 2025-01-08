package securepass;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class PasswordDashboardController {

    @FXML
    private ListView<Item> listView;
    @FXML
    private Label password;
    @FXML
    private Label lastMod;
    @FXML
    private Label created;
    @FXML
    private Label title;
    @FXML
    private TableView<Record> tableView;

    @FXML
    private TableColumn<Record, Integer> colId;

    @FXML
    private TableColumn<Record, String> colType;

    @FXML
    private TableColumn<Record, String> colUsername;

    @FXML
    private TableColumn<Record, String> colPassword;

    @FXML
    private TableColumn<Record, String> colNote;

    @FXML
    private TableColumn<Record, String> colLastUpdatedTime;

    @FXML
    private TableColumn<Record, String> colCreatedTime;

    @FXML
    private TextField searchField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextArea noteField;

    private Database db;
    private ObservableList<Record> records;

    public String loggedInUser = "a";
    private String logedInPass;
    public String selectedID;
    public int selectedIndex;
    private Stage primaryStage;
    @FXML
    private ListView<itemPass> listView1;
    @FXML
    private TextField UField;
    @FXML
    private TextField PField;
    @FXML
    private TextArea NField;

    public void setLoggedInPass(String pass) {
        logedInPass = pass;
        System.out.println("Logged in pass: " + pass);
    }

    private void populateListViewWithUsernames() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (Record record : records) {
            items.add(new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", record.getUsername(), ""));
        }
        listView.setItems(items);
    }

    private void populateListViewWithUsernames2() {
        ObservableList<itemPass> items = FXCollections.observableArrayList();
        for (Record record : records) {
            String recordId = Integer.toString(record.getId());
            items.add(new itemPass(recordId, record.getType(), record.getUsername()));
        }
        listView1.setItems(items);
    }

    public void initialize() {
        Preferences prefs = Preferences.userRoot().node("securepassApp");
        loggedInUser = prefs.get("loggedInUser", "defaultUser");
        System.out.println("Logged in username: " + loggedInUser);

        db = new Database();
        db.connect();
        // db.createRecordsTable(loggedInUser);
        records = FXCollections.observableArrayList(db.getAllRecordsForUser(loggedInUser));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colLastUpdatedTime.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedTime"));
        colCreatedTime.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        tableView.setItems(records);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        listView.setCellFactory(listView -> new CustomListCell());
        listView.getItems().addAll(
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "John Doe", "john.doe@example.com"),
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Jane Smith", "jane.smith@example.com"),
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Alice Johnson", "alice.johnson@example.com")
        );
        listView.getItems().forEach(item -> System.out.println("Added item: " + item.name));

        listView1.setCellFactory(listView -> new CustomListCellForItemPass(this)); // Pass db here

        listView1.getItems().addAll(
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Password", "John Doe"),
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Note", "Jane Smith"),
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "PIN", "Alice Johnson")
        );
        listView1.getItems().forEach(item -> System.out.println("Added item: " + item.type));

        populateListViewWithUsernames();

        populateListViewWithUsernames2();

    }

    public void JustCall() {
        System.out.println("Hi! you just call me?)");
    }

    @FXML
    private void AddBtn1(ActionEvent event) throws IOException {
        System.out.println("SelectedID: " + selectedID);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPassword.fxml"));
        Parent root = loader.load();
        AddPasswordController controller = loader.getController();
        controller.setController(this);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add New Password");
        stage.show();
    }

    @FXML
    private void UpdateBtn1(ActionEvent event) throws IOException {
        if (selectedID == null) {
            return;
        }
        System.out.println("SelectedID: " + selectedID);
        
        System.out.println("SelectedListItem: " + selectedIndex);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPassword.fxml"));
        Parent root = loader.load();
        AddPasswordController controller = loader.getController();
        controller.setController(this);
        controller.setInfo(title.getText(), UField.getText(), PField.getText(), NField.getText());

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add New Password");
        stage.show();

    }

    static class Item {

        String imagePath;
        String name;
        String email;

        public Item(String imagePath, String name, String email) {
            this.imagePath = imagePath;
            this.name = name;
            this.email = email;
        }
    }

    static class itemPass {

        String id;
        String type;
        String username;

        public itemPass(String id, String type, String username) {
            this.id = id;
            this.type = type;
            this.username = username;
        }
    }

    private static class CustomListCell extends ListCell<Item> {

        private HBox content;
        private ListCellController controller;

        public CustomListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                content = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                System.out.println("Updating item: " + item.name);
                if (controller != null) {
                    controller.setData(item.imagePath, item.name, item.email);
                }
                setGraphic(content);
            }
        }
    }

    private static class CustomListCellForItemPass extends ListCell<PasswordDashboardController.itemPass> {

        private HBox content;
        private ListController controller;
        private PasswordDashboardController controllerRef;

        public CustomListCellForItemPass(PasswordDashboardController controller) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
                content = loader.load();
                this.controller = loader.getController();
                this.controllerRef = controller;  // Save the reference to the main controller
            } catch (IOException e) {
                e.printStackTrace();
            }
            setStyle("-fx-background-color: transparent; -fx-text-fill: #000000;");
        }

        @Override
        protected void updateItem(PasswordDashboardController.itemPass item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                if (controller != null) {
                    controller.setData(item.id, item.type, item.username);
                }
                setGraphic(content);
                content.setOnMouseClicked(event -> {
                    setStyle("-fx-background-color: #000000;");
                    controller.typeLabel.setStyle("-fx-text-fill: #FFFFFF;");
                    controller.usernameLabel.setStyle("-fx-text-fill: #FFFFFF;");
                    System.out.println("Clicked item: " + item.id);
                    controllerRef.setSelectedData(item.id);
                });
                focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        setStyle("-fx-background-color: transparent;");
                        controller.typeLabel.setStyle("-fx-text-fill: #000000;");
                        controller.usernameLabel.setStyle("-fx-text-fill: #000000;");
                    }
                });
            }
        }
    }

    public void setSelectedData(String selectedItemID) {
        selectedID = selectedItemID;
        selectedIndex = listView1.getSelectionModel().getSelectedIndex();
        Record record = db.getRecordById(loggedInUser, Integer.parseInt(selectedItemID));
        usernameField.setText(record.getNote());
        title.setText(record.getType());
        UField.setText(record.getUsername());
        PField.setText(record.getPassword());
        NField.setText(record.getNote());
        lastMod.setText(record.getLastUpdatedTime());
        created.setText(record.getCreatedTime());
    }

    private boolean showConfirmationDialog(String Msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(Msg);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    @FXML
    private void handleAdd() {
        String type = typeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String note = noteField.getText();
        if (type.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Type, Username, and Password fields cannot be empty.");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String createdTime = formatTime(now);
        String lastUpdatedTime = createdTime;
        int id = db.addRecord(loggedInUser, type, username, password, note, createdTime, lastUpdatedTime);
        if (id != -1) {
            records.add(new Record(id, type, username, password, note, lastUpdatedTime, createdTime));
            clearFields();
        } else {
            showAlert("Error", "Failed to add the record.");
        }
    }

    public void handleAdd2(String type, String username, String password, String note) {
        System.out.println("Type: " + type);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Note: " + note);
        if (type.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Type, Username, and Password fields cannot be empty.");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String createdTime = formatTime(now);
        String lastUpdatedTime = createdTime;
        int id = db.addRecord(loggedInUser, type, username, password, note, createdTime, lastUpdatedTime);
        if (id != -1) {
            records.add(new Record(id, type, username, password, note, lastUpdatedTime, createdTime));
        } else {
            showAlert("Error", "Failed to add the record.");
        }
        handleSearch();
    }

    @FXML
    private void handleUpdate() {
        if (showConfirmationDialog("Do you want to Update?")) {
            return;
        }
        Record selectedRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            String type = typeField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String note = noteField.getText();
            if (type.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Type, Username, and Password fields cannot be empty.");
                return;
            }
            String lastUpdatedTime = formatTime(LocalDateTime.now());
            if (db.updateRecord(loggedInUser, selectedRecord.getId(), type, username, password, note, lastUpdatedTime)) {
                selectedRecord.setType(type);
                selectedRecord.setUsername(username);
                selectedRecord.setPassword(password);
                selectedRecord.setNote(note);
                selectedRecord.setLastUpdatedTime(lastUpdatedTime);
                tableView.refresh();
                clearFields();
            } else {
                showAlert("Error", "Failed to update the record.");
            }
        } else {
            showAlert("Warning", "Please select a record to update.");
        }
    }

    public void handleUpdate2(String type, String username, String password, String note) {
        if (selectedID != null) {
            if (type.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Type, Username, and Password fields cannot be empty.");
                return;
            }
            String lastUpdatedTime = formatTime(LocalDateTime.now());
            if (db.updateRecord(loggedInUser, Integer.parseInt(selectedID), type, username, password, note, lastUpdatedTime)) {
                tableView.refresh();
                clearFields();
            } else {
                showAlert("Error", "Failed to update the record.");
            }
        } else {
            showAlert("Warning", "Please select a record to update.");
        }
        handleSearch();
    }

    @FXML
    private void handleDelete() {
        Record selectedRecord = tableView.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            if (db.deleteRecord(loggedInUser, selectedRecord.getId())) {
                records.remove(selectedRecord);
                clearFields();
            } else {
                showAlert("Error", "Failed to delete the record.");
            }
        } else {
            showAlert("Warning", "Please select a record to delete.");
        }
    }

    @FXML
    private void handleDelete2() {
        if (selectedID != null) {
            if (db.deleteRecord(loggedInUser, Integer.parseInt(selectedID))) {
                clearFields();
            } else {
                showAlert("Error", "Failed to delete the record.");
            }
        } else {
            showAlert("Warning", "Please select a record to delete.");
        }
        handleSearch();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            records.setAll(db.getAllRecordsForUser(loggedInUser));
        } else {
            records.setAll(db.searchRecordsForUser(loggedInUser, query));
        }
        populateListViewWithUsernames2();
    }

    private void clearFields() {
        title.setText("Nothing");
        UField.clear();
        PField.clear();
        NField.clear();
    }

    private String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a"));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
