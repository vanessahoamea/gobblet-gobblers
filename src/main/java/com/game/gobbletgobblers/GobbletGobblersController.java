package com.game.gobbletgobblers;

import com.game.gobbletgobblers.board.Board;
import com.game.gobbletgobblers.board.Color;
import com.game.gobbletgobblers.board.Piece;
import com.game.gobbletgobblers.board.Size;
import com.game.gobbletgobblers.util.PieceProxy;
import com.game.gobbletgobblers.util.Popup;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
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
    @FXML private AnchorPane squaresContainer;
    @FXML private AnchorPane bluePiecesContainer;
    @FXML private AnchorPane orangePiecesContainer;
    @FXML private AnchorPane containerShadow;
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
    private boolean savedProgress;
    private final Piece[] bluePieces;
    private final Piece[] orangePieces;

    public GobbletGobblersController()
    {
        controller = this;
        board = new Board();
        selectedPiece = null;
        savedProgress = true;

        bluePieces = new Piece[3];
        for(int i=0; i<3; i++)
            bluePieces[i] = new Piece(Size.values()[i], Color.BLUE);

        orangePieces = new Piece[3];
        for(int i=0; i<3; i++)
            orangePieces[i] = new Piece(Size.values()[i], Color.ORANGE);
    }

    public boolean getSavedProgress()
    {
        return controller.savedProgress;
    }

    public void startNewGame(ActionEvent event)
    {
        try {
            switchScene(event, "game.fxml");
            controller.containerShadow.setVisible(true);
            //controller.containerShadow.setLayoutX(14.0);
        } catch(IOException e) {
            Popup.error(e);
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
            Popup.error(e);
        }
    }

    public void openSettings(ActionEvent event)
    {
        try {
            switchScene(event, "settings.fxml");
        } catch(IOException e) {
            Popup.error(e);
        }
    }

    public void exitGame(ActionEvent event)
    {
        Stage stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.close();
    }

    public void returnToMenu(ActionEvent event)
    {
        if(!controller.savedProgress)
        {
            Popup.returnToMenu();
            Popup.getAlert()
                .showAndWait()
                .ifPresent(response -> controller.savedProgress = (response == ButtonType.OK));
        }

        if(controller.savedProgress)
        {
            try {
                switchScene(event, "start.fxml");
            } catch(IOException e) {
                Popup.error(e);
            }
        }
    }

    public void saveGame()
    {
        try {
            controller.board.serialize();
            controller.savedProgress = true;
        } catch(IOException e) {
            Popup.error(e);
        }
    }

    public void resetGame()
    {
        if(!savedProgress)
        {
            Popup.reset();
            Popup.getAlert()
                .showAndWait()
                .ifPresent(response -> controller.savedProgress = (response == ButtonType.OK));
        }

        if(savedProgress)
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
            controller.savedProgress = false;

            updateBoard();
            PieceProxy.hideProxy(controller.squaresContainer);

            if(controller.board.checkForWinner())
            {
                endGame();
                return;
            }

            controller.containerShadow.setLayoutX(controller.board.getTurn() == Color.BLUE ? 686.0 : 14.0);
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

        controller.containerShadow.setLayoutX(controller.board.getTurn() == Color.BLUE ? 686.0 : 14.0);
    }

    private void endGame()
    {
        controller.containerShadow.setVisible(false);
        controller.squaresContainer.setDisable(true);
        controller.bluePiecesContainer.setDisable(true);
        controller.orangePiecesContainer.setDisable(true);
        controller.savedProgress = true;

        Popup.gameOver(controller.board.getWinner());
        Popup.getAlert().show();
    }
}