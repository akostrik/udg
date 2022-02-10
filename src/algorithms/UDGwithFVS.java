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
    // UDG fvs = this.greedyAlgo();
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToRemovePoints); 
    // this.solution = tryToReplace2by1((UDP)this); // 50 points -> 11, 12, 11, 10, 11
    // this.solution = repeatNtimes(2,(UDP)this,this.tryToReplace2by1); // long
    // this.solution = repeatWhileCanDoBetter((UDP)this,this.tryToReplace2by1s); // too long
    // this.solution = tryToReplace3by2((UDP)this); // too long, 50 points -> 13, 13, 13, 13, 13

    UDG fvs = FVSbafnaBermanFujito();    
    return fvs;	  
  }
  
  public UDG FVSbafnaBermanFujito() { // unweighted version, weight=1 for all vertices
	UDG C=null;
	Stack<Vertex> stack = new Stack<Vertex>();
	setWeightOfAllVertices(1); 

	//UDG G = this.clone();
	UDG V = this.clone();
	UDG F  = new UDG();
	//G.cleanup(); 
	V.cleanup();

    //System.out.println("V = "+V.toStringWithWightDegrees() );
    //System.out.println("F = "+F.toStringWithWightDegrees() );
	while(V.size()>0) {
   	  System.out.println("V.size() = "+V.size());
	  //System.out.println("*******");
	  C=V.anySemidisjointCycle();
      if(C.size()>0) {
   	    System.out.println("C = "+C.toStringWithWightDegrees());
        double gamma = C.minWeight();
   	    System.out.println("gamma = "+gamma);
    	for(Vertex p : C.vertices) {
    	  p.weight -= gamma; ///
    	  //if(V.contains(p)) V.vertices.get(V.vertices.indexOf(p)).weight -= gamma;
    	}
        //System.out.println("V = "+V.toStringWithWightDegrees() );
      }
      else {
    	//System.out.println("нету sjCycles");
      	double gamma = V.minExpressionForThisAlgo();
      	for(Vertex p : V.vertices) 
   	      p.weight -= gamma*(V.degree(p)-1);
        //System.out.println("V =     "+V.toStringWithWightDegrees() );
      }
   	  //System.out.println("сейчас будем переносить циклом");
      //System.out.println("V     = "+V.toStringWithWightDegrees() );
   	  //System.out.println("F     = "+F.toStringWithWightDegrees() );
   	  //System.out.println("stack = "+stack.toString());
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
   	  //System.out.println("циклом перенесли");
      //System.out.println("V = "+V.toStringWithWightDegrees() );
   	  //System.out.println("F = "+F.toStringWithWightDegrees() );
   	  //System.out.println("stack = "+stack.toString());
      //G.cleanup(); 
  	  V.cleanup();
  	  //System.out.println("сделали cleanup");
      //System.out.println("V = "+V.toStringWithWightDegrees() );
    }

    //System.out.println("стэк:");
    //System.out.println("stack = "+stack.toString() );
    //System.out.println("F     = "+F.toStringWithWightDegrees() );
    while(stack.size()>0) {
      Vertex p = stack.pop();
      if(this.hasAsFVS(F.withoutOnePoint(p)))
    	F.remove(p);
    }
    //System.out.println("закончили стэк:");
    //System.out.println("stack = "+stack.toString() );
    //System.out.println("F     = "+F.toStringWithWightDegrees() );
    
	return F;	  
  }
}