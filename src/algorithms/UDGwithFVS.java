package algorithms;
import java.util.ArrayList;

import udg.UDG;
import udg.Vertex;

public class UDGwithFVS extends UDG { // feedback vertex set
	
  public UDGwithFVS(ArrayList<Vertex> points) {
    super(points);
    this.shouldContinueGreedy         = (currentSolution,rest)           -> { return !this.isSolution.method(currentSolution); };
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)                 -> { return new UDG(pointsToRemove);                   };
    this.isSolution                   = (solutionCandidat)               -> { return this.hasAsFVS(solutionCandidat);          };
    this.shouldTryToReplace2Points    = (Vertex p1, Vertex p2)           -> { return true;                                     }; 
    this.shouldTryToReplace3Points    = (Vertex p1, Vertex p2, Vertex p3)-> { return true;                                     };
  }
  
  public UDG fvs() {
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToRemovePoints); 
    // this.solution = tryToReplace2by1((UDP)this); // 50 points -> 11, 12, 11, 10, 11
    // this.solution = repeatNtimes(2,(UDP)this,this.tryToReplace2by1); // long
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToReplace2by1s); // too long
    // this.solution = tryToReplace3by2((UDP)this); // too long, 50 points -> 13, 13, 13, 13, 13
    // this.solution = this.greedyAlgo();

    UDG.cycles = elementaryCycles();
    allCycles();
	System.out.println("cycles : "+UDG.cyclesToString());
    return null;	  
  }
}