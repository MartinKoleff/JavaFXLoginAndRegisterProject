package sample;

import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.Main.loginStage;
import static sample.LoginController.registerStage;
//import static sample.LoginController.incorrectLogin;

public class RegisterController {
    /**
     * Fields
     */
    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView resultImage;
    @FXML
    private ImageView mainImage;

    /**
     * Actions
     */

    /**
     * Register button
     */
    public void register() {
        Image image;
        Map<String, String> accounts = DBConnector.getMap();

        //If account already exists with the same password
        if (accounts.containsKey(username.getText())) {
            if (accounts.get(username.getText()).equals(password.getText())) {
                System.out.println("Account with that username and password already exists! Please try again with a different account!");
                image = new Image("/pictures/meme4.jpg", mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);

                resultImage.setImage(image);
                mainImage.setVisible(false);
                resultImage.setVisible(true);
            }
        } else if (isValidUsername(username) && isValidPassword(password)) {//valid account
            System.out.println("Successfully registered!");
            image = new Image("/pictures/meme1.jpg", mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);

            resultImage.setImage(image);
            mainImage.setVisible(false);
            resultImage.setVisible(true);

            DBConnector.registerUser(first_name.getText(), last_name.getText(), username.getText(), password.getText());

            //Delays the program closing for 3 seconds to see the success image!
            delayProgram(3);


            loginStage.show();
        } else {
            System.out.println("Invalid account!");
            image = new Image("/pictures/meme3.jpg", mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);

            resultImage.setImage(image);
            mainImage.setVisible(false);
            resultImage.setVisible(true);
        }
    }

    private void delayProgram(int seconds) {
        Thread thread = new Thread();
        try {
            thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancel button
     */
    public void cancel() {
        loginStage.show();

        //incorrectLogin.setVisibility(false);
        registerStage.close();
    }

    /**
     * Valid username:
     * - 7+ length
     * - has at least 1 upperCase
     */
    private boolean isValidUsername(TextField username) {
        String usernameString = username.getText();

        Pattern pattern = Pattern.compile("[A-Z]+");
        Matcher matcher = pattern.matcher(usernameString);
        if (usernameString.length() > 7 && matcher.find()) {
            System.out.println("Valid username.");
            return true;
        }
        return false;
    }

    /**
     * Valid password:
     * - 7+ length
     * - has at least 1 digit
     * - has at least 1 upperCase
     */
    private boolean isValidPassword(PasswordField password) {
        String passwordString = password.getText();

        Pattern pattern = Pattern.compile("[0-9]+");
        Pattern pattern2 = Pattern.compile("[A-Z]+");
        Matcher matcher = pattern.matcher(passwordString);
        Matcher matcher2 = pattern2.matcher(passwordString);

        if (passwordString.length() > 7 && matcher.find() && matcher2.find()) {
            System.out.println("Valid password.");
            return true;
        }
        return false;
    }
}

