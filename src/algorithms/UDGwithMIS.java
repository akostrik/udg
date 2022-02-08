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
    this.isSolution                = (solutionCandidat)     -> { return hasAsMisWithPropriety(solutionCandidat); }; // isMis for other goals
    this.shouldTryToReplace2Points = (Vertex p1, Vertex p2) -> { return p1.distance(p2)<4*edgeThreshold; }; /// ?
    this.shouldTryToReplace3Points = (Vertex p1, Vertex p2, Vertex p3)-> { /// ?
      return  (p1.distance(p2)<4*edgeThreshold  && p2.distance(p3)<4*edgeThreshold) && (p3.distance(p1)<4*edgeThreshold);
    }; 
  }

  public UDG misWithProperty() { 
	// "distance 2 hops" => suits for CDS as in "On greedy construction of CDS in wireless networks" by Yingshu et al
    if(!this.isConnected()) {
      System.out.println("input UPD must be connected ");
      return new UDG();
    }

    markAllVertexWhite(); 

    while(this.whiteVertices().size()>0) { // main cycle
      System.out.println("mis, rest "+this.whiteVertices().size()+" vertex");
      Vertex dominator = newDominators(); 
      dominator.markAsDominator();
  	  for(Vertex dominatee : notExploredNeighborhoodWithoutCentralPoint(dominator).vertices) { 
	    dominatee.markDominatee();
    	for(Vertex neighborOfDominatee : notExploredNeighborhoodWithoutCentralPoint(dominatee).vertices)  
    	  neighborOfDominatee.setActive();
  	  }
    } 
    
    UDG misWithProperty=this.dominatorsVertices(); 
	misWithProperty=tryToReplace2by1(misWithProperty); // too long
    return misWithProperty; 
  }  

  private Vertex newDominators() { // elections from White Active
	if(this.dominatorsVertices().size()==0) // the first dominator
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