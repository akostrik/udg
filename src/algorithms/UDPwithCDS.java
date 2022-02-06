// https://github.com/LexTek/Convex-Minimum-Dominating-Set UPMC
// https://github.com/arjunvijayvargiya/ConnectedDominatingSet : i=2 only
// https://github.com/ThamazghaSMAIL/DominatingSet/tree/master/src/algorithms UPMC
// https://github.com/cbyad/ConnectedDominatingSetAAGA/tree/master/S_MIS_CDS/src/algorithms UPMC
// https://github.com/ThamazghaSMAIL/DominatingSet/blob/master/Rapport.pdf UPMC double
// https://github.com/AlexisBelanger/MinimalConnectedDominatingSet/tree/master/AAGA_Projet2/src/algorithms UPMC

// optimisation - listing all MISs and chois the best one ?

/*
470 170
438 194
448 103
542 172
437 148
404 190
531 181
483 179
 */
package algorithms;
import java.util.ArrayList;
import java.util.HashSet;
import udp.UDP;
import udp.Vertex;

public class UDPwithCDS extends UDP { // connected dominating set
	
  public UDPwithCDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                   = (solutionCandidat)      -> { return !this.clonePartExternalTo(solutionCandidat).cyclesExist(); };
    this.willTryToReplaceTwoPoints       = (Vertex p1, Vertex p2)  -> { return true;}; 
    this.willTryToReplaceThreePoints     = (Vertex p1, Vertex p2, Vertex p3)-> {return true;};
  }

  public UDP cds() { // "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
	UDP mis = new UDPwithMisWithProperty(vertex).mis(); // not clone 
    System.out.println("mis = "+mis.toStringWithColorsDegrees());

	this.markVertexBlack(mis);
  	this.partExternalTo(mis).markAllVertexGrey();
    
  	this.mapBlackBlueComponents = blackComponents();
  	System.out.println("mapBlackBlueComponents : "+mapBlackBlueComponents.toString());

	// connector via black vertex of 2, 3, 4, or 5 blackBlueComposants
	// anyGreyNodeAdjacentToAtLeast_i_blackNodesInDiffBlackBlueComponents
    for(int i=5; i>=2; i--) // optimisation - lorsque i=5 trouvé pour i=2
      for(boolean changements=true; changements==true; changements=false)
        for(Vertex connector : this.greyVertex().vertex) {
          ArrayList<UDP> potentiallyConnectedComponents = potentiallyConnectedComponentsByThisConnector(connector);
       	  System.out.println("i="+i+", "+connector.toString()+ " can connect components : "+potentiallyConnectedComponents.toString());
    	  if(potentiallyConnectedComponents!=null && potentiallyConnectedComponents.size()==i) {
    	    connectComponents(potentiallyConnectedComponents,connector);
            // System.out.println("i="+i+", "+connector+" connect "+potentiallyConnectedComponents.toString()+" ->");
            // System.out.println("    "+mapBlackBlueComponents.toString());
    	    connector.markBlue();
    	    changements=true;
   	      }
        }
    return this.blackAndBlueVertex();
  }

  private ArrayList<UDP> potentiallyConnectedComponentsByThisConnector(Vertex connector) { // Via Black Nodes
	HashSet<UDP> potentiallyConnectedComponents = new HashSet<UDP>(); // HashSet => without dublicats
    // System.out.println("  ** potentiallyConnectedComponentsByThisConnector");
    // System.out.println("  ** verify neighborhood "+this.blackNeighborhoodWithoutCentralPoint(connector).toString());
    for (Vertex blackNeighborOfConnector : this.blackNeighborhoodWithoutCentralPoint(connector).vertex) {
      // System.out.println("  ** verify its neighbor "+blackNeighborOfConnector.toString());
      for (UDP component : this.mapBlackBlueComponents.values()) {
        // System.out.println("  *** verify component "+component.toString());
    	if (component.contains(blackNeighborOfConnector)) {
    	  potentiallyConnectedComponents.add(component); 
          // System.out.println("  *** OK contains !");
    	}
      }
    }
	return new ArrayList<UDP>(potentiallyConnectedComponents);
  }

  private void connectComponents(ArrayList<UDP> components, Vertex connector) { // à refaire
  	//System.out.println("  1) "+mapBlackBlueComponents.toString());
    UDP unitedComponent = components.get(0);
    mapBlackBlueComponents.put(connector,unitedComponent);
    unitedComponent.add(connector);
  	//System.out.println("  put "+connector.toString()+" to "+unitedComponent.toString()+ " -> ");
  	//System.out.println("     "+mapBlackBlueComponents.toString());

  	for(int i=1; i<components.size(); i++) {
	  UDP component = components.get(i);
	  for(Vertex p : component.vertex) {
		mapBlackBlueComponents.remove(p,component);
	  	//System.out.println("  remove ("+p.toString()+" , "+component.toString()+" ) -> ");
	  	//System.out.println("     "+mapBlackBlueComponents.toString());
	    mapBlackBlueComponents.put(p,unitedComponent);
	    unitedComponent.add(p);
	  	//System.out.println("  put "+p.toString()+" to "+unitedComponent.toString()+" -> ");
	  	//System.out.println("     "+mapBlackBlueComponents.toString());
	  }
	}

  	//System.out.println("  2) "+mapBlackBlueComponents.toString());
  }
}
