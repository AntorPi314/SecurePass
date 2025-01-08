package securepass;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Facebook",
                "Twitter",
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
            System.out.println("Selected option: " + selectedOption);
        });
    }

    @FXML
    private void addPassword(ActionEvent event) {
        String btnText = button1.getText();
        if ("Save Password".equals(btnText)) {
            passwordDashboardController.handleAdd2(combobox1.getValue(), UField.getText(), PField.getText(), NField.getText());
        } else {
            passwordDashboardController.handleUpdate2(combobox1.getValue(), UField.getText(), PField.getText(), NField.getText());
        }
    }

    public void setInfo(String type, String username, String password, String note) {
        combobox1.setValue(type);
        updateImage(type);
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
            case "Twitter": {
                Image image = new Image(getClass().getResource("/securepass/image/x_48.png").toExternalForm());
                imageView.setImage(image);
                break;
            }
            default: {
                Image image = new Image(getClass().getResource("/securepass/image/antor.jpg").toExternalForm());
                imageView.setImage(image);
                break;
            }
        }
    }

    public void setController(PasswordDashboardController controller) {
        this.passwordDashboardController = controller;
    }
}
