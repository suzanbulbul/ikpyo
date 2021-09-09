package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;
    public static Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("arayuzler/anasayfa.fxml"));
        Main.primaryStage.setTitle("Anasayfa");
        Main.primaryStage.setScene(new Scene(root, 1024, 768));
        Main.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void setTitle(String title){
        Main.primaryStage.setTitle(title);
    }

    public void goToPage(String page) throws Exception{
        root = FXMLLoader.load(getClass().getResource(page));
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}
