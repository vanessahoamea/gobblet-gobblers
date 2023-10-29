package com.game.gobbletgobblers;

import com.game.gobbletgobblers.board.Board;
import com.game.gobbletgobblers.board.Color;
import com.game.gobbletgobblers.board.Piece;
import com.game.gobbletgobblers.board.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GobbletGobblersController
{
    @FXML private Label turnLabel;
    @FXML private ImageView largeBlueImage;
    @FXML private Label largeBlueCount;
    @FXML private ImageView mediumBlueImage;
    @FXML private Label mediumBlueCount;
    @FXML private ImageView smallBlueImage;
    @FXML private Label smallBlueCount;
    @FXML private ImageView largeOrangeImage;
    @FXML private Label largeOrangeCount;
    @FXML private ImageView mediumOrangeImage;
    @FXML private Label mediumOrangeCount;
    @FXML private ImageView smallOrangeImage;
    @FXML private Label smallOrangeCount;

    private final Board board;
    private Piece selectedPiece;
    private final Piece[] bluePieces;
    private final Piece[] orangePieces;

    public GobbletGobblersController()
    {
        board = new Board();
        selectedPiece = null;

        bluePieces = new Piece[3];
        for(int i=0; i<3; i++)
            bluePieces[i] = new Piece(Size.values()[i], Color.BLUE);

        orangePieces = new Piece[3];
        for(int i=0; i<3; i++)
            orangePieces[i] = new Piece(Size.values()[i], Color.ORANGE);
    }

    public void startNewGame(ActionEvent event) throws IOException
    {
        board.reset();

        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void exitGame(ActionEvent event)
    {
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.close();
    }

    public void selectPiece(MouseEvent event)
    {
        Node target = (Node) event.getTarget();

        if(selectedPiece != null)
            return;

        if(target.getId().contains("Blue") && board.getTurn() == Color.ORANGE)
            return;
        if(target.getId().contains("Orange") && board.getTurn() == Color.BLUE)
            return;

        if(target.equals(smallBlueImage))
            selectPiece(bluePieces, 0, smallBlueCount);
        else if(target.equals(mediumBlueImage))
            selectPiece(bluePieces, 1, mediumBlueCount);
        else if(target.equals(largeBlueImage))
            selectPiece(bluePieces, 2, largeBlueCount);
        else if(target.equals(smallOrangeImage))
            selectPiece(orangePieces, 0, smallOrangeCount);
        else if(target.equals(mediumOrangeImage))
            selectPiece(orangePieces, 1, mediumOrangeCount);
        else if(target.equals(largeOrangeImage))
            selectPiece(orangePieces, 2, largeOrangeCount);
    }

    public void handleSquareClick(MouseEvent event)
    {
        Pane target = (Pane) ((Node) event.getTarget()).getParent();
        String[] classNames = target.getStyleClass().get(0).split(" ");

        int row = 0;
        if(classNames[0].equals("second-row"))
            row = 1;
        if(classNames[0].equals("third-row"))
            row = 2;

        int col = 0;
        if(classNames[1].equals("second-col"))
            col = 1;
        if(classNames[1].equals("third-col"))
            col = 2;

        // user selects an existing piece to move
        if(selectedPiece == null)
        {
            selectedPiece = board.movePiece(row, col);
            return;
        }

        // user places a newly selected piece
        try {
            board.placePiece(row, col, selectedPiece);
            board.changeTurn();

            target.getChildren().removeAll();
            target.getChildren().add(selectedPiece.getImage());
            selectedPiece = null;

            if(board.checkForWinner())
            {
                endGame();
                return;
            }

            String currentPlayer = board.getTurn() == Color.BLUE ? "Blue" : "Orange";
            turnLabel.setText("It's " + currentPlayer + "'s turn");
        } catch(IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectPiece(Piece[] array, int index, Label count)
    {
        int integerCount = Integer.parseInt(count.getText());

        if(integerCount == 0)
            return;

        selectedPiece = array[index];
        count.setText(Integer.toString(--integerCount));
    }

    private void endGame()
    {
        System.out.println("Game over! Winner: " + board.getWinner());
    }
}