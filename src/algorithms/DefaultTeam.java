// l'affichage spécialsé pour chaque algo ?
// javadoc ?
// classes from another projet ?

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

    //(new UDG(Vertex.convertToVertex(points))).printVertexDFS(); 
    //(new UDG(Vertex.convertToVertex(points))).printCyclesDFS(); 
    //(new UDG(Vertex.convertToVertex(points))).printSemidisjointeCyclesDFS(); 
	//(new UDG(Vertex.convertToVertex(points))).cyclesExistDFS();
	//solution = UDG.allPointOfSeveralUDG(new UDG(Vertex.convertToVertex(points)).returnCyclesDFS()); // all cycles
    //solution = (new UDG(Vertex.convertToVertex(points))).anySemidisjointCycle();

    System.out.println(solution!=null ? "solution: "+solution.toString() : "null");
	return solution!=null ? solution.convertToPoints() : new ArrayList<Point>();
  }
}
