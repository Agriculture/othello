
import gki.game.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.*;
import othello.base.OthelloGame.PlayerColor;

public class MyOthelloEvaluator implements IGameEvaluator<OthelloMove> {

    private OthelloGame game;
    private int myCount;
    private int otherCount;
    private int myCorner;
    private int otherCorner;
    private Double myMoves;
    private Double otherMoves;

    public double evaluateGame(IStdGame<OthelloMove> gameSetting) {

        //+++++++++++++++++++++++++++++++++++++++++++
        //student begin
        discCount();

        corner();

        try {
            mobility();
        } catch (Exception ex) {
            Logger.getLogger(MyOthelloEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        double myValue = 1 / 100 * myCount + 10 * myCorner + myMoves;
        double otherValue = 1 / 100 * otherCount + 10 * otherCorner + otherMoves;

        //System.out.println("myValue "+myValue+" \tmyCount "+myCount+" \tmyCorner "+myCorner+" \tmyMoves "+myMoves);
        //System.out.println("otValue "+otherValue+" \totCount "+otherCount+" \totCorner "+otherCorner+" \totMoves "+otherMoves);

        double result = myValue / (myValue + otherValue);

        return result;
        //student end
        //+++++++++++++++++++++++++++++++++++++++++++
    }

    private void corner() {
        myCorner = 0;
        otherCorner = 0;
        for (int x = 0; x < OthelloGame.WIDTH; x += (OthelloGame.WIDTH - 1)) {
            for (int y = 0; y < OthelloGame.HEIGHT; y += (OthelloGame.HEIGHT - 1)) {
                if (game.getFieldColor(x, y) == PlayerColor.Player1) {
                    myCorner++;
                } else if (game.getFieldColor(x, y) == PlayerColor.Player2) {
                    otherCorner++;
                }
            }
        }
    }

    private void discCount() {
        myCount = 0;
        otherCount = 0;
        for (int x = 0; x < OthelloGame.WIDTH; x++) {
            for (int y = 0; y < OthelloGame.HEIGHT; y++) {
                if (game.getFieldColor(x, y) == PlayerColor.Player1) {
                    myCount++;
                } else if (game.getFieldColor(x, y) == PlayerColor.Player2) {
                    otherCount++;
                }
            }
        }
    }

    private void mobility() throws Exception {
        myMoves = 0d;
        otherMoves = 0d;
        List<Integer> comingMoves = new LinkedList<Integer>();
        IStdGame<OthelloMove> tmpGame;

        // count the moves for this state
        for (OthelloMove move : game.getPossibleMoves()) {
            if (game.isPlayer1sTurn()) {
                myMoves++;
            } else {
                otherMoves++;
            }
            // collect the coming Moves
            tmpGame = game.copy();
            tmpGame.doMove(move);
            int count = 0;
            for (OthelloMove futureMove : tmpGame.getPossibleMoves()) {
                if (game.isPlayer1sTurn() != tmpGame.isPlayer1sTurn()) {
                    count++;
                }
            }
            comingMoves.add(count);
        }

        // average over the coming Moves
        int sum = 0;
        for (Integer i : comingMoves) {
            sum += i;
        }
        Double moves = sum / new Double(comingMoves.size());
        if (game.isPlayer1sTurn()) {
            otherMoves = moves;
        } else {
            myMoves = moves;
        }

    }
}
