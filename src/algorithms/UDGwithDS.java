package algorithms;
import java.util.ArrayList;

import udg.UDG;
import udg.Vertex;

public class UDGwithDS extends UDG { // dominating set

  public UDGwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)     -> { return hasAsDS(solutionCandidat); }; 
    this.shouldContinueGreedy         = (currentSolution,rest) -> { return rest.isNotEmpty();}; // ok
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)       -> { return new UDG(neighborhoodWithCentralPoint(pointsToRemove).vertex);}; 
    this.willTryToReplace2Points      = (Vertex p1, Vertex p2) -> { return p1.distance(p2)<4*edgeThreshold; }; // ?

    this.willTryToReplace3Points      = (Vertex p1, Vertex p2, Vertex p3)-> { // ?
      return    (p1.distance(p2)<4*edgeThreshold  && p2.distance(p3)<4*edgeThreshold) 
    	     || (p2.distance(p3)<4*edgeThreshold  && p3.distance(p1)<4*edgeThreshold) 
    	     ||	(p3.distance(p1)<4*edgeThreshold  && p1.distance(p2)<4*edgeThreshold); 
    }; 
  }

  public UDG ds() {
    UDG ds = greedyAlgo(); // 94

    // ds = this.tryToReplace3by2(ds); // too long

    // ds = this.tryToReplace2by1(ds); // greedy-> replace2by1 -> 81, 83, 81
    ds = repeatWhileCanDoBetter(ds,this.tryToReplace2by1); // greedy-> repeat replace2by1 -> 79

    // ds = this.tryToRemovePoints(ds); // greedy -> remove -> 93, 95, ...
    ds = repeatWhileCanDoBetter(ds,this.tryToRemovePoints); // greedy -> repeat remove -> 91

    return ds;
  }
}