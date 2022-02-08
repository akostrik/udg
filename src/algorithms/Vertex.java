package algorithms;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import utilities.Color;

public class Vertex extends Point implements Comparable<Vertex> {
  public Color   color;
  public boolean active; // for algo MIS
  public double  weight; // 

  public Vertex(int x, int y) {
    super(x,y);
    this.color  = Color.WHITE;
    this.active = false;
  }

  public Vertex(int x, int y, double weight) {
    super(x,y);
    this.color  = Color.WHITE;
    this.active = false;
    this.weight = weight;
  }

  @Override
  /// second comparator  public Vertex vertexHighest_dAsterix_id() ?
  public int compareTo(Vertex other) { // duplicata of points possible // not used
	if(this.x>other.x) 
	  return 1;
    if(this.x==other.x && this.y>other.y) 
	  return 1;
    if(this.x==other.x && this.y==other.y && System.identityHashCode(this)>System.identityHashCode(other) ) 
	  return 1;
    if(this.x<other.x) 
	  return -1;
    if(this.x==other.x && this.y<other.y) 
	  return -1;
    if(this.x==other.x && this.y==other.y && System.identityHashCode(this)<System.identityHashCode(other) ) 
	  return -1;
    return 0;
  }

  public int countEffectiveDegree(UDG g) {  // for MIS
	return g.notExploredNeighborhoodWithoutCentralPoint(this).size();
  }

  //////////////////////// COLORS

  public void setActive() {
	this.active=true;  
  }

  public void markGrey() {
    this.color=Color.GREY; 
  }

  public void markDominatee() {
    this.color=Color.GREY; 
  }

  public void markBlack() {
    this.color=Color.BLACK;
  }

  public void markAsDominator() {
    this.color=Color.BLACK;
  }

  public void markBlue() {
    this.color=Color.BLUE;
  }

  public void markNotExplored() {
    this.color=Color.WHITE; 
  }

  public boolean isGrey() {
	return this.color.equals(Color.GREY);
  }

  public boolean isDominatee() {
	return this.color.equals(Color.GREY);
  }

  public boolean isWhite() {
	return this.color.equals(Color.WHITE);
  }

  public boolean isNotExplored() {
	return this.color.equals(Color.WHITE);
  }

  public boolean isBlack() {
	return this.color.equals(Color.BLACK);
  }

  public boolean isDominator() {
	return this.color.equals(Color.BLACK);
  }

  public boolean isBlue() {
	return this.color.equals(Color.BLUE);
  }

  public boolean isNotExploredActive() {
	return (this.color.equals(Color.WHITE) && this.active);  
  }

  // // // // // // // // //// UTILS

  public Vertex clone() {
	///return new Vertex(this.x,this.y,this.color,this.active);
	return new Vertex(this.x,this.y);
  }
  
/*  public boolean equals(Vertex other) {
	return this.x==other.x&&this.y==other.y&&this.color==other.color&&this.active==other.active;
  }*/

  public static ArrayList<Vertex> convertToVertex(ArrayList<Point> points) {
	ArrayList<Vertex> toReturn = new ArrayList<Vertex>();
	for(Point p : points) 
	  toReturn.add(new Vertex(p.x,p.y));
	return toReturn;
  }
  
  public String toString() {
    String toReturn = "["+this.x+" "+this.y+" ";
 	if     (this.isNotExploredActive()) toReturn += "aw";
    else if(this.isNotExplored())       toReturn += "w";
    else if(this.isDominatee ())        toReturn += "g";
    else if(this.isDominator())         toReturn += "b";
	return toReturn+"]";
  }
}