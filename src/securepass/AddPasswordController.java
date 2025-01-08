package securepass;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddPasswordController implements Initializable {

    @FXML
    private TextField UField;
    @FXML
    private TextField PField;
    @FXML
    private TextArea NField;
    @FXML
    private ComboBox<String> combobox1;

    private PasswordDashboardController passwordDashboardController;
    @FXML
    private ImageView imageView;
    @FXML
    private Button button1;
    @FXML
    private TextField TField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combobox1.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        TField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        UField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        PField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #efe9e9; -fx-font-weight: bold;");
        NField.setStyle("-fx-background-color: #1D1D1D; -fx-text-fill: #514c4c;");
        ObservableList<String> options = FXCollections.observableArrayList(
                "Google",
                "Facebook",
                "Twitter (X)",
                "Instagram",
                "Tiktok",
                "Snapchat",
                "Discord",
                "Other"
        );
        combobox1.setItems(options);
        combobox1.setValue("Other");
        combobox1.setOnAction(event -> {
            String selectedOption = combobox1.getValue();
            if (null == selectedOption) {
                Image image = new Image(getClass().getResource("/securepass/image/antor.jpg").toExternalForm());
                imageView.setImage(image);
            } else {
                updateImage(selectedOption);
            }
            TField.setText(selectedOption);
            System.out.println("Selected option: " + selectedOption);
        });
    }

    @FXML
    private void addPassword(ActionEvent event) {
        String btnText = button1.getText();
        if ("Save Password".equals(btnText)) {
            passwordDashboardController.handleAdd2(TField.getText(), UField.getText(), PField.getText(), NField.getText());
        } else {
            passwordDashboardController.handleUpdate2(TField.getText(), UField.getText(), PField.getText(), NField.getText());
        }
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setInfo(String type, String username, String password, String note) {
        combobox1.setValue(type);
        updateImage(type);
        TField.setText(type);
        UField.setText(username);
        PField.setText(password);
        NField.setText(note);
        button1.setText("Update Password");
    }

    private void updateImage(String selectedOption) {
        switch (selectedOption) {
            case "Facebook": {
                Image image = new Image(getClass().getResource("/securepass/image/fb_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Twitter (X)": {
                Image image = new Image(getClass().getResource("/securepass/image/x_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Google": {
                Image image = new Image(getClass().getResource("/securepass/image/google_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Instagram": {
                Image image = new Image(getClass().getResource("/securepass/image/insta_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Tiktok": {
                Image image = new Image(getClass().getResource("/securepass/image/tiktok_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Snapchat": {
                Image image = new Image(getClass().getResource("/securepass/image/snapchat_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            case "Discord": {
                Image image = new Image(getClass().getResource("/securepass/image/discord_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            default: {
                Image image = new Image(getClass().getResource("/securepass/image/other_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
        }
    }

    public void setController(PasswordDashboardController controller) {
        this.passwordDashboardController = controller;
    }
}
