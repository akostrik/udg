package algorithms;
import java.awt.Point;
import java.util.ArrayList;
import udp.UDP;
import udp.Vertex;

public class DefaultTeam {
  public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
	UDP.edgeThreshold = edgeThreshold;

	// UDP solution = new UDPWithFVS(Vertex.convertToVertex(points)).fvs();
	UDP solution = new UDPwithDS(Vertex.convertToVertex(points)).ds();
	// UDP solution = new UDPwithMis(Vertex.convertToVertex(points)).misWithProperty(); 
	// UDP solution = new UDPwithCDS(Vertex.convertToVertex(points)).cds();

	System.out.println("solution: "+solution.toString());
	return solution.convertToPoints();
  }
}
