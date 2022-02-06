package algorithms;
import java.util.ArrayList;

import udp.UDP;
import udp.Vertex;

public class UDPwithDS extends UDP { // dominating set

  public UDPwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution = (solutionCandidat) -> { return hasAsDS(solutionCandidat); }; 
  }

  public UDP ds() {
    UDP solution = null;
    // solution = greedyAlgo();
    // this.funcInterf    = (surrentSolution)-> { return this.replace2by1(surrentSolution);}; // too long

    solution = repeatWhileCanDoBetter(this,(surrentSolution)-> { return this.tryToRemovePoints(surrentSolution);});
    //this.funcInterf     = (surrentSolution)-> { return this.replace2by1(surrentSolution);};
    //solution = localSearch(solution,this.funcInterf);
    return solution;
  }
}