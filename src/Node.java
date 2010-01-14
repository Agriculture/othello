
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

    public Double getValue(IGameEvaluator<MoveT> evaluator) {
        // NegaMax

        return NegaMax(evaluator, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
/*        if(state.isPlayer1sTurn()){
            return max(evaluator, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
            return min(evaluator, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
*/    }
    public Double max(IGameEvaluator<MoveT> evaluator, Double alpha, Double beta){
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
           return evaluator.evaluateGame(state);
        }

        Double value = null;
        Node<MoveT> node = null;
        // alpha is best for maximizing
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, m, depth - 1);
          //  System.out.println("from "+move+" down to "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = node.min(evaluator, alpha, beta);
            //System.out.println("MOVE: "+m+"\t\t at depth "+depth+"\t("+alpha+", "+beta+")\tvalue: "+value);

            if(bestMove == null){
                bestMove = m;
            }
            if(value >= beta){
//                bestMove = m;
                return beta;
            }

            if(value > alpha){
                bestMove = m;
                alpha = value;
            }
        }

        return alpha;
    }

    public Double NegaMax(IGameEvaluator<MoveT> evaluator, Double alpha, Double beta){
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
            if(state.isPlayer1sTurn()){
                return evaluator.evaluateGame(state);
            } else {
                return -evaluator.evaluateGame(state);
            }
        }

        Double value = null;
        Node<MoveT> node = null;
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, m, depth - 1);
          //  System.out.println("from "+move+" down to "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = -node.NegaMax(evaluator, -beta, -alpha);

            if(bestMove == null){
                bestMove = m;
            }

            if(value >= beta){
                return beta;
            }

            if(value > alpha){
                bestMove = m;
                alpha = value;
            }
        }

        return alpha;
    }

    public Double min(IGameEvaluator<MoveT> evaluator, Double alpha, Double beta){
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
           return evaluator.evaluateGame(state);
        }

        Double value = null;
        Node<MoveT> node = null;
        // beta is the best possible for minimize
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, m, depth - 1);
          //  System.out.println("from "+move+" down to "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = node.max(evaluator, alpha, beta);
            //System.out.println("MOVE: "+m+"\t\t at depth "+depth+"\t("+alpha+", "+beta+")\tvalue: "+value);
            if(bestMove == null){
                bestMove = m;
            }
            if(value <= alpha){
//                bestMove = m;
                return alpha;
            }

            if(value < beta){
                beta = value;
                bestMove = m;
            }
        }

        return beta;

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
