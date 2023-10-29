package com.game.gobbletgobblers.util;

import javafx.scene.image.Image;

public class ImageLoader
{
    private static final Image largeBlueImage = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/large-blue.png").toString());
    private static final Image mediumBlueImage  = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/medium-blue.png").toString());
    private static final Image smallBlueImage = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/small-blue.png").toString());
    private static final Image largeOrangeImage = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/large-orange.png").toString());
    private static final Image mediumOrangeImage = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/medium-orange.png").toString());
    private static final Image smallOrangeImage = new Image(
            ImageLoader.class.getResource("/com/game/gobbletgobblers/img/small-orange.png").toString());

    private ImageLoader() { }

    public static Image getLargeBluePiece()
    {
        return largeBlueImage;
    }

    public static Image getMediumBluePiece()
    {
        return mediumBlueImage;
    }

    public static Image getSmallBluePiece()
    {
        return smallBlueImage;
    }

    public static Image getLargeOrangePiece()
    {
        return largeOrangeImage;
    }

    public static Image getMediumOrangePiece()
    {
        return mediumOrangeImage;
    }

    public static Image getSmallOrangePiece()
    {
        return smallOrangeImage;
    }
}
