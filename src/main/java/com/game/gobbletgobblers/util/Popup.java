package com.game.gobbletgobblers.util;

import com.game.gobbletgobblers.board.Color;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class Popup
{
    private static final Alert alert;
    static
    {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    }

    private Popup() { }

    public static Alert getAlert()
    {
        return alert;
    }

    public static void returnToMenu()
    {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to menu");
        alert.setContentText("Are you sure you want to quit this game? Your progress will be lost unless you save first.");
    }

    public static void exit()
    {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit? Any unsaved progress will be lost.");
    }

    public static void reset()
    {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset");
        alert.setContentText("Are you sure you want to reset the board? Your progress so far will be lost.");
    }

    public static void gameOver(Color winner)
    {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over");

        if(winner == Color.BLUE)
            alert.setContentText("Blue is the winner! Congratulations!");
        else if(winner == Color.ORANGE)
            alert.setContentText("Orange is the winner! Congratulations!");
        else
            alert.setContentText("The game ended in a draw! Better luck next time!");
    }

    public static void error(Exception e)
    {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setContentText("There was an unexpected error: " + e.getMessage());
    }
}
