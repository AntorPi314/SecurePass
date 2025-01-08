package securepass;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import java.io.IOException;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class PasswordDashboardController {

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
    @FXML
    private Label welcome;
    @FXML
    private TextField PField_;
    @FXML
    private Label password1;
    @FXML
    private Label copy;
    @FXML
    private ImageView copyUser;
    @FXML
    private ImageView showHideImage;
    @FXML
    private ImageView copyPass;

    public void setLoggedInPass(String pass) {
        logedInPass = pass;
        System.out.println("Logged in pass: " + pass);
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
        UField.setEditable(false);
        PField.setEditable(false);
        PField_.setEditable(false);
        NField.setEditable(false);
        searchField.setStyle("-fx-background-color:  #63676A; -fx-text-fill: #c1b7b7;");
        UField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        PField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        PField_.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        NField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #514c4c;");
        Preferences prefs = Preferences.userRoot().node("securepassApp");
        loggedInUser = prefs.get("loggedInUser", "defaultUser");
        System.out.println("Logged in username: " + loggedInUser);
        welcome.setText("Welcome, " + loggedInUser);

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

        listView1.setCellFactory(listView1 -> new CustomListCellForItemPass(this));

        listView1.getItems().addAll(
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Password", "John Doe"),
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Note", "Jane Smith"),
                new itemPass("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "PIN", "Alice Johnson")
        );
        listView1.getItems().forEach(item -> System.out.println("Added item: " + item.type));

        populateListViewWithUsernames2();
    }

    public void JustCall() {
        System.out.println("Hi! you just call me?");
    }

    @FXML
    private void copyUsername() {
        copyToClipboard(UField.getText());
    }

    @FXML
    private void copyPassword() {
        copyToClipboard(PField_.getText());
    }

    public void copyToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
        copy.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(event -> copy.setVisible(false));
        pause.play();
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
        stage.setTitle("Update Password");
        stage.show();

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

    private static class CustomListCellForItemPass extends ListCell<PasswordDashboardController.itemPass> {

        private HBox content;
        private ListController controller;
        private PasswordDashboardController controllerRef;

        public CustomListCellForItemPass(PasswordDashboardController controller) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
                content = loader.load();
                this.controller = loader.getController();
                this.controllerRef = controller;
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
                    setStyle("-fx-background-color: #0075F6;");
                    controller.typeLabel.setStyle("-fx-text-fill: #FFFFFF;");
                    controller.usernameLabel.setStyle("-fx-text-fill: #FFFFFF;");
                    System.out.println("Clicked item: " + item.id);
                    controllerRef.setSelectedData(item.id);
                });
                focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        setStyle("-fx-background-color: transparent;");
                        controller.typeLabel.setStyle("-fx-text-fill: #FFFFFF;");
                        controller.usernameLabel.setStyle("-fx-text-fill: #FFFFFF;");
                    }
                });
            }
        }
    }

    public void setSelectedData(String selectedItemID) {
        selectedID = selectedItemID;
        selectedIndex = listView1.getSelectionModel().getSelectedIndex();
        Record record = db.getRecordById(loggedInUser, Integer.parseInt(selectedItemID));
        title.setText(record.getType());
        UField.setText(record.getUsername());
        PField.setText(record.getPassword());
        PField_.setText(record.getPassword());
        NField.setText(record.getNote());
        lastMod.setText("Last Modified : " + record.getLastUpdatedTime());
        created.setText("Created : " + record.getCreatedTime());
    }

    @FXML
    private void showPassword() {
        if (PField.isVisible()) {
            PField.setVisible(false);
            Image image = new Image(getClass().getResource("/securepass/image/show.png").toExternalForm());
            showHideImage.setImage(image);
        } else {
            PField.setVisible(true);
            Image image = new Image(getClass().getResource("/securepass/image/hide.png").toExternalForm());
            showHideImage.setImage(image);
        }
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
    private void handleDelete2() {
        if(!showConfirmationDialog("You want to delete?")){
            return;
        }
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
        lastMod.setText("Last Modified : ");
        created.setText("Created : ");
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
