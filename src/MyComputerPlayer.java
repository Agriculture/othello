
import gki.game.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.OthelloGame;
import othello.base.OthelloMove;

public class MyComputerPlayer<MoveT> implements IComputerPlayer<MoveT> {

    private Double best = null;
    private MoveT bestMove = null;
    private IGameEvaluator<OthelloMove> evaluator;
    private OthelloGame game;

    public String getLastMoveDescription() {
        return "best: " + best + " bestMove: " + bestMove;
    }

    public MoveT findBestMove(IStdGame<MoveT> game, int maxDepth,
            IGameEvaluator<MoveT> evaluator) {
        //++++++++++++++++++++++++++++++++++++
        // student begin
        this.evaluator = (IGameEvaluator<OthelloMove>) evaluator;
        this.game = (OthelloGame) game;
        try {
            //Todo: bessere Strategie, beste erreichbare Bewertung genauer setzen
            //simpel: ersten gefundenen Zug durchführen
            bestMove = MiniMax(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxDepth);
        } catch (Exception ex) {
            Logger.getLogger(MyComputerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bestMove;
        // student end
        //+++++++++++++++++++++++++++++++++
    }

    public MoveT MiniMax(Double alpha, Double beta, int maxDepth) throws Exception {

        List<Node> queue = new LinkedList<Node>();
        // add the root
        Node root = new Node(game, (maxDepth - 1), alpha, beta);
        queue.add(root);

        Node node = null;
		Node parent = null;
		Boolean pruning = false;
        while(!queue.isEmpty()){
            Main.expandedNodes++;
            node = queue.remove(0);
			//System.out.println("expand "+node);
	//	System.out.println("size "+queue.size());
			if(!pruning || parent != node.getParent()){
				pruning = false;

				if(node.isOptimized()){
				//	System.out.println("maxed "+node+" at depth "+node.getDepth());
					// this node is already maxed and the value can be pushed up
					parent = node.getParent();
					if(parent != null){
						if(!parent.optimizeValue(node.getValue(), node.getMove())){
							// pruning is going on -> dont calculate further under parent
							pruning = true;
						}
					}
				} else {
					// reached a leaf ?
					if ((node.getDepth() == 0 || !node.getPossibleMoves().iterator().hasNext())) {
						parent = node.getParent();
						if(parent != null){
							if(!parent.optimizeValue(evaluator.evaluateGame(node), node.getMove())){
								pruning = true;
							}
						}
					} else {
						// go deeper until leaf
						// also add this node to the queue again to get its maximum later
						queue.add(0, node);

						Node child = null;

						for (OthelloMove move : node.getPossibleMoves()) {
							child = new Node(node, node.getDepth() - 1, move, node.getAlpha(), node.getBeta());
						//	System.out.println("new child at depth "+(node.getDepth()-1)+" with move "+move);
							queue.add(0, child);
						}

					}
				}
			} else {
				Main.prunedNodes++;
			}
        }
        return (MoveT) root.getBestMove();
    }
}
