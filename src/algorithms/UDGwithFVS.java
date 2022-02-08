package algorithms;
import java.util.ArrayList;

import udg.UDG;
import udg.Vertex;

public class UDGwithFVS extends UDG { // feedback vertex set
	
  public UDGwithFVS(ArrayList<Vertex> points) {
    super(points);
    this.shouldContinueGreedy         = (currentSolution,rest)           -> { return !this.isSolution.method(currentSolution); };
    this.toRemoveBeforeContinueGreedy = (pointsToRemove)                 -> { return new UDG(pointsToRemove);                  };
    this.isSolution                   = (solutionCandidat)               -> { return this.hasAsFVS(solutionCandidat);          };
    this.shouldTryToReplace2Points    = (Vertex p1, Vertex p2)           -> { return true;                                     }; /// ?
    this.shouldTryToReplace3Points    = (Vertex p1, Vertex p2, Vertex p3)-> { return true;                                     };
  }

  public UDG fvs() {
    UDG fvs = this.greedyAlgo();
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToRemovePoints); 
    // this.solution = tryToReplace2by1((UDP)this); // 50 points -> 11, 12, 11, 10, 11
    // this.solution = repeatNtimes(2,(UDP)this,this.tryToReplace2by1); // long
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToReplace2by1s); // too long
    // this.solution = tryToReplace3by2((UDP)this); // too long, 50 points -> 13, 13, 13, 13, 13

    // ArrayList<UDG> cycles = elementaryCycles();
    //allCycles();
	//System.out.println("cycles : "+this.cyclesToString());
    return fvs;	  
  }
  
  public UDG FVSbafnaBermanFujito() {
	UDG clone = this.clone();
	UDG C;
	clone.cleanup();
	int i=0;
    while(clone.vertices.size()>0) {
      i++;
      if((C=clone.anySemidisjointCycle())!=null) {
    	int gamma = 1; //C.degree(C.vertexOfMinDegree());
    	
      }
    }
	
	return null;	  
  }
}