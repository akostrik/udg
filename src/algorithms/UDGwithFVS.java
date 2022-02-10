package algorithms;
import java.util.ArrayList;
import java.util.Stack;

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
    // get first solution:
	// UDG fvs = this.greedyAlgo();
    UDG fvs = FVSbafnaBermanFujito();    

    // optimize the first solution: 
	// this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToRemovePoints); 
    // this.solution = tryToReplace2by1((UDP)this); // 
    // this.solution = repeatNtimes(2,(UDP)this,this.tryToReplace2by1); // long
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToReplace2by1s); // too long
    // this.solution = tryToReplace3by2((UDP)this); // too long

    return fvs;	  
  }
  
  public UDG FVSbafnaBermanFujito() { 
	UDG C=null;
	Stack<Vertex> stack = new Stack<Vertex>();
	setWeightOfAllVertices(1); // for an unweighted graph set weight=1 for all vertices

	UDG V = this.clone();
	UDG F  = new UDG();
	V.cleanup();

	while(V.size()>0) {
   	  System.out.println("V.size() = "+V.size());
	  C=V.anySemidisjointCycle();
      if(C.size()>0) {
   	    System.out.println("C = "+C.toStringWithWightDegrees());
        double gamma = C.minWeight();
    	for(Vertex p : C.vertices)
    	  p.weight -= gamma; 
      }
      else {
      	double gamma = V.minExpressionForThisAlgo();
      	for(Vertex p : V.vertices) 
   	      p.weight -= gamma*(V.degree(p)-1);
      }
      for(int i=0; i<V.size(); i++) {
    	Vertex p = V.get(i);
    	if(p.weight==0) {
    	  F.add(p);
          V.remove(p);
          stack.push(p);
          i--;
    	}
     	System.out.println();
      }
  	  V.cleanup();
    }

    while(stack.size()>0) {
      Vertex p = stack.pop();
      if(this.hasAsFVS(F.withoutOnePoint(p)))
    	F.remove(p);
    }
    
	return F;	  
  }
}