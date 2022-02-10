package algorithms;
import java.util.ArrayList;

public class UDGwithDS extends UDG { // dominating set
  public static double epsilon = 1;         // (1+epsilon) = a bound for the local DS
  public static double ro      = 1+epsilon; // compute a DS of cardinality no more than (1+epsilon) the size of min DS 
	
  public UDGwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)     -> { return hasAsDS(solutionCandidat); }; 
    this.shouldContinueGreedy         = (currentSolution,rest) -> { return rest.isNotEmpty();}; // ok
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)       -> { return new UDG(neighborhoodWithCentralPoint(pointsToRemove).vertices);}; 
    this.shouldTryToReplace2Points    = (Vertex p1, Vertex p2) -> { return p1.distance(p2)<4*edgeThreshold; }; /// ?
    this.shouldTryToReplace3Points    = (Vertex p1, Vertex p2, Vertex p3)-> { /// ?
      return  (p1.distance(p2)<4*edgeThreshold && p2.distance(p3)<4*edgeThreshold) && (p3.distance(p1)<4*edgeThreshold);
    }; 
  }

  public UDG ds() {
    // greedy                       -> 94
	// greedy -> remove             -> 93
	// greedy -> repeat remove      -> 91
	// greedy -> replace2by1        -> 81
	// greedy -> repeat replace2by1 -> 79

    // get first solution:
	UDG ds = greedyAlgo(); 

    // optimize the first solution: 
	// ds = this.tryToReplace3by2(ds); // too long
    // ds = this.tryToReplace2by1(ds); 
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    // ds = this.tryToRemovePoints(ds); 
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 

    return ds;
  }
  
  public UDG dsNiebergHurink() {
	UDG V  = this.clone(); // rest
	ArrayList<UDG> DNi = new ArrayList<UDG>();

	return UDG.unionOf(DNi);
  }
}