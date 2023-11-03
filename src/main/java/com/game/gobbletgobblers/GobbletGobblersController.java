package com.game.gobbletgobblers;

import com.game.gobbletgobblers.board.Board;
import com.game.gobbletgobblers.board.Color;
import com.game.gobbletgobblers.board.Piece;
import com.game.gobbletgobblers.board.Size;
import com.game.gobbletgobblers.util.PieceProxy;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GobbletGobblersController
{
    @FXML private Label turnLabel;
    @FXML private AnchorPane squaresContainer;
    @FXML private AnchorPane bluePiecesContainer;
    @FXML private AnchorPane orangePiecesContainer;
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

    private GobbletGobblersController controller;
    private Board board;
    private Piece selectedPiece;
    private final Piece[] bluePieces;
    private final Piece[] orangePieces;

    public GobbletGobblersController()
    {
        controller = this;
        board = new Board();
        selectedPiece = null;

        bluePieces = new Piece[3];
        for(int i=0; i<3; i++)
            bluePieces[i] = new Piece(Size.values()[i], Color.BLUE);

        orangePieces = new Piece[3];
        for(int i=0; i<3; i++)
            orangePieces[i] = new Piece(Size.values()[i], Color.ORANGE);
    }

    public void startNewGame(ActionEvent event)
    {
        try {
            switchScene(event, "game.fxml");
        } catch(IOException e) {
            // TODO: show a proper error message to the user
        }
    }

    public void loadGame(ActionEvent event)
    {
        try {
            startNewGame(event);
            controller.board = controller.board.deserialize();
            updateBoard();
            updateLabels();
        } catch(IOException | ClassNotFoundException e) {
            // TODO: show a proper error message to the user
        }
    }

    public void openSettings(ActionEvent event)
    {
        try {
            switchScene(event, "settings.fxml");
        } catch(IOException e) {
            // TODO: show a proper error message to the user
        }
    }

    public void exitGame(ActionEvent event)
    {
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.close();
    }

    public void returnToMenu(ActionEvent event)
    {
        try {
            switchScene(event, "start.fxml");
        } catch(IOException e) {
            // TODO: show a proper error message to the user
        }
    }

    public void saveGame()
    {
        try {
            controller.board.serialize();
        } catch(IOException e) {
            // TODO: show proper error message to user
        }
    }

    public void resetGame()
    {
        controller.board.reset();
        updateBoard();
        updateLabels();

        for(Piece bluePiece : controller.bluePieces)
            bluePiece.setCount(2);
        for(Piece orangePiece : controller.orangePieces)
            orangePiece.setCount(2);

        controller.squaresContainer.setDisable(false);
        controller.bluePiecesContainer.setDisable(false);
        controller.orangePiecesContainer.setDisable(false);
    }

    public void selectPiece(MouseEvent event)
    {
        Node target = (Node) event.getTarget();

        if(controller.selectedPiece != null)
            return;

        if((target.getId().contains("Blue") && controller.board.getTurn() == Color.ORANGE) ||
            (target.getId().contains("Orange") && controller.board.getTurn() == Color.BLUE))
            return;

        PieceProxy.setStartPosition(event.getSceneX(), event.getSceneY());
        if(target.equals(controller.smallBlueImage))
            selectPiece(controller.bluePieces, 0, controller.smallBlueCount);
        else if(target.equals(controller.mediumBlueImage))
            selectPiece(controller.bluePieces, 1, controller.mediumBlueCount);
        else if(target.equals(controller.largeBlueImage))
            selectPiece(controller.bluePieces, 2, controller.largeBlueCount);
        else if(target.equals(controller.smallOrangeImage))
            selectPiece(controller.orangePieces, 0, controller.smallOrangeCount);
        else if(target.equals(controller.mediumOrangeImage))
            selectPiece(controller.orangePieces, 1, controller.mediumOrangeCount);
        else if(target.equals(controller.largeOrangeImage))
            selectPiece(controller.orangePieces, 2, controller.largeOrangeCount);
    }

    public void handleSquareClick(MouseEvent event)
    {
        ObservableList<Node> squares = controller.squaresContainer.getChildren();

        int row = 0;
        int col = 0;
        for(int i=0; i<squares.size(); i++)
        {
            if(squares.get(i).equals(((Node) event.getTarget()).getParent()))
            {
                row = i / 3;
                col = i % 3;
            }
        }

        // user selects an existing piece to move
        if(controller.selectedPiece == null)
        {
            controller.selectedPiece = controller.board.movePiece(row, col);
            if(controller.selectedPiece != null)
            {
                PieceProxy.setStartPosition(event.getSceneX(), event.getSceneY());
                PieceProxy.createProxy(controller.selectedPiece.getImage(), controller.squaresContainer);
            }
            return;
        }

        // user places a newly selected piece
        try {
            controller.board.placePiece(row, col, controller.selectedPiece);
            controller.board.changeTurn();
            controller.selectedPiece = null;

            updateBoard();
            PieceProxy.hideProxy(controller.squaresContainer);

            if(controller.board.checkForWinner())
            {
                endGame();
                return;
            }

            String currentPlayer = controller.board.getTurn() == Color.BLUE ? "Blue" : "Orange";
            controller.turnLabel.setText("It's " + currentPlayer + "'s turn!");
        } catch(IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void switchScene(ActionEvent event, String filename) throws IOException
    {

        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(
            Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm()
        );

        stage.setScene(scene);
        stage.show();

        controller = fxmlLoader.getController();
        controller.board.reset();
    }

    private void selectPiece(Piece[] array, int index, Label countLabel)
    {
        if(array[index].getCount() == 0)
            return;

        controller.selectedPiece = array[index];
        controller.selectedPiece.setCount(controller.selectedPiece.getCount() - 1);
        countLabel.setText(Integer.toString(controller.selectedPiece.getCount()));

        PieceProxy.createProxy(controller.selectedPiece.getImage(), controller.squaresContainer);
    }

    private void updateBoard()
    {
        ObservableList<Node> squares = controller.squaresContainer.getChildren();

        for(int i=0; i<squares.size(); i++)
        {
            int row = i / 3;
            int col = i % 3;

            Pane square = (Pane) squares.get(i);
            ImageView imageView = (ImageView) square.getChildren().get(0);
            imageView.setImage(null);

            if(controller.board.getBoard()[row][col].isEmpty())
                continue;

            Piece topPiece = controller.board.getBoard()[row][col].peek();
            square.getChildren().set(0, topPiece.getImage());
        }
    }

    private void updateLabels()
    {
        Label[][] countLabels = new Label[][]{
            { controller.smallBlueCount, controller.mediumBlueCount, controller.largeBlueCount },
            { controller.smallOrangeCount, controller.mediumOrangeCount, controller.largeOrangeCount }
        };

        controller.smallBlueCount.setText("2"); controller.mediumBlueCount.setText("2"); controller.largeBlueCount.setText("2");
        controller.smallOrangeCount.setText("2"); controller.mediumOrangeCount.setText("2"); controller.largeOrangeCount.setText("2");

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(controller.board.getBoard()[i][j].isEmpty())
                    continue;

                for(Piece piece : new ArrayList<>(controller.board.getBoard()[i][j]))
                {
                    int colorIndex = piece.getColor().ordinal();
                    int sizeIndex = piece.getSize().ordinal();
                    countLabels[colorIndex][sizeIndex].setText(Integer.toString(piece.getCount()));

                    if(colorIndex == 0)
                        controller.bluePieces[sizeIndex].setCount(
                            controller.bluePieces[sizeIndex].getCount() - 1
                        );
                    else
                        controller.orangePieces[sizeIndex].setCount(
                            controller.orangePieces[sizeIndex].getCount() - 1
                        );
                }
            }
        }

        String currentPlayer = controller.board.getTurn() == Color.BLUE ? "Blue" : "Orange";
        controller.turnLabel.setText("It's " + currentPlayer + "'s turn!");
    }

    private void endGame()
    {
        controller.turnLabel.setText("Game over! Winner: " + controller.board.getWinner());
        controller.squaresContainer.setDisable(true);
        controller.bluePiecesContainer.setDisable(true);
        controller.orangePiecesContainer.setDisable(true);
    }
}