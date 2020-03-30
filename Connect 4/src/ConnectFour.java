/**
 * Connect Four game:
 * Takes input from user and computer, the game is represented on a board, each position on the board
 * has one of three states RED, YELLOW or VOID (the colours of course representing if a piece
 * of that colour is in this position).
 * <p>
 * Input is taken from users and the appropriate action is taken on the board:
 * Commands are dx to drop a piece in column x or vx to veto column x preventing any pieces from
 * being dropped here next turn. Connect Four class can read human input, get input from an AI
 * player and set up the game.
 * <p>
 * <p>
 * Author Nick Badot
 */

import java.util.Scanner;


public class ConnectFour {
    final static int RED = 1, YELLOW = -1, VOID = 0;
    static Scanner scanalot;
    Board board;
    CPUPlayer joe;
    int human, ai, firsttogo;

    //constructor
    public ConnectFour() {
        board = new Board();
        joe = new CPUPlayer(YELLOW);
    }

    //setup the game
    public void setupGame() {
        System.out.println("Human: You are about to play a of Connect 4 game against Joe the AI." +
                "\nThis is slightly different from a standard game of Connect 4. You can either DROP a piece into a " +
                "column, or VETO a column to prevent the other player from dropping a piece there next turn.\n" +
                "You cannot play a veto after your opponent has played a veto.\n" +
                "To make a move type D (drop) or V (veto) followed by the column number ");
        System.out.println("Select your colour red or yellow (r/y)");
        String s = scanalot.next();
        if (s.equals("r")) {
            System.out.println("Human player is red.");
            human = RED;
            ai = YELLOW;
        } else {
            human = YELLOW;
            ai = RED;
            System.out.println("Human player is Yellow.");
        }
        System.out.println("Human: Would you like to make the first move? (y/n))");
        s = scanalot.next();
        if (s.equalsIgnoreCase("y")) {
            firsttogo = human;
        } else firsttogo = ai;
    }

    //reads input from a human player
    public void readInput(int player) {
        if (player == RED) System.out.print("Red ");
        else System.out.print("Yellow ");
        System.out.println("player to move");
        String s = scanalot.next();
        if (s.length() < 2) readInput(player);
        else {
            char c = s.charAt(0);
            int col = Character.getNumericValue(s.charAt(1));
            //System.out.println(" Char is " + c + " Col is "+col);
            if (col > 7 || col < 1) {
                System.out.println("Column values must be between 0 and 6, please try again");
                readInput(player);
            }
            if (!(c == 'v') && !(c == 'V') && !(c == 'd') && !(c == 'D')) {
                System.out.println("Commands must begin with v (veto) or d (drop) followed by a column number," +
                        " please try again");
                readInput(player);
            }
            if (!board.makemove(s, player)) {
                System.out.println("Invalid move! try again");
                readInput(player);
            }
        }
    }

    //human makes a move
    public int humanMove() {
        readInput(human);
        System.out.println(board);
        int score = board.getScore(human, true);
        System.out.println("Human's score:" + score);
        return score;
    }

    //allows AI 'Joe' to make a move
    public int aiMove() {
        String m = joe.abSearch(board, 6, false, true);
        board.makemove(m, ai);
        System.out.println(board);
        int score = board.getScore(ai, true);
        System.out.println("AI's score:" + score);
        return score;
    }

    //Plays the game
    public void play() {
        scanalot = new Scanner(System.in);
        setupGame();
        if (firsttogo != human) {
            aiMove();
        }
        while (!board.stalemate()) {
            if (humanMove() >= 1800) {
                System.out.println("Human is Victorious!");
                break;
            }
            if (aiMove() >= 1800) {
                System.out.println("The AI is Victorious!! Take that humanity!!");
                break;
            }
        }
        System.out.println(board.movelist);
        scanalot.close();
    }

}
