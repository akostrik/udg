// algo non ditribué
// optimisation - safe effective degree (but < complexity of CDS)
/*
390 400
400 440
390 480
460 400
450 440
460 480
*/
package algorithms;
import java.util.ArrayList;

import udg.UDG;
import udg.Vertex;

public class UDGwithMIS extends UDG { // maximal independent set

  public UDGwithMIS(ArrayList<Vertex> vertex) { 
    super(vertex);
    this.isSolution                  = (solutionCandidat)                -> { return hasAsMisWithPropriety(solutionCandidat); }; // isMis for other goals
    this.shouldTryToReplace2Points   = (Vertex p1, Vertex p2)            -> { return true; }; 
    this.shouldTryToReplace3Points = (Vertex p1, Vertex p2, Vertex p3) -> { return true; };
  }

  public UDG misWithProperty() { 
	// "distance 2 hops" => suits for CDS "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
    if(!this.isConnected()) {
      System.out.println("input UPD must be connected ");
      return new UDG();
    }

    markAllVertexWhite(); 

    while(this.whiteVertex().size()>0) { // main cycle
      System.out.println("mis, rest "+this.whiteVertex().size()+" vertex");
      Vertex dominator = newDominators(); 
      dominator.markAsDominator();
  	  for(Vertex dominatee : notExploredNeighborhoodWithoutCentralPoint(dominator).vertices) { 
	    dominatee.markDominatee();
    	for(Vertex neighborOfDominatee : notExploredNeighborhoodWithoutCentralPoint(dominatee).vertices)  
    	  neighborOfDominatee.setActive();
  	  }
    } 
    
    UDG mis=this.blackVertex(); // dominator black, dominatee grey
	// mis=tryToReplace2by1(mis); too long
    return mis; 
  }  

  private Vertex newDominators() { // elections from White Active
	if(this.blackVertex().size()==0) // the first dominator
	  return this.theMostConnectedPoint();  
    Vertex activeWhite=anyNotExploredActiveVertex();
    return this.notExploredActiveNeighborhoodWithCentralPoint(activeWhite).vertexHighest_dAsterix_id();
  }
	
  ////////////////////////////////////////////// UTILS

  public UDG mis() { 
	// hasn't property "distance 2 hops" => doesn't suit for CDS "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
	UDG rest = this.clone();
	UDG mis  = new UDG();
	while(rest.size()>0) {
	  Vertex v = rest.get(0);
	  mis.add(v.clone());
	  rest.removeAll(rest.neighborhoodWithCentralPoint(v).clone());
	}
	return mis;
  } 
}