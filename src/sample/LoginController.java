package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static sample.Main.loginStage;

public class LoginController implements Initializable {
    /**
     * Fields
     */
    @FXML
    private Text incorrectLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Hyperlink hyperlink;

    public static Text incorrectLoginClone;
    public static Stage registerStage;
    /**
     * Actions
     */


    /**
     * Opening the Register window
     */
    @FXML
    private void openRegisterWindow(ActionEvent actionEvent) {
        if (hyperlink.isVisited()) {
            createRegisterWindow();
        }
    }

    /**
     * Creating the Register window (and closing the login window)
     */
    private void createRegisterWindow() {
        Parent registerRoot = null;
        try {
            registerRoot = FXMLLoader.load(getClass().getResource("registerWindowV2.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        registerStage = new Stage();
        registerStage.setTitle("Register Window");
        registerStage.getIcons().add(new Image("/pictures/cat.jpg"));
        registerStage.setScene(new Scene(registerRoot, 400, 600));
        registerStage.show();
        registerStage.setResizable(false);
        loginStage.close();
    }

    /**
     * Login Button
     */
    @FXML
    private void login() {
        if (checkIfAccountExists(username.getText(), password.getText())) {
            System.out.println("Successfully logged in!");
            loginStage.close();
            //open the main program...
        } else {
            incorrectLoginClone = incorrectLogin;
            incorrectLoginClone.setVisible(true);
            System.out.println("Incorrect account. Try again.");
        }
    }

    /**
     * Checks if account exists in DB
     */
    private boolean checkIfAccountExists(String username, String password) {
        Map<String, String> accounts = DBConnector.getMap();

        if (accounts.containsKey(username)) {
            if (accounts.get(username).equals(password)) {
                return true;
            }
        }
        return false;
    }

    //Unused
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

