package algorithms;
import java.awt.Point;
import java.util.ArrayList;
import udp.UDP;
import udp.Vertex;

public class DefaultTeam {
  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	UDP.edgeThreshold = edgeThreshold;

	// UDP solution = new UDPWithFVS(Vertex.transformToVertex(points)).fvs();
	UDP solution = new UDPwithMis(Vertex.transformToVertex(points)).misWithProperty(); 
	// UDP solution = new UDPwithCDS(Vertex.transformToVertex(points)).cds();

	System.out.println("solution: "+solution.toString());
	return solution.convertToPoints();
  }
}
