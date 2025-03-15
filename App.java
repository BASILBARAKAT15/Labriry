package com.mycompany.GLibraryProject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage mainStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LogIn"));
        stage.setScene(scene);
        stage.show();
        
        App.mainStage = stage;
    }
    
    static void setRoot(String fxml) throws IOException {
        if(fxml.equals("LogIn")) {
            double sceneWidth = 623;
            double sceneHeight = 438;
            
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            
            double x = (screenWidth - sceneWidth) / 2;
            double y = (screenHeight - sceneHeight) / 2;
            
            mainStage.setWidth(sceneWidth);
            mainStage.setHeight(sceneHeight);
            mainStage.setX(x);
            mainStage.setY(y);
        }
        
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        return root;
    }
    
    public static void main(String[] args) {
        launch();
    }
}