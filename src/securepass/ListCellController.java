package securepass;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ListCellController {

    @FXML private ImageView imageView;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;

    // Method to set the data in the list cell
    public void setData(String imagePath, String name, String email) {
        // Load the image from the URL
        imageView.setImage(new Image(imagePath));
        
        // Set the text for the labels
        nameLabel.setText(name);
        emailLabel.setText(email);
    }
}
