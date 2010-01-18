
import gki.game.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyComputerPlayer<MoveT> implements IComputerPlayer<MoveT> {

    private Double best = null;
    private MoveT bestMove = null;
    private IGameEvaluator<MoveT> evaluator;
    private IStdGame<MoveT> game;

    public String getLastMoveDescription() {
        return "best: " + best + " bestMove: " + bestMove;
    }

    public MoveT findBestMove(IStdGame<MoveT> game, int maxDepth,
            IGameEvaluator<MoveT> evaluator) {
        //++++++++++++++++++++++++++++++++++++
        // student begin
        this.evaluator = evaluator;
        this.game = game;
        try {
            //Todo: bessere Strategie, beste erreichbare Bewertung genauer setzen
            //simpel: ersten gefundenen Zug durchführen
            bestMove = NegaMax(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxDepth - 1);
        } catch (Exception ex) {
            Logger.getLogger(MyComputerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bestMove;
        // student end
        //+++++++++++++++++++++++++++++++++
    }

    public MoveT NegaMax(Double alpha, Double beta, int maxDepth) throws Exception {

        List<Node<MoveT>> queue = new LinkedList<Node<MoveT>>();
        // add the root
        Node<MoveT> root = new Node(game, (maxDepth - 1));
        queue.add(root);

        Node<MoveT> node = null;
        while(!queue.isEmpty()){
            Main.expandedNodes++;
            node = queue.remove(0);

            // already a value
            if(node.getValue() != null){
                // this node is already maxed and the value can be pushed up
                Node<MoveT> parent = node.getParent();
                if(parent != null){
                    // check if we need to invert
                    if(parent.isPlayer1sTurn() != node.isPlayer1sTurn()){
                        parent.maxValue(-node.getValue(), node.getMove());
                    } else {
                        parent.maxValue(node.getValue());
                    }
                }
            } else {
                //try to get the value

                // reached a leaf ?
                if (node.getDepth() == 0 || !node.getPossibleMoves().iterator().hasNext()) {
                    if (node.isPlayer1sTurn()) {
                        node.maxValue(evaluator.evaluateGame(node), null);
                    } else {
                        node.maxValue(-evaluator.evaluateGame(node), null);
                    }
                } else {
                    // go deeper until leaf
                    // also add this node to the queue again to get its maximum later
                    queue.add(0, node);

                    Node<MoveT> child = null;
                    bestMove = null;

                    for (MoveT move : node.getPossibleMoves()) {
                        child = new Node(node, node.getDepth() - 1, move);
                        //   System.out.println("from "+move+" down to "+node+"\t at depth "+depth+"\t("+alpha+", "+beta+")\t go down");
                        queue.add(0, child);
                    }
                }
            }
        }
        // last node is also the first Node
        return node.getBestMove();
    }
}
