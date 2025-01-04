package securepass;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ListCellController {
    @FXML private ImageView imageView;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;

    public void setData(String imagePath, String name, String email) {
        imageView.setImage(new Image(imagePath));
        nameLabel.setText(name);
        emailLabel.setText(email);
    }
}
