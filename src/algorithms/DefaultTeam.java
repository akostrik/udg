// l'affichage spécialsé pour chaque algo ?
// gitignore ?
// javadoc ?

package algorithms;
import java.awt.Point;
import java.util.ArrayList;

public class DefaultTeam {
  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	UDG.edgeThreshold = edgeThreshold;
    UDG solution=null;
	
	solution = new UDGwithFVS(Vertex.convertToVertex(points)).fvs();
	//solution = new UDGwithDS(Vertex.convertToVertex(points)).ds();
	//solution = new UDGwithMIS(Vertex.convertToVertex(points)).misWithProperty(); 
	//solution = new UDGwithCDS(Vertex.convertToVertex(points)).cds();
	//solution = UDG.allPointOfSeveralUDG(new UDG(Vertex.convertToVertex(points)).DFSreturnCycles()); // all cycles
	// (new UDG(Vertex.convertToVertex(points))).DFSprintCycles(); // print all cycles
    //solution = (new UDG(Vertex.convertToVertex(points))).anySemidisjointCycle();
	System.out.println(solution!=null ? "solution: "+solution.toString() : "null");
	return solution!=null ? solution.convertToPoints() : new ArrayList<Point>();
  }
}
