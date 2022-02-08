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
	setWeightForAllVertices(1); 
	UDG rest = this.clone();
	UDG fvs  = new UDG();
    System.out.println("*******");
    System.out.println("this = "+this.toStringWithWightDegrees() );
    System.out.println("rest = "+rest.toStringWithWightDegrees() );
    System.out.println("fvs  = "+fvs .toStringWithWightDegrees() );
	Stack<Vertex> stack = new Stack<Vertex>();
	UDG sjCycle;
	this.cleanup(); rest.cleanup(); // ?

	while(rest.vertices.size()>0) {
      System.out.println("--1--");
      System.out.println("this = "+this.toStringWithWightDegrees() );
      System.out.println("rest = "+rest.toStringWithWightDegrees() );
      System.out.println("fvs  = "+fvs.toStringWithWightDegrees() );
      if((sjCycle=rest.anySemidisjointCycle())!=null) {
   	    System.out.println("    sjCycle = "+sjCycle.toStringWithWightDegrees());
    	for(Vertex p : sjCycle.vertices)
    	  p.weight=0;
      }
      else {
    	System.out.println("    no sjCycles");
      	double gamma=0;
    	for(Vertex p : rest.vertices)
    	  if(gamma>1./(rest.degree(p)-1))
    		gamma=1./(rest.degree(p)-1);
    	for(Vertex p : rest.vertices)
    	  p.weight = p.weight - gamma*(rest.degree(p)-1);
      }
      for(int i=0; i<rest.size(); i++) {
    	Vertex p = rest.get(i);
    	if(p.weight==0) {
       	  System.out.println("    переносим "+p.toString()+" из rest в fvs");
    	  fvs.add(p);
          rest.remove(p);
          i--;
          stack.push(p);
    	}
      }
      System.out.println("--2--");
      System.out.println("this = "+this.toStringWithWightDegrees() );
      System.out.println("rest = "+rest.toStringWithWightDegrees() );
      System.out.println("fvs  = "+fvs .toStringWithWightDegrees() );

  	  this.cleanup(); rest.cleanup(); // ?
      System.out.println("--3-- aftre cleanup");
      System.out.println("this = "+this.toStringWithWightDegrees() );
      System.out.println("rest = "+rest.toStringWithWightDegrees() );
      System.out.println("fvs  = "+fvs .toStringWithWightDegrees() );
    }

    System.out.println("*** stack = "+stack.toString() );
    while(stack.size()>0) {
      Vertex p = stack.pop();
      if(this.hasAsFVS(fvs.withoutOnePoint(p)))
    	fvs.remove(p);
    }
    System.out.println("*** stack = "+stack.toString() );
    System.out.println("*** return fvs = "+fvs.toString() );
    
	return fvs;	  
  }
}