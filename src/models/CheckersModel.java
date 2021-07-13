package models;

import java.util.ArrayList;
import java.util.Random;

import models.CheckersEnum.Piece;
import models.CheckersEnum.Player;
import models.CheckersEnum.Turn;
import windows.NewGameWindow;

// This class holds the model to create and store the checkers board and its pieces 
public class CheckersModel {

    // List of valid Pieces for comparison
    public static final Piece[] VALID_PIECES = { Piece.OPEN, Piece.WHITE, Piece.BLACK, Piece.WHITE_KING, Piece.BLACK_KING };
    public static final Piece[] INVALID_PIECES = { Piece.ILLEGAL, Piece.ERROR };
    
    // White is to play first (According to the requirements)
    public static final Turn DEFAULT_TURN = Turn.WHITE_TO_PLAY;
    
    // Standard Board Settings for Checkers
    public static final int MAX_PIECES = 12;
    public static final int BOARD_SIZE = 8;
    
    // Used to address piece positions by checkers notation
    private static final String COLUMNS = "ABCDEFGH";

    private class CheckersBoard {

        // Declares the standard starting setup for Checkers
        private final Piece[][] STANDARD_SETUP = {
                { Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE },
                { Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL, Piece.WHITE, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN },
                { Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK },
                { Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK, Piece.ILLEGAL, Piece.BLACK } };

        // Declares the standard starting setup for Checkers
        private final Piece[][] BLANK_SETUP = {
                { Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN },
                { Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN },
                { Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN },
                { Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL },
                { Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN, Piece.ILLEGAL, Piece.OPEN } };

        // An array to hold the type of piece in each position
        private Piece[][] pieces = new Piece[BOARD_SIZE][BOARD_SIZE];

        // Total number of pieces placed by each team
        private int totalBlackPieces;
        private int totalWhitePieces;

        // Load standard or blank starting setup
        private CheckersBoard(boolean standard) {
            if (standard) {
                this.pieces = STANDARD_SETUP;
                this.totalBlackPieces = MAX_PIECES;
                this.totalWhitePieces = MAX_PIECES;
            } else {
                this.pieces = BLANK_SETUP;
                this.totalBlackPieces = 0;
                this.totalWhitePieces = 0;
            }

        }
        
        // Load standard or blank starting setup
        private CheckersBoard(Piece[][] pieces, int totalBlack, int totalWhite) {
            this.pieces = pieces;
            this.totalBlackPieces = totalBlack;
            this.totalWhitePieces = totalWhite;
        }

        // Convert string position "B3" to array coordinates (1,2) which map to Piece[2][1]
        private int[] coordinates(String position) {
            String column = position.substring(0, 1);
            String row = position.substring(1);

            int x = COLUMNS.indexOf(column);
            int y = Integer.parseInt(row) - 1;

            // Return Error if x or y is out of range
            if (x == -1 || y < 0 || y > 7) {
                x = -1;
                y = -1;
            }
            int[] coord = { x, y };
            // Return the coordinates
            return coord;
        }
        
        // Create a copy of the CheckersBoard data
        public CheckersBoard copy() {
            // Create new pieces array
            Piece[][] piecesCopy = new Piece[BOARD_SIZE][BOARD_SIZE];
            
            // Iterate across all of the legal (light) squares of the CheckersBoard
            for (int y = 0; y < 8; y++) {
                // Loop through all of the legal (light) squares in the current row
                for (int x = y % 2; x < 8; x = x + 2) {
                    // Add possible moves for the current location to the movelist
                    piecesCopy[y][x] = this.getPiece(x, y);
                }

            }
            
            return new CheckersBoard(piecesCopy, this.getTotalBlackPieces(), this.getTotalWhitePieces());
            
        }
        
        public void addAllPossibleMoves(ArrayList<RegularMove> regularList, ArrayList<CaptureMove> captureList) {
            // Build new main move lists (regularMoveList, captureMoveList)
            // Iterate across all of the legal (light) squares of the CheckersBoard
            for (int y = 0; y < 8; y++) {
                // Loop through all of the legal (light) squares in the current row
                for (int x = y % 2; x < 8; x = x + 2) {
                    // Add possible moves for the current location to the movelist
                    addPossibleMoves(x, y, regularList, captureList, turn);

                }

            }
        }
        
        public ArrayList<CaptureMove> getEnemyCaptureMoves() {
            
            Turn enemyTurn = (turn == Turn.BLACK_TO_PLAY) ? Turn.WHITE_TO_PLAY: Turn.BLACK_TO_PLAY;
            ArrayList<RegularMove> regularList = new ArrayList<RegularMove>();
            ArrayList<CaptureMove> captureList = new ArrayList<CaptureMove>();
            
            // Build new main move lists (regularMoveList, captureMoveList)
            // Iterate across all of the legal (light) squares of the CheckersBoard
            for (int y = 0; y < 8; y++) {
                // Loop through all of the legal (light) squares in the current row
                for (int x = y % 2; x < 8; x = x + 2) {
                    // Add possible moves for the current location to the movelist
                    addPossibleMoves(x, y, regularList, captureList, enemyTurn);

                }

            }
            
            return captureList;
        }
        
        // Add possible moves for the piece located at the given coordinates
        private void addPossibleMoves(int x, int y, ArrayList<RegularMove> regularList, ArrayList<CaptureMove> captureList, Turn testTurn) {
            
            int[] pieceCoord = {x, y};
            int[] testCoord = {0, 0};
            int[] jumpCoord = {0, 0};

            Piece currentPiece = getPiece(x, y);
            Piece testPiece;

            switch (testTurn) {
            case BLACK_TO_PLAY:

                switch (currentPiece) {
                case BLACK_KING:
                    // Check if the piece can move up and to the left
                    testCoord[0] = x - 1; testCoord[1] = y + 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, false));
                        break;
                    case WHITE: case WHITE_KING:
                        jumpCoord[0] = x - 2; jumpCoord[1] = y + 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, false));
                        }
                        break;
                    default:
                        // No valid moves up and to the left
                        break;

                    }

                    // Check if the piece can move up and to the right
                    testCoord[0] = x + 1; testCoord[1] = y + 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, false));
                        break;
                    case WHITE: case WHITE_KING:
                        jumpCoord[0] = x + 2; jumpCoord[1] = y + 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, false));
                        }
                        break;
                    default:
                        // No valid moves up and to the right
                        break;

                    }
                    // Fall through to remaining move cases
                    
                case BLACK:
                    // Check if the piece can move down and to the left
                    testCoord[0] = x - 1; testCoord[1] = y - 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, willPromote(currentPiece, testCoord[1])));
                        break;
                    case WHITE: case WHITE_KING:
                        jumpCoord[0] = x - 2; jumpCoord[1] = y - 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, willPromote(currentPiece, jumpCoord[1])));
                        }
                        break;
                    default:
                        // No valid moves up and to the left
                        break;

                    }
                    // Check if the piece can move down and to the right
                    testCoord[0] = x + 1; testCoord[1] = y - 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:                  
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, willPromote(currentPiece, testCoord[1])));
                        break;
                    case WHITE: case WHITE_KING:
                        jumpCoord[0] = x + 2; jumpCoord[1] = y - 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, willPromote(currentPiece, jumpCoord[1])));
                        }
                        break;
                    default:
                        // No valid moves up and to the right
                        break;

                    }
                    break;
                    
                default:
                    // Do nothing because only black pieces can move when BLACK_TO_PLAY
                    break;

                }
                break;
            case WHITE_TO_PLAY:
                switch (currentPiece) {
                case WHITE_KING:
                    // Check if the piece can move down and to the left
                    testCoord[0] = x - 1; testCoord[1] = y - 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, false));
                        break;
                    case BLACK: case BLACK_KING:
                        jumpCoord[0] = x - 2; jumpCoord[1] = y - 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, false));
                        }
                        break;
                    default:
                        // No valid moves up and to the left
                        break;

                    }

                    // Check if the piece can move down and to the right
                    testCoord[0] = x + 1; testCoord[1] = y - 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, false));
                        break;
                    case BLACK: case BLACK_KING:
                        jumpCoord[0] = x + 2; jumpCoord[1] = y - 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, false));
                        }
                        break;
                    default:
                        // No valid moves up and to the right
                        break;

                    }
                    // Fall through to remaining move cases
                    
                case WHITE:
                    // Check if the piece can move up and to the left
                    testCoord[0] = x - 1; testCoord[1] = y + 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, willPromote(currentPiece, testCoord[1])));
                        break;
                    case BLACK: case BLACK_KING:
                        jumpCoord[0] = x - 2; jumpCoord[1] = y + 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, willPromote(currentPiece, jumpCoord[1])));
                        }
                        break;
                    default:
                        // No valid moves up and to the left
                        break;

                    }

                    // Check if the piece can move up and to the right
                    testCoord[0] = x + 1; testCoord[1] = y + 1;
                    testPiece = getPiece(testCoord);

                    switch (testPiece) {
                    case OPEN:
                        // Add regular move to list
                        regularList.add(new RegularMove(pieceCoord, testCoord, willPromote(currentPiece, testCoord[1])));
                        break;
                    case BLACK: case BLACK_KING:
                        jumpCoord[0] = x + 2; jumpCoord[1] = y + 2;
                        if (isOpen(jumpCoord)) {
                            // Add capture move to list
                            captureList.add(new CaptureMove(pieceCoord, jumpCoord, willPromote(currentPiece, jumpCoord[1])));
                        }
                        break;
                    default:
                        // No valid moves up and to the right
                        break;

                    }

                default:
                    // Do nothing because only white pieces can move when WHITE_TO_PLAY
                    break;

                }
                break;

            }
            
        }
        
        //
        public void executeMove(Move newMove) {
            int[] startCoord = newMove.getStartCoord(); //get start coordinates
            int[] endCoord = newMove.getEndCoord(); //get end coordinates
            boolean capture = newMove.getCapture(); //determines if a piece will be captured
            
            // Finds out what kind of piece is being moved
            Piece pieceToMove = getPiece(startCoord);
            
            // Promotes a white piece if it's being moved to the black sides end
            if (endCoord[1] == 7 && pieceToMove== Piece.WHITE){
                pieceToMove = Piece.WHITE_KING;
            }
            // Promotes a black piece if it's being moved to the white sides end
            else if (endCoord[1] == 0  && pieceToMove == Piece.BLACK){
                pieceToMove = Piece.BLACK_KING;
            }
            
            // Removes the piece to be moved, places the piece at its destination
            setPiece(startCoord, Piece.OPEN);
            setPiece(endCoord, pieceToMove);
            
            // Determines if a piece is being captured
            if (capture == true){
                // Removes the captured piece
                setPiece(newMove.getCaptureCoord(), Piece.OPEN);
            }
        
        }

        // Get piece from the board
        public Piece getPiece(int[] coord) {
            if (validCoord(coord)) {
                int x = coord[0];
                int y = coord[1];
                return pieces[y][x];
            } else {
                return Piece.ERROR;
            }
        }

        public Piece getPiece(int x, int y) {
            int[] coord = { x, y };
            return getPiece(coord);
        }

        public Piece getPiece(String position) {
            int[] coord = coordinates(position);
            return getPiece(coord);
        }

        // Get total number of black pieces on the board
        public int getTotalBlackPieces() {
            return totalBlackPieces;
        }

        // Get total number of white pieces on the board
        public int getTotalWhitePieces() {
            return totalWhitePieces;
        }
        
        //
        public boolean isOpen(int[] coord) {
            return getPiece(coord) == Piece.OPEN;
        }

        // Set piece on the board
        public boolean setPiece(int[] coord, Piece newPiece) {
            // Check that the coordinates are valid
            if (validCoord(coord)) {

                int x = coord[0];
                int y = coord[1];

                // Get the current piece
                Piece oldPiece = pieces[y][x];

                // If the square is not an illegal position and the new piece is
                // valid
                if (oldPiece != Piece.ILLEGAL && validPiece(newPiece)) {

                    // Store the current values of the number of pieces on each team
                    int black = totalBlackPieces;
                    int white = totalWhitePieces;

                    // Update the totals for each team as needed
                    switch (oldPiece) {
                    case BLACK:
                    case BLACK_KING:
                        black--;
                        break;

                    case WHITE:
                    case WHITE_KING:
                        white--;
                        break;

                    default:
                        // Do nothing
                        break;
                    }

                    switch (newPiece) {
                    case BLACK:
                    case BLACK_KING:
                        black++;
                        break;

                    case WHITE:
                    case WHITE_KING:
                        white++;
                        break;

                    default:
                        // Do nothing
                        break;
                    }

                    // If the number of black and white pieces is valid after
                    // the change
                    if (black <= MAX_PIECES && white <= MAX_PIECES) {

                        // Add the new piece
                        pieces[y][x] = newPiece;

                        // Update the values of the number of pieces on each
                        // team
                        totalBlackPieces = black;
                        totalWhitePieces = white;

                        // Return success
                        return true;
                    }

                }

            }

            // Return failure if one of the conditions above fails
            return false;

        }

        public boolean setPiece(int x, int y, Piece newPiece) {
            int[] coord = { x, y };
            return setPiece(coord, newPiece);
        }

        public boolean setPiece(String position, Piece newPiece) {
            int[] coord = coordinates(position);
            return setPiece(coord, newPiece);
        }

        // Checks that the coordinates provides are valid
        private boolean validCoord(int[] coord) {
            return (coord.length == 2 && coord[0] >= 0 && coord[0] < BOARD_SIZE && coord[1] >= 0 && coord[1] < BOARD_SIZE);
        }

        // Checks if the piece provided is valid
        private boolean validPiece(Piece checkPiece) {
            return (checkPiece != Piece.ILLEGAL && checkPiece != Piece.ERROR);
        }

        // Checks that the maximum number of pieces is not exceeded by either
        // team and that their is at least one piece on the board
        public boolean validBoard() {
            return (totalBlackPieces <= MAX_PIECES && totalWhitePieces <= MAX_PIECES && (totalBlackPieces + totalWhitePieces) > 0);
        }

    }
    
    abstract class AI {
        public abstract Move computerMove();
    }
    
    private class EasyFirstAI extends AI {

        public Move computerMove() {
            
            if (captureMoveList.isEmpty()) {
                if (regularMoveList.isEmpty()) {
                    return null;
                }
                else {
                    // Return first move
                    return regularMoveList.get(0);
                }
            }
            else {
                // Return first move
                return captureMoveList.get(0);
            }

        }
        
    }
    
    private class EasyRandomAI extends AI {

        public Move computerMove() {
            
            Random random = new Random();
            
            if (captureMoveList.isEmpty()) {
                if (regularMoveList.isEmpty()) {
                    return null;
                }
                else {
                    // Return random move
                    return regularMoveList.get(random.nextInt(regularMoveList.size()));
                }
                
            }
            else {
                // Return random move
                return captureMoveList.get(random.nextInt(captureMoveList.size()));
            }

        }
        
    }
    
    private class HardAI extends AI {
        int[][] boardEvaluationWhite = new int[8][8];
        int[][] boardEvaluationBlack = new int[8][8];
        
        //Evaluate the board squares
        public void boardEvaluate(){
            int whtRowAverage=0;
            int whtColAverage=0;
            int blkRowAverage=0;
            int blkColAverage=0;
            for (int y = 0; y < 8; y++) {
                for (int x = y % 2; x < 8; x = x + 2) {
                    if(board.getPiece(x,y)==Piece.WHITE_KING || board.getPiece(x,y)==Piece.WHITE){
                        whtColAverage+=x+1;
                        whtRowAverage+=y+1;
                    }
                    else if(board.getPiece(x,y)==Piece.BLACK_KING || board.getPiece(x,y)==Piece.BLACK){
                        blkColAverage+=x+1;
                        blkRowAverage+=y+1;
                    }
                }
            }
            whtColAverage=whtColAverage/board.getTotalWhitePieces();
            whtRowAverage=whtColAverage/board.getTotalWhitePieces();    
            blkColAverage=blkColAverage/board.getTotalBlackPieces();
            blkRowAverage=blkRowAverage/board.getTotalBlackPieces();
           
            for (int y = 0; y < 8; y++) {
                for (int x = y % 2; x < 8; x = x + 2) {
                    boardEvaluationWhite[x][y]=(8-Math.abs(x+1-whtColAverage))*(8-Math.abs(y+1-whtRowAverage));                       
                    boardEvaluationBlack[x][y]=(8-Math.abs(x+1-blkColAverage))*(8-Math.abs(y+1-blkRowAverage));                       
                }
            }
        } 
               
        public Move computerMove() {
            
            Random random = new Random();
            
            if (captureMoveList.isEmpty())
            {
                ArrayList<RegularMove> bestMoves = new ArrayList<RegularMove>();
                
                int moveHighestValue = -1028;
                int moveValue=0;
                
                CheckersBoard testBoard;
                int possibleCaptures;                
                int currentCaptures = getEnemyTotalCaptures(board);
                
                boardEvaluate();
                
                for(RegularMove testMove: regularMoveList) {
                    // Create a board (testBoard) to represent the setup after the move (testMove) is made
                    testBoard = board.copy();
                    testBoard.executeMove(testMove);
                    possibleCaptures = getEnemyTotalCaptures(testBoard);
                    
                    int[] endCoord = testMove.getEndCoord();
                    if((turn==Turn.WHITE_TO_PLAY && board.getPiece(testMove.getStartCoord())==Piece.WHITE)){
                    	moveValue=boardEvaluationWhite[endCoord[0]][endCoord[1]];     	
                    }
                    else if (turn==Turn.BLACK_TO_PLAY && board.getPiece(testMove.getStartCoord())==Piece.BLACK_KING) {
                    	moveValue=(boardEvaluationWhite[endCoord[0]][endCoord[1]])/2;                       
                    }
                    else if (turn==Turn.WHITE_TO_PLAY && board.getPiece(testMove.getStartCoord())==Piece.WHITE_KING){
                        moveValue=(boardEvaluationBlack[endCoord[0]][endCoord[1]])/2;                                          	
                    }
                    else if (turn==Turn.BLACK_TO_PLAY && board.getPiece(testMove.getStartCoord())==Piece.BLACK) {
                        moveValue=boardEvaluationBlack[endCoord[0]][endCoord[1]];                       
                    }
                    moveValue -= 64 * (possibleCaptures - currentCaptures);
                     
                    // If the move is better
                    if (moveValue > moveHighestValue) {
                        moveHighestValue = moveValue;
                        
                        bestMoves.clear();
                        bestMoves.add(testMove);
                    }
                    // If the move is equally as good
                    else if (moveValue == moveHighestValue) {
                        bestMoves.add(testMove);
                    }
                    
                }
                
                // Return random move from the list of best regular moves
                return bestMoves.get(random.nextInt(bestMoves.size()));

            }
            else if (captureMoveList.size() == 1)
            {
                return captureMoveList.get(0);
            }
            else
            {
                // Find the best capture move
                int chainLength = 0;
                int maxChainLength = 0;
                ArrayList<CaptureMove> bestMoves = new ArrayList<CaptureMove>();
                        
                for (CaptureMove testMove: captureMoveList) {
                    
                    chainLength = 1 + captureChainLength(board, testMove);
                    
                    // If the move is better
                    if (chainLength > maxChainLength) {
                        maxChainLength = chainLength;
                        
                        bestMoves.clear();
                        bestMoves.add(testMove);
                    }
                    // If the move is equally as good
                    else if (chainLength == maxChainLength) {
                        bestMoves.add(testMove);
                    }
                }
                
                // Return random move from the list of best capture moves
                return bestMoves.get(random.nextInt(bestMoves.size()));

            }
        }
        
        private int getEnemyTotalCaptures(CheckersBoard testBoard) {
            int total = 0;
            for (CaptureMove testMove: testBoard.getEnemyCaptureMoves()) {
                total += 1 + captureChainLength(testBoard, testMove);
            }
            return total;
        }
        
        // Call method recursively to search through tree?
        private int captureChainLength(CheckersBoard testBoard, CaptureMove testMove) {
            
            if (testMove.getPromote()) {
                return 0;
            }
            else {
                // Create a copy of the board
                CheckersBoard newBoard = testBoard.copy();
                
                // Execute the test move
                newBoard.executeMove(testMove);
                
                // Create blank move lists
                ArrayList<RegularMove> regularList = new ArrayList<RegularMove>();
                ArrayList<CaptureMove> captureList = new ArrayList<CaptureMove>();
                
                // Get the move lists after the board after the move is executed
                newBoard.addAllPossibleMoves(regularList, captureList);
                
                if (captureList.isEmpty()) {
                    return 0;
                }
                else {
                    
                    int chainLength = 0;
                    int maxChainLength = 0;
                    
                    for (CaptureMove newMove: captureList) {
                        
                        chainLength = 1 + captureChainLength(newBoard, newMove);
                        
                        if (chainLength > maxChainLength) {
                            maxChainLength = chainLength;
                        }

                    }
                    return chainLength;
                }
            }
            
            
        }
    }
    
    abstract class Move {
        
        // Is this move a capture move?
        private boolean capture;
        
        // Does this move result in a promotion
        private boolean promote;
        
        // Start and end coordinates of the move
        private int startCoordX;
        private int startCoordY;
        private int endCoordX;
        private int endCoordY;
        
        //
        public Move(int[] startCoord, int[] endCoord, boolean promote, boolean capture) {
            // Note: Coordinates are not stored as arrays because Java was
            // not storing the data in the move lists properly.
            this.startCoordX = startCoord[0];
            this.startCoordY = startCoord[1];
            this.endCoordX = endCoord[0];
            this.endCoordY = endCoord[1];
            this.capture = capture;
            this.promote = promote;
        }
        
        // Get methods
        public boolean getCapture() {
            return capture;
        }
        
        public boolean getPromote() {
            return promote;
        }
        
        public int[] getStartCoord() {
            return new int[]{startCoordX, startCoordY};
        }
        
        public int[] getEndCoord() {
            return new int[]{endCoordX, endCoordY};
        }
        
        public int[] getCaptureCoord() {
            if (this.capture) {
                // Average start and end coordinates to get the capture coordinates
                return new int[]{(startCoordX + endCoordX) / 2, (startCoordY + endCoordY) / 2};
            }
            else {
                // There are no capture coordinates for when capture is not true
                return new int[]{-1, -1};
            }
        }
        
        // String Move Notation "D6 - C5"
        public String toString() {
            
            String representation;
            
            if (this.capture) {
                representation = COLUMNS.substring(startCoordX, startCoordX+1) + (startCoordY + 1) + 
                        " x " + COLUMNS.substring(endCoordX, endCoordX+1) + (endCoordY + 1);
            }
            else {
                representation = COLUMNS.substring(startCoordX, startCoordX+1) + (startCoordY + 1) + 
                        " - " + COLUMNS.substring(endCoordX, endCoordX+1) + (endCoordY + 1);
            }
            
            if (this.promote) {
                representation = "+ " + representation + " +";
            }
            
            return representation;
            
        }
        
    }
    
    private class RegularMove extends Move {
        // Create a move to store in memory
        private RegularMove(int[] startCoord, int[] endCoord, boolean promote) {
            super(startCoord, endCoord, promote, false);
        }
    }
    
    private class CaptureMove extends Move {
        // Create a move to store in memory
        private CaptureMove(int[] startCoord, int[] endCoord, boolean promote) {
            super(startCoord, endCoord, promote, true);
        }
    }
    
    // Create variables for CheckersModel    
    // Create model of the board
    private CheckersBoard board;

    // Number of turns that both teams have taken
    private int turnNumber;
    
    // Indicates if game setup is complete (used in custom setup)
    private boolean setupComplete;
    
    // The current list of valid regular moves
    private ArrayList<RegularMove> regularMoveList = new ArrayList<RegularMove>();
    
    // The current list of valid capture moves
    private ArrayList<CaptureMove> captureMoveList = new ArrayList<CaptureMove>();
    
    // The current list of invalid capture moves due to special cases
    private ArrayList<CaptureMove> invalidMoveList = new ArrayList<CaptureMove>();
    private ArrayList<int[]> invalidMoveCoord = new ArrayList<int []>();
    
    // The current list of valid capture moves in a capture chain
    private ArrayList<CaptureMove> captureChainMoveList = new ArrayList<CaptureMove>();
    
    // The list of all past moves made
    private ArrayList<Move> pastMoveList = new ArrayList<Move>();
    
    // Stores the last move made for easy reference
    private Move lastMove;
    
    // The string representation of all past moves made
    private String pastMoveListString;
    
    // Whose turn is next
    private Turn turn;
    
    // Players
    private Player blackPlayer;
    private Player whitePlayer;
    
    // AI
    private EasyFirstAI easyFirstComputer;
    private EasyRandomAI easyRandomComputer;
    private HardAI hardComputer;
    
    // Create an instance of CheckersModel 
    CheckersModel(boolean standard, Turn toPlay, Player black, Player white) {
        
        // Initialise variables 
        board = new CheckersBoard(standard);
        turnNumber = 1;
        pastMoveListString = "";
        lastMove = null;
        turn = toPlay;
        
        blackPlayer = black;
        whitePlayer = white;
        
        easyFirstComputer = new EasyFirstAI();
        easyRandomComputer = new EasyRandomAI();
        hardComputer = new HardAI();
        
        // The setup is complete if it is a standard (not custom) setup
        setupComplete = standard;
        
        // Load first move lists if the board setup is complete
        // Otherwise setupComplete() must be called later before the game can start
        if (setupComplete) {
            refreshMoveLists();
        }

    }
    
    // Refresh move lists to prepare for the next move
    private void refreshMoveLists() {
        // Make sure setup is complete before generating move lists
        if (setupComplete) {

            // Clear the current move lists
            regularMoveList.clear();
            captureMoveList.clear();
            captureChainMoveList.clear();
            invalidMoveList.clear();

            // Add all possible moves
            board.addAllPossibleMoves(regularMoveList, captureMoveList);

            // If the last move was a capture, the next capture move must be done by that same piece (if possible)
            if (lastMove != null) {
                
                // Get the last move
                int[] lastMoveCoord = lastMove.getEndCoord();
                int lastMoveX = lastMoveCoord[0];
                int lastMoveY = lastMoveCoord[1];
                
                // Setup variables for the loops
                int[] start;
                
                // If the last move was a promotion or there still 
                if ( lastMove.getPromote() ) {
                        invalidMoveCoord.add(lastMove.getEndCoord());
                }
                
                // There are still invalid moves due to promotion
                // Turn has not ended yet after promotion
                if (!invalidMoveCoord.isEmpty()) {
                    // Iterate through all capture moves
                    for (CaptureMove nextCapture: captureMoveList) {
                        start = nextCapture.getStartCoord();
                        
                        // Iterate through all the bad coordinates
                        for (int[] badCoord: invalidMoveCoord) {
                            if (start[0] == badCoord[0] && start[1] == badCoord[1]) {
                                invalidMoveList.add(nextCapture);
                            }
                        }
                    }
                    // Remove invalid moves due to promotion rule
                    // Pieces that have been promoted this turn cannot capture again that turn
                    captureMoveList.removeAll(invalidMoveList);
                }
                
                // Otherwise if the last move was a capture
                if (lastMove.getCapture()) {
                    boolean activeCaptureChain = false;
                    
                    // Iterate through all capture moves
                    for (CaptureMove nextCapture: captureMoveList) {
                        start = nextCapture.getStartCoord();
                        
                        // If the end of the last capture move is the start of the next capture move
                        if (start[0] == lastMoveX && start[1] == lastMoveY) {
                            captureChainMoveList.add(nextCapture);
                            activeCaptureChain = true;
                        }
                    } 
                    
                    // If a capture move chain is active
                    if (activeCaptureChain) {
                        captureMoveList.clear();
                        // Load the valid moves that continue the capture chain
                        captureMoveList.addAll(captureChainMoveList);
                    }
                }
                
            }
            
        }
        
    }
    
    // End turn
    private void endTurn() {
        switch (turn) {
        case BLACK_TO_PLAY:
            turn = Turn.WHITE_TO_PLAY;
            break;
        case WHITE_TO_PLAY:
            turn = Turn.BLACK_TO_PLAY;
            turnNumber = turnNumber + 1;
            break;
        }
        // Clear list of invalid moves since the turn has ended
        invalidMoveCoord.clear();
        // Load move lists for the next player
        refreshMoveLists();
        
        // Enable saving at the beginning of each turn
        NewGameWindow.enableSave();
        
        // Disable saving at the end of the game
        if (isFinished()) {
            NewGameWindow.disableSave();
        }

    }
    
    private void executeMove(Move newMove) {
        pastMoveList.add(newMove);
        lastMove = newMove;
        pastMoveListString += newMove.toString() + "\n";
        board.executeMove(newMove);
    }
    
    private boolean willPromote(Piece pieceToMove, int endCoordY) {
        return (endCoordY == 7 && pieceToMove == Piece.WHITE) || (endCoordY == 0  && pieceToMove == Piece.BLACK);
    }
    
    public Move getAIMove() {
        
        Move computerMove = null;
        
        switch (turn) {
        case BLACK_TO_PLAY:
            switch (blackPlayer) {
            case AI_EASY_FIRST:
                computerMove = easyFirstComputer.computerMove();
                break;
            case AI_EASY_RANDOM:
                computerMove = easyRandomComputer.computerMove();
                break;
            case AI_HARD:
                computerMove = hardComputer.computerMove();
                break;
            default:
                break;
            
            }

            break;
        case WHITE_TO_PLAY:
            switch (whitePlayer) {
            case AI_EASY_FIRST:
                computerMove = easyFirstComputer.computerMove();
                break;
            case AI_EASY_RANDOM:
                computerMove = easyRandomComputer.computerMove();
                break;
            case AI_HARD:
                computerMove = hardComputer.computerMove();
                break;
            default:
                break;
            
            }
            
            break;
        
        }
        
        return computerMove;
    }
    
    // Get the past move list string representation
    public String getPastMoveListString() {
        return pastMoveListString;
    }
    
    // Get piece from the board
    public Piece getPiece(int[] coord) {
        return board.getPiece(coord);
    }

    public Piece getPiece(int x, int y) {
        return board.getPiece(x, y);
    }

    public Piece getPiece(String position) {
        return board.getPiece(position);
    }
    
    public Turn getTurn() {
        return turn;
    }
    
    public Player getBlackPlayer() {
        return blackPlayer;
    }
    
    public Player getWhitePlayer() {
        return whitePlayer;
    }
    
    // Is there currently a capture move available?
    public boolean isCapture() {
        return !captureMoveList.isEmpty();
    }
    
    // Is the the game now over?
    public boolean isFinished() {
        return (captureMoveList.isEmpty() && regularMoveList.isEmpty());
    }
    
    //
    public boolean isHumanTurn() {
        return ((turn == Turn.BLACK_TO_PLAY && blackPlayer == Player.HUMAN) || (turn == Turn.WHITE_TO_PLAY && whitePlayer == Player.HUMAN));
    }
    
    // Get the past move list string representation
    public boolean setPastMoveListString(String loadPastMoves) {
        // If setup is not complete
        if (setupComplete == false) {
            // Load past moves string
            pastMoveListString = loadPastMoves;
            // Return success
            return true;
        }
        else {
            // Cannot edit past move list externally once setup is complete
            // Return failure
            return false;
        }
        
    }

    // Set piece on the board
    public boolean setPiece(int[] coord, Piece newPiece) {
        return board.setPiece(coord, newPiece);
    }

    public boolean setPiece(int x, int y, Piece newPiece) {
        return board.setPiece(x, y, newPiece);
    }

    public boolean setPiece(String position, Piece newPiece) {
        return board.setPiece(position, newPiece);
    }
    
    // Used to indicate that the custom setup is now complete
    public void setupComplete() {
        
        if (!setupComplete) {
            setupComplete = true;
            refreshMoveLists();
        }
        
    }
    
    
    // Determines if the current model setup is valid
    public boolean validStartSetup() {

        // Check if the board is valid
        if (board.validBoard()) {

            // Get the number of pieces on each team
            int black = board.getTotalBlackPieces();
            int white = board.getTotalWhitePieces();

            // Check if the game is still in progress (neither team has
            // Won/lost)
            if (black != 0 && white != 0) {
                // The setup is a valid start setup
                return true;
            }
            else {
                // The setup is an invalid start setup
                return false;
            }
        }
        else {
            // The setup is invalid
            return false;
        }

    }
    
    // Is the first click a valid location to begin a move?
    public boolean validFirstClick(int[] clickCoord){
        Piece pieceClicked = getPiece(clickCoord);
        
        boolean valid = false;
        
        switch (turn) {
        case BLACK_TO_PLAY:  
            
            switch (pieceClicked){
            case BLACK: case BLACK_KING:
                valid = true;            
                break;
            default:
                break;
            }
            break;
            
        case WHITE_TO_PLAY:   
            switch (pieceClicked){
            case WHITE: case WHITE_KING:
                valid = true;            
                break;
            default:
                break;
            }
            break;    
        }
        
        return valid;
        
    }

    // Is the move being attempted valid? If so execute it.
    public boolean validMove(int[] startCoord, int[] endCoord) {

        if (captureMoveList.isEmpty()) {
            for(RegularMove currentMove: regularMoveList) {
                int[] start = currentMove.getStartCoord();
                int[] end = currentMove.getEndCoord();
                
                if (start[0]==startCoord[0] && start[1]==startCoord[1] &&
                    end[0]==endCoord[0] && end[1]==endCoord[1]) {
                    // Do it!
                    executeMove(currentMove);
                    endTurn();
                    return true;
                }
            }
        }
        else {
            for(CaptureMove currentMove: captureMoveList) {
                int[] start = currentMove.getStartCoord();
                int[] end = currentMove.getEndCoord();
                
                if (start[0]==startCoord[0] && start[1]==startCoord[1] &&
                    end[0]==endCoord[0] && end[1]==endCoord[1]) {
                    
                    // Execute the valid move
                    executeMove(currentMove);
                    // Reload load move lists for the same player
                    refreshMoveLists();
                    // If there are no more valid capture moves
                    if (captureMoveList.isEmpty()) {
                        endTurn();
                    }
                    else {
                        // Disable saving for the rest of the turn
                        NewGameWindow.disableSave();
                    }
                    
                    // A valid move existed and was executed
                    return true;
                    
                }
            }
        }
        
        // The move was invalid and not executed
        return false;
    }

}
