package sample;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.LoginController.*;
import static sample.Main.loginStage;
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
    @FXML
    private ToggleButton adminToggle;
    @FXML
    private Button registerButton;

    /**
     * Actions
     */

    /**
     * Register button
     */
    public void register() {
        Image image;
        String imagePath;
        Map<String, String> accounts = DBConnector.getMap();

        //If account already exists with the same password
        if (accounts.containsKey(username.getText())) {
            if (accounts.get(username.getText()).equals(password.getText())) {
                System.out.println("Account with that username and password already exists! Please try again with a different account!");

                imagePath = "/pictures/meme4.jpg";
                image = new Image(imagePath, mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);
                resultImage.setImage(image);
                mainImage.setVisible(false);
                resultImage.setVisible(true);
            }
        } else if (isValidUsername(username) && isValidPassword(password)) {//valid account
            System.out.println("Successfully registered!");

            imagePath = "/pictures/meme1.jpg";
            image = new Image(imagePath, mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);
            resultImage.setImage(image);
            resultImage.setVisible(true);
            mainImage.setVisible(false);

            DBConnector.registerUser(adminToggle.isSelected(), first_name.getText(), last_name.getText(), username.getText(), password.getText());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            loginStage.show();
                            registerStage.close();
                        }
                    });
                }
            });

            //Delays the program!
            thread.start();

        } else {
            System.out.println("Invalid account!");

            imagePath = "/pictures/meme3.jpg";
            image = new Image(imagePath, mainImage.getFitWidth(), mainImage.getFitHeight(), false, true);
            resultImage.setImage(image);
            mainImage.setVisible(false);
            resultImage.setVisible(true);
        }
    }

    /**
     * Cancel button
     */
    public void cancel() {
        loginStage.show();

        //Hide incorrect login text if it exists. Otherwise it is not visible!
        if (incorrectLoginClone != null) {
            incorrectLoginClone.setVisible(false);
        }
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

//            mainImage.setImage(resultImage.getImage());
//            mainImage.setImage(null);

//            PauseTransition pause = new PauseTransition(Duration.INDEFINITE);
//            pause.setOnFinished(
//                    e -> {
//                        resultImage.setVisible(true);
//                        mainImage.setVisible(false);
//                        pause.playFromStart(); // loop again
//                    });
//            pause.play();
//        for(Instant i = start; i.compareTo(end); i.plusSeconds(1)){
//
//        }
//        time.atOffset(ZoneOffset.ofTotalSeconds(5));

//            resultImage = new ImageView();

//            mainImage = new ImageView();
//            mainImage.setImage(image);
//            mainImage.setVisible(true);

