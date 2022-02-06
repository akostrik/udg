package algorithms;
import java.util.ArrayList;

import udp.UDP;
import udp.Vertex;

public class UDPwithDS extends UDP { // dominating set

  public UDPwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)               -> { return hasAsDS(solutionCandidat); }; 
    this.shouldContinueGreedy         = (currentSolution,rest)           -> { return rest.isNotEmpty();}; // ok
    this.toRemoveBeforeContinueGreedy = (pointToRemove)                  -> { return new UDP(neighborhoodWithCentralPoint(pointToRemove).vertex);}; ///
    this.willTryToReplaceTwoPoints    = (Vertex p1, Vertex p2)           -> { return true; }; // ?
    this.willTryToReplaceThreePoints  = (Vertex p1, Vertex p2, Vertex p3)-> { return true; }; // ?
  }

  public UDP ds() {
    UDP ds = greedyAlgo();
    // ds = this.replace2by1(ds); // too long
	ds = this.tryToReplace2by1(ds); // too long


    // ds = repeatWhileCanDoBetter(this,(surrentSolution)-> { return this.tryToRemovePoints(surrentSolution);});
    // ds = localSearch(ds,this.funcInterf);
    return ds;
  }
}