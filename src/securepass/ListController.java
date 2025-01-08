package securepass;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ListController {

    @FXML
    public ImageView imageView;
    @FXML
    public Label typeLabel;
    @FXML
    public Label usernameLabel;

    public void setData(String id, String type, String username) {
        setImage(type);
        typeLabel.setText(type);
        usernameLabel.setText(username);
    }

    public void setImage(String type) {
        if (null == type) {
            Image image = new Image(getClass().getResource("/securepass/image/other_48.png").toExternalForm());
            imageView.setImage(image);
        } else {
            switch (type) {
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
                    Image image = new Image(getClass().getResource("/securepass/image/other_48.png").toExternalForm());
                    imageView.setImage(image);
                    break;
                }
            }
        }
    }
}
