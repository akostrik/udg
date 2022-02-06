package algorithms;
import java.util.ArrayList;

import udp.UDP;
import udp.Vertex;

public class UDPwithDS extends UDP { // dominating set

  public UDPwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)               -> { return hasAsDS(solutionCandidat); }; 
// à refaire:
    this.shouldContinueGreedy         = (currentSolution,rest)           -> { return !this.isSolution.method(currentSolution);};
    this.toRemoveBeforeContinueGreedy = (pointToRemove)                  -> { return new UDP(pointToRemove);};
    this.willTryToReplaceTwoPoints    = (Vertex p1, Vertex p2)           -> { return true; }; 
    this.willTryToReplaceThreePoints  = (Vertex p1, Vertex p2, Vertex p3)-> { return true; };
  }

  public UDP ds() {
    UDP solution = greedyAlgo();
    // solution = this.replace2by1(solution); // too long

    // solution = repeatWhileCanDoBetter(this,(surrentSolution)-> { return this.tryToRemovePoints(surrentSolution);});
    //solution = localSearch(solution,this.funcInterf);
    return solution;
  }
}