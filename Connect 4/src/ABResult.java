/*
 * Class used to store the result of an AB search with or without Killer Heurstic
 */
public class ABResult {
    private int score;
    private String move;

    public ABResult(int s, String m) {
        score = s;
        move = m;
    }

    // we may need to just return a score at leaf nodes
    public ABResult(int s) {
        score = s;
    }

    public void addMove(String m) {
        move = m;
    }

    public int score() {
        return score;
    }

    public String move() {
        return move;
    }
}
