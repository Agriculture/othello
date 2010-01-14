
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
    // for pruning
    private Double min;
    private Double max;

    public Node(IStdGame<MoveT> original, MoveT move, int depth) {
        if(depth > 0){
            this.parent = original;
        }
        this.state = original.copy();
        this.move = move;
        this.depth = depth;

        this.min = 0d;
        this.max = 0d;

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
            // calculate
			node = new Node(state, m, depth + 1);
            value = node.getValue(evaluator, maxDepth);
			// update the bound
			if(state.isPlayer1sTurn()){
				if(value < min){
					min = value;
				}
			} else {
				if(best > max){
					max = value;
				}
			}

			if(!state.isPlayer1sTurn()){
				value = 1 - value;
			}
            //	System.out.println("MOVE: "+move+"\tvalue: "+value);

            if ((best == null) || (value > best)){
                bestMove = m;
                best = value;
            }

        }
		if(best != null){
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
         //   System.out.println(this);
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

    // for checking while pruning
    public Double getmin(){
        return min;
    }
    public Double getmax(){
        return max;
    }

    @Override
    public String toString() {
        return "Node "+move+" at depth "+depth+" with bounds ("+min+", "+max+")";
    }
}
