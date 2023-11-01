package com.game.gobbletgobblers.board;

import java.io.*;
import java.util.EmptyStackException;
import java.util.Stack;

public class Board implements Serializable
{
    private Stack<Piece>[][] board;
    private Color turn;
    private Color winner;
    private int freeSquares;
    private boolean isMoving;
    private int[] movingFrom;

    public Board()
    {
        reset();
    }

    public Stack<Piece>[][] getBoard()
    {
        return board;
    }

    public Color getTurn()
    {
        return turn;
    }

    public Color getWinner()
    {
        return winner;
    }

    public void changeTurn()
    {
        turn = (turn == Color.ORANGE) ? Color.BLUE : Color.ORANGE;
    }

    public void placePiece(int row, int col, Piece piece) throws IllegalStateException
    {
        if(isMoving && movingFrom[0] == row && movingFrom[1] == col)
            throw new IllegalStateException("You must place the piece in a new square after selecting it!");

        if(board[row][col].isEmpty())
        {
            board[row][col].push(piece);
            freeSquares--;

            isMoving = false;
            movingFrom[0] = -1; movingFrom[1] = -1;

            return;
        }

        if(board[row][col].size() < 3)
        {
            Piece lastPlaced = board[row][col].peek();

            if(piece.getSize().ordinal() > lastPlaced.getSize().ordinal())
            {
                board[row][col].push(piece);

                isMoving = false;
                movingFrom[0] = -1; movingFrom[1] = -1;
            }
            else
                throw new IllegalStateException("This piece is too small to be placed here!");

            return;
        }

        throw new IllegalStateException("This piece is too small to be placed here!");
    }

    public Piece movePiece(int row, int col)
    {
        if(board[row][col].isEmpty() || board[row][col].peek().getColor() != turn)
            return null;

        if(board[row][col].size() == 1)
            freeSquares++;

        isMoving = true;
        movingFrom[0] = row; movingFrom[1] = col;

        return board[row][col].pop();
    }

    public boolean checkForWinner()
    {
        // check all rows
        for(int row=0; row<3; row++)
        {
            try {
                if(board[row][0].peek().getColor() == board[row][1].peek().getColor() &&
                    board[row][1].peek().getColor() == board[row][2].peek().getColor())
                {
                    winner = board[row][0].peek().getColor();
                    return true;
                }
            } catch (EmptyStackException e) {
                // continue checking
            }
        }

        // check all columns
        for(int col=0; col<3; col++)
        {
            try {
                if(board[0][col].peek().getColor() == board[1][col].peek().getColor() &&
                    board[1][col].peek().getColor() == board[2][col].peek().getColor())
                {
                    winner = board[0][col].peek().getColor();
                    return true;
                }
            } catch (EmptyStackException e) {
                // continue checking
            }
        }

        // check main diagonal
        try {
            if(board[0][0].peek().getColor() == board[1][1].peek().getColor() &&
                board[1][1].peek().getColor() == board[2][2].peek().getColor())
            {
                winner = board[1][1].peek().getColor();
                return true;
            }
        } catch(EmptyStackException e) {
            // continue checking
        }

        // check antidiagonal
        try {
            if(board[0][2].peek().getColor() == board[1][1].peek().getColor() &&
                board[1][1].peek().getColor() == board[2][0].peek().getColor())
            {
                winner = board[1][1].peek().getColor();
                return true;
            }
        } catch(EmptyStackException e) {
            // do nothing
        }

        return freeSquares == 0;
    }

    public void reset()
    {
        board = new Stack[3][3];
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                board[i][j] = new Stack<>();
            }
        }

        turn = Color.ORANGE;
        winner = null;
        freeSquares = 9;
        isMoving = false;
        movingFrom = new int[]{ -1 , -1 };
    }

    public void serialize() throws IOException
    {
        // creating file if it doesn't exist
        File saveFile = new File("data/save.txt");
        saveFile.createNewFile();

        FileOutputStream outFile = new FileOutputStream("data/save.txt");
        ObjectOutputStream out = new ObjectOutputStream(outFile);

        out.writeObject(this);
        out.flush();

        outFile.close();
        out.close();
    }

    public Board deserialize() throws IOException, ClassNotFoundException
    {
        FileInputStream inFile = new FileInputStream("data/save.txt");
        ObjectInputStream in = new ObjectInputStream(inFile);

        Board loadedBoard = (Board) in.readObject();

        inFile.close();
        in.close();

        return loadedBoard;
    }
}
