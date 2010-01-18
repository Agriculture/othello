
import gki.game.*;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.OthelloMove;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author konrad
 */
public class Node<MoveT> implements Comparable {

    private IStdGame<MoveT> state;
    private IGameEvaluator<MoveT> evaluator;
    private MoveT move;
    private int depth;
    private MoveT bestMove;

    public Node(IStdGame<MoveT> original, IGameEvaluator<MoveT> evaluator, MoveT move, int depth) {
        this.state = original.copy();
        this.move = move;
        this.depth = depth;
        this.evaluator = evaluator;


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

    public Double getValue() {
        // NegaMax

        return NegaMax(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        /*        if(state.isPlayer1sTurn()){
        return max(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
        return min(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
         */    }

    public Double max(Double alpha, Double beta) {
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
            return evaluator.evaluateGame(state);
        }

        Double value = null;
        Node<MoveT> node = null;
        // alpha is best for maximizing
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, evaluator, m, depth - 1);
            //  System.out.println("from "+move+" down to "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = node.min(alpha, beta);
            //System.out.println("MOVE: "+m+"\t\t at depth "+depth+"\t("+alpha+", "+beta+")\tvalue: "+value);

            if (bestMove == null) {
                bestMove = m;
            }
            if (value >= beta) {
//                bestMove = m;
                return beta;
            }

            if (value > alpha) {
                bestMove = m;
                alpha = value;
            }
        }

        return alpha;
    }

    public Double NegaMax(Double alpha, Double beta) {
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
            if (state.isPlayer1sTurn()) {
                return evaluator.evaluateGame(state);
            } else {
                return -evaluator.evaluateGame(state);
            }
        }

        Double value = null;
        Node<MoveT> node = null;
        bestMove = null;
        // get the next moves
/*        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        for (MoveT m : state.getPossibleMoves()) {
        queue.add(new Node(state, evaluator, m, depth - 1));
        }
        
        while(!queue.isEmpty()){
        node = queue.poll();
         */
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, evaluator, m, depth - 1);
            Main.expandedNodes++;
            //   System.out.println("from "+move+" down to "+node+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = -node.NegaMax(-beta, -alpha);

            if (bestMove == null) {
                bestMove = node.getMove();
            }

            if (value >= beta) {
                Main.prunedNodes++;
                return beta;
            }

            if (value > alpha) {
                bestMove = node.getMove();
                alpha = value;
            }
        }

        return alpha;
    }

    public Double min(Double alpha, Double beta) {
        if (depth == 0 || !state.getPossibleMoves().iterator().hasNext()) {
            return evaluator.evaluateGame(state);
        }

        Double value = null;
        Node<MoveT> node = null;
        // beta is the best possible for minimize
        bestMove = null;
        // get the next moves
        for (MoveT m : state.getPossibleMoves()) {
            node = new Node(state, evaluator, m, depth - 1);
            //  System.out.println("from "+move+" down to "+m+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
            value = node.max(alpha, beta);
            //System.out.println("MOVE: "+m+"\t\t at depth "+depth+"\t("+alpha+", "+beta+")\tvalue: "+value);
            if (bestMove == null) {
                bestMove = m;
            }
            if (value <= alpha) {
//                bestMove = m;
                return alpha;
            }

            if (value < beta) {
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

    public IStdGame<MoveT> getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Node " + move + " at depth " + depth;
    }

    /**
     * not used
     * @param arg0
     * @return
     */
    public int compareTo(Object arg0) {
        Node o = (Node) arg0;
        Double a;
        if (state.isPlayer1sTurn()) {
            a = -evaluator.evaluateGame(state);
        } else {
            a = evaluator.evaluateGame(state);
        }
        Double b;
        if (o.getState().isPlayer1sTurn()) {
            b = -evaluator.evaluateGame(o.getState());
        } else {
            b = evaluator.evaluateGame(o.getState());
        }
        return -a.compareTo(b);
    }
}
