// l'affichage spécialsé pour chaque algo ?
// javadoc ?
// classes from another projet ?
// Stream<ArrayList<Point>> parallel
// BitSet visited = new BitSet(rest.size());
// public List<String> calc(final Map<String, Vertex> interfaces, final Map<String, Vertex> clients) {

package algorithms;
import java.awt.Point;
import java.util.ArrayList;

public class DefaultTeam {
  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	UDG.edgeThreshold = edgeThreshold;
    UDG solution=null;

    //solution = new UDGwithFVS(Vertex.convertToVertex(points)).fvs();
	solution = new UDGwithDS(Vertex.convertToVertex(points)).ds();
	//solution = new UDGwithMIS(Vertex.convertToVertex(points)).misWithProperty(); 
	//solution = new UDGwithCDS(Vertex.convertToVertex(points)).cds();
	//solution = UDG.unionOf(new UDG(Vertex.convertToVertex(points)).returnCyclesDFS()); // all cycles

    System.out.println(solution!=null ? "solution: "+solution.toString() : "null");
	return solution!=null ? solution.convertToPoints() : new ArrayList<Point>();
  }
}