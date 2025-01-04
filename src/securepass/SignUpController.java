package securepass;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private Label tc;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    private Database db;

    public void initialize() {
        db = new Database();
        db.connect();
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        if (db.addUser(username, password, email)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup Successful");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully!");
            alert.showAndWait();
            db.createRecordsTable(username);
            if (!db.isTableExist("Info")) {
                db.createInfoTable();
            }
            switchToLogin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Signup Failed");
            alert.setHeaderText(null);
            alert.setContentText("Username already exists!");
            alert.showAndWait();
        }
    }

    @FXML
    private void switchToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
