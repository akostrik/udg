package algorithms;
import java.util.ArrayList;

public class UDGwithMIS extends UDG { // maximal independent set

  public UDGwithMIS(ArrayList<Vertex> vertex) { 
    super(vertex);
    this.isSolution                = (solutionCandidat)     -> { return hasAsMisWithPropriety(solutionCandidat); }; // isMis for other goals
    this.shouldTryToReplace2Points = (Vertex p1, Vertex p2) -> { return p1.distance(p2)<4*edgeThreshold; }; /// ?
    this.shouldTryToReplace3Points = (Vertex p1, Vertex p2, Vertex p3)-> { /// ?
      return  (p1.distance(p2)<4*edgeThreshold  && p2.distance(p3)<4*edgeThreshold) && (p3.distance(p1)<4*edgeThreshold);
    }; 
  }

  public UDG mis() { 
    if(!this.isConnected()) {
       System.out.println("input UPD must be connected ");
       return new UDG();
    }

    // get first solution:
    UDG mis = misWithProperty(); // need for CDS
    // UDG mis = misNaivWithoutProperty(); // doesn't suit for CDS

    // optimize the first solution: 
	mis=tryToReplace2by1(mis); // too long

	return mis;
  }
  
  public UDG misWithProperty() { 
	// "Connected Domination in Multihop Ad Hoc Wireless Networks" by Cardei, Cheng, Cheng, Du	
	// propetrty = "distance 2 hops" => suits for CDS "On greedy construction of CDS in wireless networks" by Yingshu et al
    markAllVertexWhite(); 
    while(this.whiteVertices().size()>0) { // main cycle
      System.out.println("mis, rest "+this.whiteVertices().size()+" vertex");
      Vertex dominator = newDominator(); 
      dominator.markAsDominator();
  	  for(Vertex dominatee : notExploredNeighborhoodWithoutCentralPoint(dominator).vertices) { 
	    dominatee.markDominatee();
    	for(Vertex neighborOfDominatee : notExploredNeighborhoodWithoutCentralPoint(dominatee).vertices)  
    	  neighborOfDominatee.setActive();
  	  }
    } 
    return this.dominatorsVertices();
  }  

  private Vertex newDominator() { // elections from White Active
	if(this.dominatorsVertices().size()==0) // the first dominator
	  return this.theMostConnectedPoint();  
    Vertex activeWhite=anyNotExploredActiveVertex();
    return this.notExploredActiveNeighborhoodWithCentralPoint(activeWhite).vertexHighest_dAsterix_id();
  }
	
  public UDG misNaivWithoutProperty() { 
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