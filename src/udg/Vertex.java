package udg;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import algorithms.UDGwithMIS;

public class Vertex extends Point {
  public Color   color;
  public boolean active;                         // for algo MIS
  public Vertex  dominator;                      // for algo MIS
  public boolean hasBroadcastedToBlackNeighbors; // for algo MIS

  public Vertex(int x, int y) {
    this.x                                       = x;
    this.y                                       = y;
    this.color                                   = Color.WHITE;
    this.active                                  = false;
    this.dominator                               = null;
  }

  public Vertex(int x, int y, Color color, boolean active, Vertex dominator) {
    this.x                                       = x;
    this.y                                       = y;
    this.color                                   = color;
    this.active                                  = active;
    this.dominator                               = dominator;
  }
  
  public int countEffectiveDegree(UDG g) {
	return g.notExploredNeighborhoodWithoutCentralPoint(this).size();
  }

  public int getDegree(UDG g) {
	return g.neighborhoodWithoutCentralPoint(this).size();
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

  //////////////////// UTILS

  public Vertex clone() {
	return new Vertex(this.x,this.y,this.color,this.active,this.dominator);
  }
  
  public boolean equals(Vertex other) {
	return this.x==other.x&&this.y==other.y&&this.color==other.color&&this.active==other.active&&this.dominator==other.dominator;
  }

  public static ArrayList<Vertex> convertToVertex(ArrayList<Point> points) {
	ArrayList<Vertex> toReturn = new ArrayList<Vertex>();
	for(Point p : points) 
	  toReturn.add(new Vertex(p.x,p.y));
	return toReturn;
  }
  
  public String toString() {
    String toReturn = "["+this.x+" "+this.y+" ";
 	if     (this.isNotExploredActive()) toReturn += "aw";
    else if(this.isNotExplored())          toReturn += "w";
    else if(this.isDominatee ())          toReturn += "g";
    else if(this.isDominator())          toReturn += "b";
	return toReturn+"]";
  }
}