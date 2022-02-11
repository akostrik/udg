package algorithms;
import java.util.ArrayList;

public class UDGwithDS extends UDG { // dominating set
  public static double epsilon = 1;         // (1+epsilon) = a bound for the local DS
  public static double ro      = 1+epsilon; // compute a DS of cardinality no more than (1+epsilon) the size of min DS 
  public static int sizeMinNiebergHurink = 40;
  
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
    if(!this.isConnected()) {
      System.out.println("input UPD must be connected ");
      return new UDG();
    }

    // get first solution:
	// UDG ds = greedyAlgo(); 
    UDG ds = dsNiebergHurink();
    
    // optimize the first solution: 
    // ds = repeatWhileCanDoBetter(ds,this.tryToReplace3by2); // too long
    // ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    // ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 

    return ds;
  }

  public UDG dsNiebergHurink() {
	//if(this.size()<sizeMinNiebergHurink) 
      //return dsLitleGraph(); 
	System.out.println("aPathOf3hopsOrLongerExiste() = "+aPathOf3hopsOrLongerExiste());
	if(!this.aPathOf3hopsOrLongerExiste()) 
      return dsLitleGraph(); 

    Vertex theMostConnected = this.theMostConnectedPoint();
    
	UDG V  = this.clone(); // rest
	ArrayList<UDG> DNi = new ArrayList<UDG>();

	return UDG.unionOf(DNi);
  }
  
  public UDG dsLitleGraph() {
	UDG ds = greedyAlgo(); 
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace3by2); 
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 
    return ds;	  
  }
}