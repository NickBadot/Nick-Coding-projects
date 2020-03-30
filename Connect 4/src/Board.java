import java.util.ArrayList;
import java.util.Stack;

/**
 * This class represents the board used in a connect four game, the 5*7 board is represented by an
 * int array, a place in the grid with no pieces has a value 0, one with a "red" piece has a value
 * of 1 and a "yellow" piece has a value of -1.
 *
 * Pieces are dropped in one of the seven columns until one player manages to get four in a row of
 * the pieces of their colour, a veto move can be made instead of placing a piece but not straight
 * after another veto move. A veto variable is kept and takes the value of a column if the column
 * is vetoed, otherwise it takes the value -1, a vetocount variable is kept to reset the veto value
 * to -1 after two drops.
 *
 * The board is 'smart' and is able to evaluate the score of a given player and store a list of all
 * valid moves at any given state and a list of moves played to date.
 *
 * @author Nick
 * Student Number 11463982
 */
public class Board {
    int[][] grid;
    final static int RED = 1, YELLOW = -1, VOID = 0;
    private int veto, vetocount;
    ArrayList<String> validMoves;
    Stack<String> movelist;

    //constructor
    public Board() {
        grid = new int[5][7];
        wipe_board();
        veto = -1;
        vetocount = 0;
        movelist = new Stack<String>();
        updateValidMoves();
    }

    //sets board to void, could be useful to introduce a new game function
    private void wipe_board() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                grid[i][j] = VOID;
            }
        }
    }

    //stalemate is detected if no moves can be played but neither player has won
    public boolean stalemate() {
        if (validMoves.isEmpty()) return true;
        return false;
    }

    //make a move, return false if the move is invalid
    boolean makemove(String s, int player) {
        if (!validMoves.contains(s)) return false;
        movelist.push(s);
        char c = s.charAt(0);
        int col = Character.getNumericValue(s.charAt(1)) - 1; //moves labelled 1-7 not 0-6
        switch (c) {
            case 'v':
            case 'V': {
                return this.veto(col);
            }
            case 'd':
            case 'D': {
                return this.drop(col, player);
            }
            default: {
                return false;
            }
        }
    }

    //removes most recent move, generally this will only be called by the AI's search methods
    void unmakeMove() {
        String s = movelist.pop();
        char c = s.charAt(0);
        int col = Character.getNumericValue(s.charAt(1)) - 1;
        switch (c) {
            case 'v':
            case 'V': {
                vetocount = 0;
                veto = -1;
                break;
            }
            case 'd':
            case 'D': {
                for (int i = 0; i < 5; i++) {
                    if (grid[i][col] != VOID) {
                        grid[i][col] = VOID;
                        break;
                    }
                }
            }
            default: {
                break;
            }
        }
        updateValidMoves();
    }

    //drops a piece of a certain colour, returns false if the column is full or vetoed
    public boolean drop(int column, int player) {
        //check for vetoed column, reset veto if necesary
        if (column == veto) return false;
        if (vetocount > 0) {
            --vetocount;
            if (vetocount == 1) veto = -1;
        }
        for (int i = 4; i >= 0; i--) {
            if (grid[i][column] == VOID) {
                grid[i][column] = player;
                updateValidMoves();
                return true;
            }
        }
        return false;
    }

    //gives player the option to veto a column
    public boolean veto(int column) {
        if (vetocount != 0) return false;
        else veto = column;
        vetocount = 2;
        updateValidMoves();
        return true;
    }

    /*
     * Updates a list of all possible valid moves, any column can be vetoed if there has been no veto
     * in the last two turns, any column can be dropped into if it is not vetoed and not full. There is
     * a maximum of 14 possible moves.
     */
    public void updateValidMoves() {
        validMoves = new ArrayList<String>();
        int score = getScore(RED, true);
        if (score > 1800 || score < -1800) return;//no moves are available if one player has won
        else {
            String s = "d";
            for (int j = 0; j < 7; j++)
                if (j != veto && grid[0][j] == VOID) validMoves.add(s + (j + 1));
            //moves are labelled 1-7 but columns are 0-6 so we add 1 to j
            if (vetocount == 0) {
                s = "v";
                for (int j = 1; j < 8; j++)
                    validMoves.add(s + (j));
            }
        }
    }

    public ArrayList<String> validMoves() {
        return validMoves;
    }

    /*
     *  This function looks for runs of the player's colour, a run of two pieces is worth
     *  5 points with a space before or after, a run of three is worth 50 if it has a space before or after.
     *  A winning run is 2000. An arraylist is kept for positions used for type of
     *  run to avoid counting duplicates, with positions stored by the unique identifier i*10+j
     *  Guards are set when checking whether the edge of the board will be crossed (e.g if(j+k>6) break for
     *  horizontal runs) to avoid index out of bounds errors..
     *  A boolean paramater neg specifies whether the opponents score is negated or not, this
     *  is generally initially true initially then  set to false when calculating the opponents score for
     *  subtraction
     */
    public int getScore(int player, boolean neg) {
        ArrayList<Integer> vertical = new ArrayList<Integer>();
        ArrayList<Integer> horizontal = new ArrayList<Integer>();
        ArrayList<Integer> diagonalright = new ArrayList<Integer>();
        ArrayList<Integer> diagonalleft = new ArrayList<Integer>();
        boolean spacebefore = false;
        int score = 0, run;
        for (int i = 4; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (grid[i][j] == player) {
                    if (i > 2) { //check for vertical runs
                        if (!vertical.contains(i * 10 + j)) {  // multiply by 10 to encode position
                            vertical.add(i * 10 + j);
                            run = 1;
                            for (int k = 1; k < 4; k++) {
                                if (grid[i - k][j] == VOID) break;
                                else if (grid[i - k][j] == -player) {
                                    run = 1;
                                    break;
                                } else {
                                    vertical.add((i - k) * 10 + j);
                                    run++;
                                }
                            }
                            switch (run) {
                                case 2:
                                    score += 5;
                                    break;
                                case 3:
                                    score += 50;
                                    break;
                                case 4:
                                    score += 2000;
                                    return score;
                                default:
                                    break;
                            }
                        }
                    }
                    if (!horizontal.contains(i * 10 + j)) {    //check for horizontal runs
                        if (j > 0) {
                            if (grid[i][j - 1] == VOID) spacebefore = true;
                        }
                        horizontal.add(i * 10 + j);
                        run = 1;
                        for (int k = 1; k < 4; k++) {
                            if (j + k > 6) { //break if we are about to cross the edge of the board
                                if (!spacebefore) run = 1;
                                break;
                            } else if (grid[i][j + k] != player) {
                                if (!spacebefore && (grid[i][j + k] != VOID))
                                    run = 1;
                                break;
                            }
                            horizontal.add(i * 10 + (j + k));
                            run++;
                        }
                        switch (run) {
                            case 2:
                                score += 5;
                                break;
                            case 3:
                                score += 50;
                                break;
                            case 4:
                                score += 2000;
                                return score;
                            default:
                                break;
                        }

                    }
                    spacebefore = false;
                    if (!diagonalright.contains(i * 10 + j)) { //check for diagonal runs to the right
                        if (j > 0 && i < 4) {
                            if (grid[i + 1][j - 1] == VOID) spacebefore = true;
                        }
                        diagonalright.add(i * 10 + j);
                        run = 1;
                        for (int k = 1; k < 4; k++) {
                            if (i - k < 0 || j + k > 6) { //break if we are about to cross the edge of the board
                                if (!spacebefore) run = 1;
                                break;
                            } else if (grid[i - k][j + k] != player) {
                                if (!spacebefore && (grid[i - k][j + k] != VOID)) run = 1;
                                break;
                            }
                            diagonalright.add((i - k) * 10 + (j + k));
                            run++;
                        }
                        switch (run) {
                            case 2:
                                score += 5;
                                break;
                            case 3:
                                score += 50;
                                break;
                            case 4:
                                score += 2000;
                                return score;
                            default:
                                break;
                        }
                    }
                    spacebefore = false;
                    if (!diagonalleft.contains(i * 10 + j)) { //check for diagonal runs to the left
                        if (j < 6 && i < 4) {
                            if (grid[i + 1][j + 1] == VOID) spacebefore = true;
                        }
                        diagonalleft.add(i * 10 + j);
                        run = 1;
                        for (int k = 1; k < 4; k++) {
                            if (i - k < 0 || j - k < 0) {
                                if (!spacebefore) run = 1;
                                break;
                            } else if (grid[i - k][j - k] != player) {
                                if (!spacebefore && (grid[i - k][j - k] != VOID)) run = 1;
                                break;
                            }
                            diagonalright.add((i - k) * 10 + (j - k));
                            run++;
                        }
                        switch (run) {
                            case 2:
                                score += 5;
                                break;
                            case 3:
                                score += 50;
                                break;
                            case 4:
                                score += 2000;
                                return score;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        if (neg) score -= getScore(-player, false);
        return score;
    }

    //print state of the board
    public String toString() {
        String s = "";
        for (int i = 0; i < 5; i++) {
            s += "|";
            for (int j = 0; j < 7; j++) {
                if (grid[i][j] == VOID) s += " |";
                else if (grid[i][j] == RED) s += "r|";
                else s += "y|";
            }
            s += "\n";
        }
        return s;
    }

    //return vetoed column
    public int veto() {
        return veto;
    }
}
