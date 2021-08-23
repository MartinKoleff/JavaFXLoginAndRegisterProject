package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {

    public static Stage loginStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent loginRoot = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
        loginStage = primaryStage;
        loginStage.setTitle("Login Window");
        loginStage.getIcons().add(new Image("/pictures/cat.jpg"));
        loginStage.setScene(new Scene(loginRoot, 600, 400));
        loginStage.setResizable(false);
        loginStage.show();
    }


    public static void main(String[] args) {
        DBConnector dbc = new DBConnector("jdbc:mysql://192.168.0.116:3306",
                "martin",
                "password");

        dbc.createDB();

        launch(args);
    }
}
