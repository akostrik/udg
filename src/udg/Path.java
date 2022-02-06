package udg;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

public class Path {
  public ArrayList<Point> points = null;

  public Path(ArrayList<Point> points) {
    this.points = points;
  }

  public Path(Point p) {
    this.points = new ArrayList<Point>();
    this.points.add(p);
  }
  
  public Path() {
    this.points = new ArrayList<Point>();
  }

  public Path sortedClone() {
	Path sortedClone = new Path((ArrayList<Point>) this.points.clone());
/*    Collections.sort(sortedClone.points);
    sortedClone.points.sort(Comparator.naturalOrder());
    Collections.sort(sortedClone.points, (p1, p2) -> p2.compareTo(p1));*/
    return sortedClone ;
  }

  public Path cloneAndAddPoint(Point p) {
	Path toReturn = this.clone();
	toReturn.add(p);
	return toReturn;
  }

  public void add(Point p) {
	points.add(p);
  }

  public void add(ArrayList<Point> pps) {
	points.addAll(pps);
  }

  public void add(Path path) {
	points.addAll((Collection<? extends Point>) path.points.clone());
  }

  public Point get(int n) {
	return points.get(n);
  }

  public boolean contains(Point p) {
	return points.contains(p);
  }

  public int size() {
	return points.size();
  }

  public ArrayList<Point> getPoints() {
	return points;
  }
  
  public Path clone() {
	return this.clone();
  }
}