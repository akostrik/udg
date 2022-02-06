package algorithms;
import java.util.ArrayList;

import udp.UDP;
import udp.Vertex;

public class UDPwithDS extends UDP { // dominating set

  public UDPwithDS(ArrayList<Vertex> vertex) {
    super(vertex);
  }

  public UDP ds() {
    return strategy();
  }
  
  public UDP strategy() {
	  UDP solution = null;
    // solution = greedyAlgo();
    // this.funcInterf    = (surrentSolution)-> { return this.replace2by1(surrentSolution);}; // very long

    solution = repeatWhileCanDoBetter(this,(surrentSolution)-> { return this.tryToRemovePoints(surrentSolution);});
    //this.funcInterf     = (surrentSolution)-> { return this.replace2by1(surrentSolution);};
    //solution = localSearch(solution,this.funcInterf);
    return solution;
  }
  
  public boolean isSolution(UDP solutionToVerify) { 
	if(solutionToVerify.isEmpty()) return false;
    for(Vertex p : vertex) {
      boolean pointIsVisited=false;
      for(Vertex pds : solutionToVerify.vertex)  
	    if (isEdgeOrEqualPoints(p,pds)) 
	      pointIsVisited=true;
      if(!pointIsVisited)
        return false;
    }
   return true;
  }
}