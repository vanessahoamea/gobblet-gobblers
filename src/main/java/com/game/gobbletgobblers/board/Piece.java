package com.game.gobbletgobblers.board;

import com.game.gobbletgobblers.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece
{
    private final Size size;
    private final Color color;

    public Piece(Size size, Color color)
    {
        this.size = size;
        this.color = color;
    }

    public Size getSize()
    {
        return size;
    }

    public Color getColor()
    {
        return color;
    }

    public ImageView getImage()
    {
        Image image;
        ImageView imageView;

        if(color == Color.BLUE)
        {
            image = switch(size)
            {
                case SMALL -> ImageLoader.getSmallBluePiece();
                case MEDIUM -> ImageLoader.getMediumBluePiece();
                case LARGE -> ImageLoader.getLargeBluePiece();
            };
        }
        else
        {
            image = switch(size)
            {
                case SMALL -> ImageLoader.getSmallOrangePiece();
                case MEDIUM -> ImageLoader.getMediumOrangePiece();
                case LARGE -> ImageLoader.getLargeOrangePiece();
            };
        }

        imageView = new ImageView(image);
        imageView.setFitHeight(130.0);
        imageView.setFitWidth(130.0);
        imageView.setPickOnBounds(true);

        return imageView;
    }
}
