// l'affichage spécialsé pour chaque algo ?
package algorithms;
import java.awt.Point;
import java.util.ArrayList;

import udg.UDG;
import udg.Vertex;

public class DefaultTeam {
  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	UDG.edgeThreshold = edgeThreshold;

	UDG solution = new UDGwithFVS(Vertex.convertToVertex(points)).fvs();
	// UDG solution = new UDGwithDS(Vertex.convertToVertex(points)).ds();
	// UDG solution = new UDGwithMis(Vertex.convertToVertex(points)).misWithProperty(); 
	// UDG solution = new UDGwithCDS(Vertex.convertToVertex(points)).cds();

	System.out.println("solution: "+solution.toString());
	return solution.convertToPoints();
  }
}
