package securepass;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PasswordDashboardController implements Initializable {

    @FXML
    private ListView<Item> listView;
    @FXML
    private TextField username;
    @FXML
    private Label password;
    @FXML
    private TextField note;
    @FXML
    private Label lastMod;
    @FXML
    private Label created;
    @FXML
    private Label title;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the custom ListCell for the ListView
        listView.setCellFactory(listView -> new CustomListCell());

        // Add sample data to the ListView
        listView.getItems().addAll(
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "John Doe", "john.doe@example.com"),
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Jane Smith", "jane.smith@example.com"),
                new Item("https://avatars.githubusercontent.com/u/123496530?s=48&v=4", "Alice Johnson", "alice.johnson@example.com")
        );

        // Debug: Print the added items to the console
        listView.getItems().forEach(item -> System.out.println("Added item: " + item.name));
    }

    // Item class to hold the data for each list item
    static class Item {
        String imagePath;
        String name;
        String email;

        public Item(String imagePath, String name, String email) {
            this.imagePath = imagePath;
            this.name = name;
            this.email = email;
        }
    }

    // Custom ListCell implementation
    private static class CustomListCell extends ListCell<Item> {

        private HBox content;
        private ListCellController controller;

        public CustomListCell() {
            try {
                // Load the FXML file for the list item layout
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                content = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace(); // Log the exception for debugging purposes
            }
        }

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null); // Remove the graphic if the item is empty or null
            } else {
                // Debug: Check if data is being set
                System.out.println("Updating item: " + item.name);

                // Set the data on the controller if it's not empty
                if (controller != null) {
                    controller.setData(item.imagePath, item.name, item.email);
                }
                setGraphic(content); // Set the content as the graphic
            }
        }
    }
}
