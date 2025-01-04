package securepass;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ListController {
    @FXML private ImageView imageView;
    @FXML private Label typeLabel;
    @FXML private Label usernameLabel;

    public void setData(String imagePath, String type, String username) {
        imageView.setImage(new Image(imagePath));
        typeLabel.setText(type);
        usernameLabel.setText(username);
    }
}
