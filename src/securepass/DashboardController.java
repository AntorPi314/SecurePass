package securepass;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.Initializable;

public class DashboardController implements Initializable {

    public String loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Preferences prefs = Preferences.userRoot().node("securepassApp");
        loggedInUser = prefs.get("loggedInUser", "defaultUser");
        System.out.println("Logged in user name: " + loggedInUser);
    }

    public void setLoggedInUser(String value) {
        loggedInUser = value;
    }

}
