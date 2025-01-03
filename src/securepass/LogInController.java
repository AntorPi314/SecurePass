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

public class LogInController {

    @FXML
    private Label tc;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label lblError;
    
    private Database db;

    public void initialize() {
        db = new Database();
        db.connect();
        db.createUserTable();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username and Password cannot be empty.");
            return;
        }
        if (db.validateUser(username, password)) {
            System.out.println("Login successful!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PasswordDashboard.fxml"));
                Parent dashboardRoot = loader.load();
                //PasswordDashboardController dashboardController = loader.getController();
                //dashboardController.setLoggedInUser(username);
                Scene dashboardScene = new Scene(dashboardRoot);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Unable to load the dashboard. Please try again.");
            }
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
}
