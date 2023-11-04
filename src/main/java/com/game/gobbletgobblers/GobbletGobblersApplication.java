package com.game.gobbletgobblers;

import com.game.gobbletgobblers.util.Popup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GobbletGobblersApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("start.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(
            Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm()
        );

        stage.setTitle("Gobblet Gobblers");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            if(!((GobbletGobblersController) fxmlLoader.getController()).getSavedProgress())
            {
                event.consume();

                Popup.exit();
                Popup.getAlert().showAndWait().ifPresent(response -> {
                    if(response == ButtonType.OK)
                        stage.close();
                });
            }
        });
    }

    public static void main(String[] args)
    {
        launch();
    }
}