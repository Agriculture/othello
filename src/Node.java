
import gki.game.*;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.OthelloMove;

/**
 *  Adapter for IStdGame
 * @author konrad
 */
public class Node<MoveT> implements IStdGame<MoveT>{
    private IStdGame<MoveT> adaptee;
    private Double alpha = null;
    private Double beta = null;
    private Double value = null;
    private Node<MoveT> parent = null;
    private MoveT move = null;
    private MoveT bestMove = null;
    private int Depth;

    /**
     * only for the root
     * @param adaptee
     */
    public Node(IStdGame<MoveT> adaptee, int depth){
        this.adaptee = adaptee;
        this.Depth = depth;
    }

    public Node(Node<MoveT> parent, int depth, MoveT move) throws Exception{
        this.adaptee = parent.copy();
        this.move = move;
        this.adaptee.doMove(move);
        this.parent = parent;
    }

    public IStdGame<MoveT> copy() {
        return adaptee.copy();
    }

    public boolean isPlayer1sTurn() {
        return this.adaptee.isPlayer1sTurn();
    }

    public Winner getWinner() {
        return this.adaptee.getWinner();
    }

    public Iterable<MoveT> getPossibleMoves() {
        return this.adaptee.getPossibleMoves();
    }

    public void doMove(MoveT arg0) throws Exception {
        adaptee.doMove(arg0);
    }

    public void maxValue(Double arg, MoveT newMove){
        if(value == null || arg > value){
            value = arg;
            bestMove = newMove;
        }
    }

    public MoveT getBestMove(){
        return bestMove;
    }

    public Double getValue(){
        return value;
    }

    public void setParent(Node<MoveT> parent){
        this.parent = parent;
    }

    public Node<MoveT> getParent(){
        return this.parent;
    }

    public void setDepth(int depth){
        this.Depth = depth;
    }

    public int getDepth(){
        return this.Depth;
    }

}
