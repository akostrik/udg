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
    this.isSolution                  = (solutionCandidat)                -> { return this.hasAsCDS(solutionCandidat); };
    this.willTryToReplaceTwoPoints   = (Vertex p1, Vertex p2)            -> { return true; }; 
    this.willTryToReplaceThreePoints = (Vertex p1, Vertex p2, Vertex p3) -> { return true; };
  }

  public UDP cds() { 
	UDP cds=cdsAlgoArticle(); 
    System.out.println("1) cds = "+cds.toStringWithColorsDegrees());
	cds = this.tryToRemovePoints(cds); 
	// cds = this.tryToReplace2by1(cds); // too long
	System.out.println("2) cds = "+cds.toStringWithColorsDegrees());
    return cds;	
  }
  
  private UDP cdsAlgoArticle() { // "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
	UDP mis = new UDPwithMisWithProperty(vertex).mis(); 
    System.out.println("mis = "+mis.toStringWithColorsDegrees());
	this.markVertexBlack(mis);
  	this.partExternalTo(mis).markAllVertexGrey();
  	this.mapBlackBlueComponents = blackComponents();

	// connect 2/3/4/5 blackBlueComposants par anyGreyNodeAdjacentToAtLeast_i_blackNodesInDiffBlackBlueComponents
    for(int i=5; i>=2; i--) /// optimisation - lorsque i=5 trouvé pour i=2
      for(boolean changements=true; changements==true; changements=false)
        for(Vertex connector : this.greyVertex().vertex) {
          ArrayList<UDP> potentiallyConnectedComponents = potentiallyConnectedBlackBlueComponents(connector);
       	  System.out.println("i="+i+", "+connector.toString()+ " can connect components : "+potentiallyConnectedComponents.toString());
    	  if(potentiallyConnectedComponents!=null && potentiallyConnectedComponents.size()==i) {
    	    connectBlackBlueComponents(potentiallyConnectedComponents,connector);
    	    connector.markBlue();
    	    changements=true;
   	      }
        }
    return this.blackAndBlueVertex();
  }

  private ArrayList<UDP> potentiallyConnectedBlackBlueComponents(Vertex connector) { 
	HashSet<UDP> potentiallyConnectedComponents = new HashSet<UDP>(); 
    for (Vertex blackNeighborOfConnector : this.blackNeighborhoodWithoutCentralPoint(connector).vertex) 
      for (UDP component : this.mapBlackBlueComponents.values()) 
    	if (component.contains(blackNeighborOfConnector)) 
    	  potentiallyConnectedComponents.add(component); 
 	return new ArrayList<UDP>(potentiallyConnectedComponents);
  }
 
  private void connectBlackBlueComponents(ArrayList<UDP> components, Vertex connector) { /// optimisation not keep all
    UDP unitedComponent = components.get(0);
    mapBlackBlueComponents.put(connector,unitedComponent);
    unitedComponent.add(connector);
  	for(int i=1; i<components.size(); i++) {
	  UDP component = components.get(i);
	  for(Vertex p : component.vertex) {
		mapBlackBlueComponents.remove(p,component);
	    mapBlackBlueComponents.put(p,unitedComponent);
	    unitedComponent.add(p);
	  }
	}
  }
}