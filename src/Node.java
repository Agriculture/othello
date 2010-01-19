
import gki.game.*;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.OthelloGame;
import othello.base.OthelloMove;

/**
 *  Adapter for IStdGame
 * @author konrad
 */
public class Node extends OthelloGame{
    private OthelloGame adaptee;
    private Double alpha = null;
    private Double beta = null;
    private Double value = null;
    private Node parent = null;
    private OthelloMove move = null;
    private OthelloMove bestMove = null;
    private int depth;

    /**
     * only for the root
     * @param adaptee
     */
    public Node(OthelloGame adaptee, int depth){
		super();
        this.adaptee = adaptee;
        this.depth = depth;
    }

    public Node(Node parent, int depth, OthelloMove move) throws Exception{
		super();
        this.adaptee = parent.copy();
        this.move = move;
        this.adaptee.doMove(move);
        this.parent = parent;
		this.depth = depth;
    }

	@Override
    public OthelloGame copy() {
        return (OthelloGame) adaptee.copy();
    }

	@Override
    public boolean isPlayer1sTurn() {
        return this.adaptee.isPlayer1sTurn();
    }

	@Override
    public Winner getWinner() {
        return this.adaptee.getWinner();
    }

	@Override
	public PlayerColor getFieldColor(int x, int y) {
		return adaptee.getFieldColor(x, y);
	}

	public Iterable<OthelloMove> getPossibleMoves() {
    	return this.adaptee.getPossibleMoves();
    }

    public void doMove(OthelloMove arg0) throws Exception {
        adaptee.doMove(arg0);
    }

    public void optimizeValue(Double arg, OthelloMove newMove){
//		System.out.println("maximize at depth "+depth+" value "+value+" with "+arg+" turn player1 "+this.isPlayer1sTurn());
		if(adaptee.isPlayer1sTurn()){
			//maximize
			if(value == null || arg > value){
				value = arg;
				bestMove = newMove;
			}
		} else {
			//minimize
			if(value == null || arg < value){
				value = arg;
				bestMove = newMove;
			}
		}
    }

	public OthelloMove getMove(){
		return move;
	}

    public OthelloMove getBestMove(){
        return bestMove;
    }

    public Double getValue(){
        return value;
    }

    public Node getParent(){
        return this.parent;
	}

    public int getDepth(){
        return this.depth;
    }

	@Override
	public String toString() {
		return adaptee.toString()+"\nPLAYER "+(adaptee.isPlayer1sTurn() ? "wHITE" : "BLACK");
	}


}
