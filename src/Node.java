
import gki.game.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author konrad
 */
public class Node<MoveT> {

    private IStdGame<MoveT> state;
    private MoveT move;
    private int depth;
    private MoveT bestMove;

    public Node(IStdGame<MoveT> original, MoveT move, int depth) {
        this.state = original.copy();
        this.move = move;
        this.depth = depth;

        try {
            if (move != null) {
                // only at the root the move == null
                state.doMove(move);
            }
        } catch (Exception ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * use maxN:
     *  - just maximize the player whose turn it is
     *
     * @param evaluator
     * @param maxDepth
     * @return
     */
    public MoveT getBestMove(IGameEvaluator<MoveT> evaluator, int maxDepth) {
        Double value = null;
        Node<MoveT> node = null;
        Double best = null;
        bestMove = null;

        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, m, depth + 1);
            value = node.getValue(evaluator, maxDepth);
            //	System.out.println("MOVE: "+move+"\tvalue: "+value);

            if ((best == null) || (state.isPlayer1sTurn() && value > best) ||
                    (!state.isPlayer1sTurn() && value < best)) {
                bestMove = m;
                best = value;
            }

        }

        return bestMove;

    }

    public Double getValue(IGameEvaluator<MoveT> evaluator, int maxDepth) {
        if (depth >= maxDepth) {
            return evaluator.evaluateGame(state);
        } else {
            if(bestMove == null){
                bestMove = getBestMove(evaluator, maxDepth);
            }
            //System.out.println("best move at depth "+depth+" is "+bestMove);
            Node<MoveT> next = new Node(state, bestMove, depth+1);
            return next.getValue(evaluator, maxDepth);
        }
    }

    // for getting the result while searching we need to recover the move
    public MoveT getMove() {
        return move;
    }

    // checking the max depth
    public int getDepth() {
        return depth;
    }
}
