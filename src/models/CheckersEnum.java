package models;

public class CheckersEnum {
    public enum Piece {
        // Enum constants for pieces on the checkers board
        BLACK("Black Piece"), BLACK_KING("Black King"), WHITE("White Piece"), WHITE_KING("White King"),
        // Enum constants for spaces on the checkers board
        OPEN("Empty"), ILLEGAL("Illegal"), ERROR("Error");

        private String pieceName;

        private Piece(String name) {
            this.pieceName = name;
        }

        // Returns the type of piece as a string
        public String toString() {
            return this.pieceName;
        }
    };
    
    public enum Player {
        // Enum constants for players
        HUMAN("Human"), AI_EASY_FIRST("Easy AI"), AI_EASY_RANDOM("Normal AI"), AI_HARD("Hard AI");

        private String playerName;

        private Player(String name) {
            this.playerName = name;
        }

        // Returns the type of piece as a string
        public String toString() {
            return this.playerName;
        }
    };

    // Used to keep track of whose turn it is
    public enum Turn {
        BLACK_TO_PLAY("Black To Play"), WHITE_TO_PLAY("White To Play");
        
        private String representation;
        
        private Turn(String representation) {
            this.representation = representation;
        }
        
        // Returns the type of piece as a string
        public String toString() {
            return this.representation;
        }
    }
}
