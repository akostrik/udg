package udp;
import java.awt.Point;

public class Edge {
  private Point p1;
  private Point p2;
  
  public Edge(Point p1, Point p2) {
	this.p1=p1;
	this.p2=p1;
  }

  public boolean contains(Point p) {
	return this.p1.equals(p) || this.p2.equals(p);
  }

  public Point theOtherPointOfTheEdge(Point p) {
	if(this.p1.equals(p)) return p2;
	if(this.p2.equals(p)) return p1;
	return null;
  }
}
