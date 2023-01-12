/*
 * A player for testing purposes
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */

package src.main.java.com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.*;

/**
 * Test player
 */
public class TestPlayer extends Player {

    /**
     * Constructor
     * @param name Player's name
     * @param color Player color: one of Constants.BLACK or Constants.WHITE
     */
    public TestPlayer(int color) {
        super(color);
    }

    public Board createBoard(Board inputBoard) {
        Board newBoard = new Board();
        for(int row = 0; row < Constants.SIZE; row++) {
            for(int col = 0; col < Constants.SIZE; col++) {
                newBoard.setSquare(new Player(inputBoard.getSquare(new Position(row, col)).getStatus()), new Position(row, col));
            }
        }

        return newBoard; 
    }

    /**
     *
     * @param board
     * @return The player's next move
     */
    @Override
    public Position getNextMove(Board board) {
        int blackSquares = board.countSquares(Constants.BLACK);
        int whiteSquares = board.countSquares(Constants.WHITE);
        int squaresChanged = 0; 

        Map<Position, Integer> moveValue = new HashMap();
        Board newBoard = new Board();

        if(!board.noMovesAvailable(this)) {
            
            // checks all positions for their number of pieces added to the board.
            // Then applies that value to a map(moveValue) with the key of the 
            // position andthe value of the squares changed. 

            for(int row = 0; row < Constants.SIZE; row++) {
                for(int col = 0; col < Constants.SIZE; col++) {
                    newBoard = createBoard(board); 
                    if (isLegalMove(newBoard, new Position(row, col))) {
                        newBoard.makeMove(this, new Position(row, col));
                        squaresChanged = newBoard.countSquares(this.getColor()) - board.countSquares(this.getColor());
                        moveValue.put(new Position(row, col), squaresChanged);
                    }  
                }
            }

            // checks all the map values against each other. 
            // returns the greatest position
            // uses a nested for loop b/c map keys are Positions, which need x and y

            int maxValue = moveValue.get(new Position(0, 0)); 
            Position maxValuePos = new Position(0, 0);
            for(int row = 0; row < Constants.SIZE; row++) {
                for(int col = 0; col < Constants.SIZE; col++) {
                    if(moveValue.get(new Position(row, col)) > maxValue) {
                        maxValue = moveValue.get(new Position(row, col)); 
                        maxValuePos = new Position(row, col);
                    }
                }
            }
            return maxValuePos;
        }

        return null; 
        
        /**  ArrayList<Position> list = this.getLegalMoves(board);
        if (list.size() > 0) {
        int idx = (int) (Math.random() * list.size());
        return list.get(idx);
        } else {
        return null;
        }
         **/
    }

    /**
     * Is this a legal move?
     * @param player Player asking
     * @param positionToCheck Position of the move being checked
     * @return True if this space is a legal move
     */
    private boolean isLegalMove(Board board, Position positionToCheck) {
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            if (step(board, positionToCheck, directionVector, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Traverses the board in the provided direction. Checks the status of
     * each space: 
     * a. If it's the opposing player then we'll move to the next
     *    space to see if there's a blank space
     * b. If it's the same player then this direction doesn't represent
     *    a legal move
     * c. If it's a blank AND if it's not the adjacent square then this
     *    direction is a legal move. Otherwise, it's not.
     * 
     * @param player  Player making the request
     * @param position Position being checked
     * @param direction Direction to move
     * @param count Number of steps we've made so far
     * @return True if we find a legal move
     */
    private boolean step(Board board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int color = this.getColor();
        if (newPosition.isOffBoard()) {
            return false;
        } else if (board.getSquare(newPosition).getStatus() == -color) {
            return this.step(board, newPosition, direction, count+1);
        } else if (board.getSquare(newPosition).getStatus() == color) {
            return count > 0;
        } else {
            return false;
        }
    }

    /**
     * Get the legal moves for this player on the board
     * @param board
     * @return True if this is a legal move for the player
     */
    public ArrayList<Position> getLegalMoves(Board board) {
        int color = this.getColor();
        ArrayList list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                if (board.getSquare(this, row, col).getStatus() == Constants.EMPTY) {
                    Position testPosition = new Position(row, col);
                    if (this.isLegalMove(board, testPosition)) {
                        list.add(testPosition);
                    }
                }        
            }
        }
        return list;
    }

}