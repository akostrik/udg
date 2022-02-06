
// optimisation - listing all MISs and chois the best one ?

package algorithms;
import java.util.ArrayList;
import java.util.HashSet;

import udg.UDG;
import udg.Vertex;

public class UDGwithCDS extends UDG { // connected dominating set
	
  public UDGwithCDS(ArrayList<Vertex> vertex) {
    super(vertex);
    this.isSolution                  = (solutionCandidat)                -> { return this.hasAsCDS(solutionCandidat); };
    this.willTryToReplace2Points   = (Vertex p1, Vertex p2)            -> { return true; }; 
    this.willTryToReplace3Points = (Vertex p1, Vertex p2, Vertex p3) -> { return true; };
  }

  public UDG cds() { 
	UDG cds=cdsAlgoArticle(); 
	cds = this.tryToRemovePoints(cds); 
	// cds = this.tryToReplace2by1(cds); // too long
	System.out.println(" cds = "+cds.toStringWithColorsDegrees());
    return cds;	
  }
  
  private UDG cdsAlgoArticle() { // "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
	UDG mis = new UDGwithMIS(vertex).misWithProperty(); 
    System.out.println("mis = "+mis.toStringWithColorsDegrees());
	this.markVertexBlack(mis);
  	this.partExternalTo(mis).markAllVertexGrey();
  	this.mapBlackBlueComponents = blackComponents();

	// connect 2/3/4/5 blackBlueComposants par anyGreyNodeAdjacentToAtLeast_i_blackNodesInDiffBlackBlueComponents
    for(int i=5; i>=2; i--) /// optimisation - lorsque i=5 trouvé pour i=2
      for(boolean changements=true; changements==true; changements=false)
        for(Vertex connector : this.greyVertex().vertex) {
          ArrayList<UDG> potentiallyConnectedComponents = potentiallyConnectedBlackBlueComponents(connector);
       	  System.out.println("i="+i+", "+connector.toString()+ " can connect components : "+potentiallyConnectedComponents.toString());
    	  if(potentiallyConnectedComponents!=null && potentiallyConnectedComponents.size()==i) {
    	    connectBlackBlueComponents(potentiallyConnectedComponents,connector);
    	    connector.markBlue();
    	    changements=true;
   	      }
        }
    return this.blackAndBlueVertex();
  }

  private ArrayList<UDG> potentiallyConnectedBlackBlueComponents(Vertex connector) { 
	HashSet<UDG> potentiallyConnectedComponents = new HashSet<UDG>(); 
    for (Vertex blackNeighborOfConnector : this.blackNeighborhoodWithoutCentralPoint(connector).vertex) 
      for (UDG component : this.mapBlackBlueComponents.values()) 
    	if (component.contains(blackNeighborOfConnector)) 
    	  potentiallyConnectedComponents.add(component); 
 	return new ArrayList<UDG>(potentiallyConnectedComponents);
  }
 
  private void connectBlackBlueComponents(ArrayList<UDG> components, Vertex connector) { /// optimisation not keep all UDP
    UDG unitedComponent = components.get(0);
    mapBlackBlueComponents.put(connector,unitedComponent);
    unitedComponent.add(connector);
  	for(int i=1; i<components.size(); i++) {
	  UDG component = components.get(i);
	  for(Vertex p : component.vertex) {
		mapBlackBlueComponents.remove(p,component);
	    mapBlackBlueComponents.put(p,unitedComponent);
	    unitedComponent.add(p);
	  }
	}
  }
}
