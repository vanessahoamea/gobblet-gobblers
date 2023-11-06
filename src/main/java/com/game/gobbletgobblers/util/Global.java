package com.game.gobbletgobblers.util;

import com.game.gobbletgobblers.board.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;
import java.util.prefs.Preferences;

public class Global
{
    private static final Preferences prefs = Preferences.userRoot().node(Global.class.getName());
    private static final Media media = new Media(Objects.requireNonNull(Global.class.getResource("/com/game/gobbletgobblers/music/theme.mp3")).toString());
    private static final MediaPlayer mediaPlayer = new MediaPlayer(media);
    private static boolean isMusicOn = prefs.getBoolean("musicOn", true);
    private static boolean isMusicPlaying = false;
    private static Color startingColor = Color.values()[prefs.getInt("startingColor", 0)];

    private Global() { }

    public static boolean getIsMusicOn()
    {
        return isMusicOn;
    }

    public static Color getStartingColor()
    {
        return startingColor;
    }

    public static double getStartingContainerX()
    {
        return (startingColor == Color.BLUE) ? 686.0 : 14.0;
    }

    public static void playMusic()
    {
        if(isMusicPlaying)
            return;

        mediaPlayer.play();
        isMusicOn = true;
        isMusicPlaying = true;
        prefs.putBoolean("musicOn", true);
    }

    public static void stopMusic()
    {
        if(!isMusicPlaying)
            return;

        mediaPlayer.stop();
        isMusicOn = false;
        isMusicPlaying = false;
        prefs.putBoolean("musicOn", false);
    }

    public static void switchStartingColor(Color startingColor)
    {
        Global.startingColor = startingColor;
        prefs.putInt("startingColor", startingColor.ordinal());
    }
}
