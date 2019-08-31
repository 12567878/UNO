package uno;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage stage;  //注意static
    @FXML
    Button single_button;
    @FXML
    void game_start(MouseEvent event) throws Exception{
        try {
            Parent root2 = FXMLLoader.load(getClass().getResource("view/gaming.fxml"));
            assert stage!=null;
            stage.setScene(new Scene(root2, 1080, 810));
            //stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("view/entry.fxml"));
        primaryStage.setTitle("UNO");
        primaryStage.setScene(new Scene(root, 1080, 810));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

}
