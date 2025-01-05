package securepass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.util.prefs.Preferences;
import javafx.application.Platform;

public class LogInController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label lblError;

    private Database db;

    public void initialize(URL url, ResourceBundle rb) {
        Preferences prefs = Preferences.userRoot().node("securepassApp");
        usernameField.setText(prefs.get("loggedInUser", ""));
        db = new Database();
        db.connect();
        db.createUserTable();

        passwordField.setOnAction(event -> {
            try {
                handleLogin(new ActionEvent(passwordField, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> passwordField.requestFocus());
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username and Password cannot be empty.");
            return;
        }
        if (db.validateUser(username, password)) {
            Preferences prefs = Preferences.userRoot().node("securepassApp");
            prefs.put("loggedInUser", username);
            System.out.println("Save in prefs: " + username);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("PasswordDashboard.fxml"));
            Parent root = loader.load();

            //PasswordDashboardController dashboardController = loader.getController();
            //dashboardController.setLoggedInUser(username);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Password Dashboard");
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            System.out.println("Invalid login credentials.");
            lblError.setText("Invalid username or password.");
        }
    }

    @FXML
    private void switchToSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent signupRoot = loader.load();
            Scene signupScene = new Scene(signupRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(signupScene);
            stage.setTitle("Signup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the signup page. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void PrivacyPolicy(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PrivacyPolicy.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Privacy Policy");
        stage.show();
    }
}
