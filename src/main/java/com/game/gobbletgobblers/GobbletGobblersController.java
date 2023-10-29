package com.game.gobbletgobblers;

import com.game.gobbletgobblers.board.Board;
import com.game.gobbletgobblers.board.Piece;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GobbletGobblersController
{
    private Board board;
    private Piece selectedPiece;

    public void startNewGame(ActionEvent event) throws IOException
    {
        // setting up game board
        board = new Board();
        selectedPiece = null;

        // changing scene
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }
}