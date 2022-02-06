package algorithms;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import udp.UDP;
import udp.Vertex;

public class UDPwithKmeans extends UDP {
  ArrayList<Vertex> centers = null;
  ArrayList<UDP>    kmeans  = null;
	
  public UDPwithKmeans(ArrayList<Vertex> vertex) {
	this.vertex = vertex;
	this.kmeans = new ArrayList<UDP>();
	//initializeCentres();
	//putTheRestOfPointsTo1stKmean();
  }
/*  
  public UDP kmeans() {
	while(true) 
	  if(acquisionHasNoChanges() && deplacingCentresToBarycentresHasNoChanges())
		break;
  }
  
  public void initializeCentres() {
	centers = new ArrayList<Vertex>();
	centers.add(vertex.get(0)); 
	for (int i=1; i<K ; i++) { 
	  centers.add(vertex.get(i));   							  // 1 version random  
	  //centers.add(nearestPointToOneOfAlredayPickedCenters()); // 2 version 
	  //centers.add(pickCenterWithProbabilityArticle());        // 3 version (article)
	}
    for (Vertex center : centers) { // add every center in its own kmean
   	  UDP newKmean = new UDP();
      newKmean.center = center;
      newKmean.add(center); 
      kmeans.add(newKmean);
    }
  }
	
  public Vertex nearestPointToOneOfAlredayPickedCenters() {
	ArrayList<Vertex> theRestOfThePoints = theRestOfThePoints();
	Vertex nearestPoint = theRestOfThePoints.get(0); 
	double minDistance = minDistanceAlredyPickedCenters(nearestPoint);
	for(Vertex candidatNearestPoint : theRestOfThePoints) {
	  double candidatMinDistance = minDistanceAlredyPickedCenters(candidatNearestPoint);
	  if(candidatMinDistance<minDistance) {
		minDistance = candidatMinDistance;
		nearestPoint = candidatNearestPoint;
	  }
	}
	return nearestPoint;
  }
	
  public Vertex pickCenterWithProbabilityArticle() {
	ArrayList<Integer> probabilities = new ArrayList<Integer>();
	for(Point p : vertex)
	  probabilities.add(minDistanceAlredyPickedCenters(p)); // = (D(x))^2 of the article
	int summeAllProb = 0; 
	for(Integer prob : probabilities) 
	  summeAllProb += prob; 
	int myRandom = (new Random()).nextInt(summeAllProb);	
	int summeProb = 0;
	for(int i=0; i<vertex.size(); i++) {
	  summeProb += probabilities.get(i);
	  if(summeProb>myRandom) {
		if(centers.contains(vertex.get(i)))
		  return pickCenterWithProbabilityArticle();
		return vertex.get(i);
	  }
	}
	return null;
 }
	
  public boolean acquisionHasNoChanges() {
	// acquisition : add every point to the group of the nearest center
	boolean noChanges=true;  
    for(UDP kmean : kmeans) {
   	  ArrayList<Vertex> kmeanPoints = new ArrayList<Vertex>(kmean.getAsArrayOfPoints());
	  for(Vertex p : kmeanPoints) {
	  	UDP nearestKmean = nearestKmean(p);
	  	if(kmean!=nearestKmean) { // as objects ?
	      nearestKmean.add(p);
	      kmean.remove(p);
	      noChanges=false;
	    }
	  }
	}
	//System.out.println("  acquision no changes = "+noChanges);
	return noChanges;
  }
	  
  public boolean deplacingCentresToBarycentresHasNoChanges() {
	// update the center of every kmean to its barycenter 
	boolean noChanges=true;  
	for(UDP kmean : kmeans) 
	  if(kmean.deplacingCentreToBarycenterHasMadeChanges())
    	noChanges=false;
	  //System.out.println("  deplacing no changes = "+noChanges);
	  //System.out.println("  centers = "+centers);
	  return noChanges;
  }
	
	////////////// Utils
	public double minDistanceAlredyPickedCenters(Point p) {
		double minDistance = p.distance(centers.get(0));
		for(Point center : centers) {
			double candidatDistance = p.distance(center);
			if(candidatDistance<minDistance)
				minDistance=candidatDistance; 
		}
		return minDistance ;
	}
	
	public UDP nearestKmean(Point p) {
		  double minDistance = kmeans.get(0).distanceCenter(p);
		  UDP nearestKmean = kmeans.get(0);
		  for(UDP candidatNearestKmean : kmeans) {
			  if(candidatNearestKmean.center==null) {
				  //System.out.println("PB candidatNearestKmean "+candidatNearestKmean+" to the point "+p);
				  //System.out.println("kmeans = "+this.toString());
				  return null;
			  }
			  double candidatMinDistance = candidatNearestKmean.distanceCenter(p);
			  if(candidatMinDistance<minDistance) {
				  nearestKmean = candidatNearestKmean;
				  minDistance = candidatMinDistance;
			  }
		  }
		  return nearestKmean;
		}
		
	public ArrayList<ArrayList<Point>> getAsArrayOfArraysOfPoints() {
		ArrayList<ArrayList<Point>> arrayOfArrayOfPoints = new ArrayList<ArrayList<Point>>();
		for(UDP kmean : kmeans) 
			arrayOfArrayOfPoints.add(kmean.getAsArrayOfPoints());
		return arrayOfArrayOfPoints;
	}
		
	public ArrayList<Point> theRestOfThePoints() {
		ArrayList<Point> toReturn = new ArrayList<Point>(vertex);
		toReturn.removeAll(centers);
		return toReturn;
	}
	
	public void putTheRestOfPointsTo1stKmean() {
		kmeans.get(0).addAll(theRestOfThePoints());		
	}	*/
}