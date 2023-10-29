package com.game.gobbletgobblers.board;

public class Piece
{
    private final Size size;
    private final Color color;
    private final String icon;

    public Piece(Size size, Color color)
    {
        this.size = size;
        this.color = color;
        this.icon = "img/" + size.toString().toLowerCase() + "-" +
                color.toString().toLowerCase() + ".png";
    }

    public Size getSize()
    {
        return size;
    }

    public Color getColor()
    {
        return color;
    }

    public String getIcon()
    {
        return icon;
    }

    @Override
    public String toString()
    {
        var str = new StringBuilder();

        switch(size)
        {
            case LARGE -> str.append("L");
            case MEDIUM -> str.append("M");
            case SMALL -> str.append("S");
        }

        switch(color)
        {
            case BLUE -> str.append("B");
            case ORANGE -> str.append("O");
        }

        return str.toString();
    }
}
