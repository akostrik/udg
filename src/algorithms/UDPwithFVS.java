package algorithms;
import java.util.ArrayList;

import udp.UDP;
import udp.Vertex;

public class UDPwithFVS extends UDP { // feedback vertex set
	
  public UDPwithFVS(ArrayList<Vertex> points) {
    super(points);
    this.shouldContinueGreedy         = (currentSolution,rest)  -> { return !this.isSolution.method(currentSolution);};
    this.toRemoveBeforeContinueGreedy = (pointToRemove)         -> { return new UDP(pointToRemove);};
    this.isSolution                   = (solutionCandidat)      -> { return this.hasAsFVS(solutionCandidat); };
    this.willTryToReplaceTwoPoints    = (Vertex p1, Vertex p2)  -> { return true;}; // try to replace any pair of point by another one
    this.willTryToReplaceThreePoints  = (Vertex p1, Vertex p2, Vertex p3)-> {return true;};
  }
  
  public UDP fvs() {
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToRemovePoints); 
    // this.solution = tryToReplace2by1((UDP)this); // 50 points -> 11, 12, 11, 10, 11
    // this.solution = repeatNtimes(2,(UDP)this,this.tryToReplace2by1); // long
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToReplace2by1s); // too long
    // this.solution = tryToReplace3by2((UDP)this); // too long, 50 points -> 13, 13, 13, 13, 13
    // this.solution = this.greedyAlgo();

    UDP.cycles = elementaryCycles();
    allCycles();
	System.out.println("cycles : "+UDP.cyclesToString());
    return null;	  
  }
}