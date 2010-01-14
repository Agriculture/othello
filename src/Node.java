
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
    private IStdGame<MoveT> parent = null;
    private MoveT move;
    private int depth;
    private MoveT bestMove;


    public Node(IStdGame<MoveT> original, MoveT move, int depth) {
        if (depth > 0) {
            this.parent = original;
        }
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
    public MoveT getBestMove() {
        return bestMove;
    }

    public Double getValue(IGameEvaluator<MoveT> evaluator, Double alpha, Double beta) {
        // NegaMax

        if (depth == 0) {
            if(!state.isPlayer1sTurn()){
                // player 2 tries to maximize the negation of the value
                return -evaluator.evaluateGame(state);
            } else {
                return evaluator.evaluateGame(state);
            }
        }

        Double value = null;
        Node<MoveT> node = null;
        Double best = null;
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, m, depth - 1);
            System.out.println("MOVE: "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = -node.getValue(evaluator, -alpha, -beta);
            System.out.println("MOVE: "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\tvalue: "+value);

            if(value >= beta){
                bestMove = m;
                System.out.println("CUT");
                return beta;
            }
            if(value > alpha){
                bestMove = m;
                alpha = value;
            }

            if (!state.isPlayer1sTurn()) {
                value = 1 - value;
            }
            
        }

        return alpha;

    }

    // for getting the result while searching we need to recover the move
    public MoveT getMove() {
        return move;
    }

    // checking the max depth
    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "Node " + move + " at depth " + depth;
    }
}
