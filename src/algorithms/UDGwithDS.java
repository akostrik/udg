package algorithms;
import java.util.ArrayList;

public class UDGwithDS extends UDG { // dominating set

  public UDGwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)     -> { return hasAsDS(solutionCandidat); }; 
    this.shouldContinueGreedy         = (currentSolution,rest) -> { return rest.isNotEmpty();}; // ok
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)       -> { return new UDG(neighborhoodWithCentralPoint(pointsToRemove).vertices);}; 
    this.shouldTryToReplace2Points = (Vertex p1, Vertex p2)    -> { return p1.distance(p2)<4*edgeThreshold; }; /// ?
    this.shouldTryToReplace3Points = (Vertex p1, Vertex p2, Vertex p3)-> { /// ?
      return  (p1.distance(p2)<4*edgeThreshold  && p2.distance(p3)<4*edgeThreshold) && (p3.distance(p1)<4*edgeThreshold);
    }; 
  }

  public UDG ds() {
    // greedy                       -> 94
	// greedy -> remove             -> 93
	// greedy -> repeat remove      -> 91
	// greedy -> replace2by1        -> 81
	// greedy -> repeat replace2by1 -> 79

	UDG ds = greedyAlgo(); 

	// optimisations:
	// ds = this.tryToReplace3by2(ds); // too long
    // ds = this.tryToReplace2by1(ds); 
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); 
    // ds = this.tryToRemovePoints(ds); 
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); 

    return ds;
  }
}