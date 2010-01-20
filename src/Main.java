import gki.game.*;
import java.util.Date;
import othello.base.*;

public class Main 
{
	public static int expandedNodes  = 0;
	public static int prunedNodes  = 0;

	private static void testPlayer(IGameEvaluator<OthelloMove> evaluator, MyComputerPlayer player) throws Exception
    {
 /*      try
        {
*/
            Long start = System.currentTimeMillis();
            double erg=OthelloControler.percentageWinsPlayerAgainstRnd(evaluator, player, 3, 100,true);
            System.out.println("Gewonnen: "+erg*100+" %");
            Long end = System.currentTimeMillis();
            System.out.println("time "+(end - start) +" (ms)");
            System.out.println("expandedNodes "+expandedNodes +" prunedNodes "+prunedNodes+" ("
					+(new Integer(prunedNodes*100/(expandedNodes)))+"%)");
        }
/*        catch(Exception ex)
        {
            System.err.println("Fehler: "+ex.toString());
            ex.printStackTrace(System.err);
        }
    }
*/    public static void main(String[] args) throws Exception
    {
        MyOthelloEvaluator evaluator=new MyOthelloEvaluator();
        MyComputerPlayer player=new MyComputerPlayer();
        
      //  OthelloControler.run(evaluator, player);
//       OthelloControler.runConsole(evaluator, player,3);
        testPlayer(evaluator,player);
    }
}
