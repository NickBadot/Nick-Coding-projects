/*
 * CPUPlayer class represents the AI player in a game of connect four, the AI player uses either
 * Alpha-Beta Search or AB search enhanced with the killer heuristic to decide on a move to play.
 */

import java.util.ArrayList;
import java.util.HashMap;

public class CPUPlayer {
    int col, evals;
    Board board;
    HashMap<Integer, HashMap<String, Integer>> killmove;

    //constructor assigns the Ai a colour
    public CPUPlayer(int c) {
        col = c;
    }

    /*
     * Alphabeta search returns a move, this is the first call at the root node will store the original
     * board statically to speed up the search rather than passing a board into every recursive call. Boolean
     */
    public String abSearch(Board b, int depth, boolean verbose, boolean kill) {
        board = b;
        evals = 0;
        ABResult res;
        if (kill) {
            killmove = new HashMap<Integer, HashMap<String, Integer>>();
            res = abKH(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, col, verbose);
            if (verbose) System.out.println(killmove);
        } else res = abSearch(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, col, verbose);
        System.out.println("CPU has selected move : " + res.move() + " with " + evals + " evaluations");
        return res.move();
    }

    //alpha-beta search, creates new positions to a certain depth to evaluate strength of a move
    public ABResult abSearch(int depth, double alpha, double beta, int player, boolean verbose) {
        ABResult temp, bestres = new ABResult(-1000000);
        ArrayList<String> moves = board.validMoves();
        if (depth == 0 || moves.isEmpty()) { //if moves list is empty the game is over
            evals++;
            if (verbose) System.out.println("Leaf evaluated, alpha = " + alpha + " ,beta = " + beta);
            return new ABResult(board.getScore(player, true));
        } else {
            for (String move : moves) {
                board.makemove(move, player);
                temp = abSearch(depth - 1, -beta, -alpha, -player, verbose);
                board.unmakeMove();
                int score = -temp.score();
                if (score >= beta) { //beta cutoff
                    if (verbose) System.out.println("Cutoff with value " + score + " at depth " + depth);
                    return new ABResult(score, move);
                }
                if (score > bestres.score()) {
                    bestres = new ABResult(score, move);
                }
                alpha = Math.max(alpha, score);
            }
            return bestres;
        }
    }

    /*
     * AlphaBeta search modified to use the killer heuristic, whereby a 'killer' move
     * that causes the most cutoff is stored and searched first for each ply
     */
    public ABResult abKH(int depth, double alpha, double beta, int player, boolean verbose) {
        HashMap<String, Integer> killmoveAtDepth;
        ABResult temp, bestres = new ABResult(-1000000);
        ArrayList<String> moves = board.validMoves();
        if (depth == 0 || moves.isEmpty()) {
            evals++;
            if (verbose) System.out.println("Leaf evaluated, alpha = " + alpha + " ,beta = " + beta);
            return new ABResult(board.getScore(player, true));
        } else {
            if (killmove.containsKey(depth)) { //if a killer move exists it must be retrieved
                killmoveAtDepth = killmove.get(depth);
                moves = reorderKM(getKillerMove(killmoveAtDepth, moves), moves);
            } else killmoveAtDepth = new HashMap<String, Integer>();
            for (String move : moves) {
                board.makemove(move, player);
                temp = abKH(depth - 1, -beta, -alpha, -player, verbose);
                board.unmakeMove();
                int score = -temp.score();
                if (score >= beta) {
                    if (verbose) System.out.println("Cutoff with value " + score + " at depth " + depth);
                    if (!killmoveAtDepth.containsKey(move)) killmoveAtDepth.put(move, 1);
                    else {    //increment the entry in killmoves if cutoff occurs
                        int c = killmoveAtDepth.get(move);
                        killmoveAtDepth.put(move, c++);
                    }
                    killmove.put(depth, killmoveAtDepth);
                    return new ABResult(score, move);
                }
                if (score > bestres.score()) {
                    bestres = new ABResult(score, move);
                }
                alpha = Math.max(alpha, score);
            }
            return bestres;

        }
    }

    //get killer move from a set of moves
    String getKillerMove(HashMap<String, Integer> kill, ArrayList<String> moves) {
        int score = 0;
        String res = "";
        for (String s : moves) {
            if (kill.containsKey(s)) {
                if (kill.get(s) > score) {
                    score = kill.get(s);
                    res = s;
                }
            }
        }
        return res;
    }

    //put killer move first
    ArrayList<String> reorderKM(String m, ArrayList<String> moves) {
        if (!moves.contains(m) || m.equals("")) return moves;
        moves.remove(m);
        moves.add(0, m);
        return moves;
    }
}
