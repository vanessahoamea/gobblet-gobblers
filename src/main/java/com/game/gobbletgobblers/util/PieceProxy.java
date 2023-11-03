package com.game.gobbletgobblers.util;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class PieceProxy
{
    private static ImageView imageView;
    private static double startX;
    private static double startY;

    private PieceProxy() { }

    public static void setStartPosition(double x, double y)
    {
        startX = x;
        startY = y;
    }

    public static void createProxy(ImageView view, AnchorPane pane)
    {
        PieceProxy.imageView = new ImageView(view.getImage());

        PieceProxy.imageView.setLayoutX(startX - 10);
        PieceProxy.imageView.setLayoutY(startY - 10);
        PieceProxy.imageView.setFitHeight(100.0);
        PieceProxy.imageView.setFitWidth(100.0);
        PieceProxy.imageView.setOpacity(0.5);

        ((AnchorPane) pane.getParent()).getChildren().add(imageView);
        pane.getScene().setOnMouseMoved(PieceProxy::updatePosition);
    }

    public static void hideProxy(AnchorPane pane)
    {
        ((AnchorPane) pane.getParent()).getChildren().remove(imageView);
        pane.getScene().removeEventHandler(MouseEvent.MOUSE_MOVED, PieceProxy::updatePosition);
    }

    private static void updatePosition(MouseEvent event)
    {
        imageView.setLayoutX(event.getX() - 10);
        imageView.setLayoutY(event.getY() - 10);
    }
}
