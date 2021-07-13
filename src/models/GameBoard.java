package models;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

import models.CheckersModel.Move;
import models.CheckersEnum.Piece;
import models.CheckersEnum.Player;
import models.CheckersEnum.Turn;
import windows.NewGameWindow;

// GameBoard draws the checkers board and all of its pieces
public class GameBoard extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    // AI wait time (ms)
    public static final int DEFAULT_WAIT_TIME = 200;
    
    // Constants for the board size
    private static final int SQUARE_PIXEL_SIZE = 50;
    private static final int BOARD_PIXEL_SIZE = SQUARE_PIXEL_SIZE * CheckersModel.BOARD_SIZE;
    
    // The pieces on the board are drawn based on the model
    public CheckersModel model;
    
    private boolean gamePaused;
    private int waitTime;
    
    private int[] startSquare = { -1, -1 };
    private int[] endSquare = { -1, -1 };
    
    private boolean resigned;
    

    // Initialize the GameBoard
    public GameBoard(boolean standard, Turn toPlay,
                     Player black, Player white) {
        setSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        
        gamePaused = true;
        waitTime = DEFAULT_WAIT_TIME;    
        model = new CheckersModel(standard, toPlay, black, white);
        resigned = false;

        this.setPreferredSize(new Dimension(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE));
        this.addMouseListener(new TestMouse());
        
        // Run first turn after initialization is complete
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {        
                waitForNextTurn();
            }
        });

    }
    
    // Painting everything on the GameBoard
    @Override
    public void paint(Graphics square) {

        super.paint(square);
        
        // Create Piece variable for loop
        Piece piece;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                // Checker board pattern is created
                if (x % 2 == y % 2) {
                    square.setColor(Color.RED);
                } else {
                    square.setColor(Color.BLACK);

                }
                square.fillRect(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);

                piece = model.getPiece(x, 7 - y);

                switch (piece) {
                case WHITE:
                    square.setColor(Color.GRAY);
                    square.fillOval(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                    square.setColor(new Color(220, 222, 220));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 4, y * SQUARE_PIXEL_SIZE + 4, 42, 42);
                    break;

                case BLACK:
                    square.setColor(Color.DARK_GRAY);
                    square.fillOval(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                    square.setColor(new Color(120, 120, 120));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 4, y * SQUARE_PIXEL_SIZE + 4, 42, 42);
                    break;

                case WHITE_KING:
                    // Small gold circle is used to indicate king
                    square.setColor(Color.GRAY);
                    square.fillOval(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                    square.setColor(new Color(220, 222, 220));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 4, y * SQUARE_PIXEL_SIZE + 4, 42, 42);
                    square.setColor(new Color(227, 196, 10));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 15, y * SQUARE_PIXEL_SIZE + 15, 20, 20);
                    break;

                case BLACK_KING:
                    // Small gold circle is used to indicate king
                    square.setColor(Color.DARK_GRAY);
                    square.fillOval(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE);
                    square.setColor(new Color(120, 120, 120));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 4, y * SQUARE_PIXEL_SIZE + 4, 42, 42);
                    square.setColor(new Color(227, 196, 10));
                    square.fillOval(x * SQUARE_PIXEL_SIZE + 15, y * SQUARE_PIXEL_SIZE + 15, 20, 20);
                    break;

                default:
                    // Do nothing for all other cases
                    break;

                }

                // Draw rectangle for valid start square
                if (endSquare[0] == x && endSquare[1] == 7 - y && model.validFirstClick(endSquare)) {
                    square.setColor(Color.YELLOW);
                    square.drawRect(x * SQUARE_PIXEL_SIZE, y * SQUARE_PIXEL_SIZE, SQUARE_PIXEL_SIZE - 1,
                            SQUARE_PIXEL_SIZE - 1);
                }

            }

        }

    }
    
    public boolean isPaused() {
        return gamePaused;
    }
    
    public void pauseGame() {
        gamePaused = true;
    }
    
    public void resign() {
        resigned = true;
    }
    
    public void resumeGame() {
        gamePaused = false;
    }
    
    public void setTime(int time) {
        waitTime = time;
    }
    
    public void startGame() {
        resumeGame();
    }
    
    private void click(int[] coord) {
        startSquare = endSquare;
        endSquare = coord;

        // Check if move is valid and if so execute move
        boolean success = model.validMove(startSquare, endSquare);
        
        // Repaint the information right now to avoid graphics glitches
        paintImmediately(this.getVisibleRect());
        
        // If a move was made
        if (success) {
            // Update the window
            NewGameWindow.update();

            if (!model.isFinished()) {
                
                waitForNextTurn();

            }
            
        }
    }
    
    private void turn() {
        if (model.isHumanTurn()) {
            // Enable Resign. Wait for human input.
            NewGameWindow.enableResign();
        }
        else {
            // Disable Resign
            NewGameWindow.disableResign();
            
            /* Create worker thread for computer player calculations 
               so that the user interface remains responsive         */
            
            SwingWorker<Move, Void> workerThread = new SwingWorker<Move, Void>() {
                // Schedule a compute-intensive task in a background thread
                @Override
                protected Move doInBackground() throws Exception {
                   
                   return model.getAIMove();

                }
        
                // Run in event-dispatching thread (EDT) after doInBackground() completes
                @Override
                protected void done() {
                   try {
                      // Use get() to get the result of doInBackground()
                      final Move computerMove = get();
                      
                      // Set a timer so that the first click occurs one waitTime later
                      Timer firstClick = new Timer(1 * waitTime, new ActionListener() {
                          public void actionPerformed(ActionEvent evt) {
                              // Make first click
                              click(computerMove.getStartCoord());
                          };
                      });
                      
                      firstClick.setRepeats(false);
                      firstClick.start();
                      
                      // Set a timer so that the second click occurs two waitTimes later
                      Timer secondClick = new Timer(2 * waitTime, new ActionListener() {
                          public void actionPerformed(ActionEvent evt) {
                              // Make second click
                              click(computerMove.getEndCoord());
                          };
                      });
                      
                      secondClick.setRepeats(false);
                      secondClick.start();
                      

                   } catch (InterruptedException | ExecutionException e) {
                      // Do Nothing
                   } 
                }
             };
             
             // Start the computer AI worker thread
             workerThread.execute();
            
        }
    }
    
    private void waitForNextTurn() {
        
        // If the board cannot be displayed after after the game
        // has started then the NewGameWindow has been closed
        if (!this.isDisplayable() && !gamePaused) {
            // End this thread and don't create any more
            return;
        }
        
        // If the game is displayed but paused
        else if (gamePaused) {
            // Loop: Wait for the game to start or resume
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {        
                    waitForNextTurn();
                }
            });
        }
        else {
            // Run next turn after the NewGameWindow updates
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    turn();
                }
            });
        };
    }
    

    // Test mouse click input
    private class TestMouse extends MouseInputAdapter {

        public void mousePressed(MouseEvent e) {
            // Get the coordinates
            int x = e.getX() / SQUARE_PIXEL_SIZE;
            int y = (BOARD_PIXEL_SIZE - e.getY()) / SQUARE_PIXEL_SIZE;
            int[] coord = { x, y };
            
            if (model.isHumanTurn() && !resigned) {
                // Register the click in the model
                click(coord);
            }
            
        }

    }

}
