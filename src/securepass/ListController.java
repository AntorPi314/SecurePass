package securepass;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ListController {
    @FXML public ImageView imageView;
    @FXML public Label typeLabel;
    @FXML public Label usernameLabel;

    public void setData(String id, String type, String username) {
        if (type == "Facebook") {
            Image image = new Image(getClass().getResource("/securepass/image/antor.jpg").toExternalForm());
            imageView.setImage(image);
        } else {
            Image image = new Image(getClass().getResource("/securepass/image/antor.jpg").toExternalForm());
            imageView.setImage(image);
        }
        typeLabel.setText(type);
        usernameLabel.setText(username);
    }
}
