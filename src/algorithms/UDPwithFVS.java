package algorithms;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import udp.UDP;
import udp.Vertex;

public class UDPwithFVS extends UDP { // feedback vertex set
	
  public UDPwithFVS(ArrayList<Vertex> points) {
    super(points);
    this.shouldContinueGreedy         = (currentSolution,rest)  -> { return !this.isSolution.method(currentSolution);};
    this.toRemoveBeforeContinueGreedy = (pointToRemove)         -> { return new UDP(pointToRemove);};
    this.isSolution                   = (solutionCandidat)      -> { return !this.clonePartExternalTo(solutionCandidat).cyclesExist(); };
    this.willTryToReplaceTwoPoints    = (Vertex p1, Vertex p2)  -> { return true;}; // try to replace any pair of point by another one
    this.willTryToReplaceThreePoints  = (Vertex p1, Vertex p2, Vertex p3)-> {return true;};
  }
  
  public UDP fvs() {
    // this.solution = repeatWhileCanDoBetter((GeomGraph)this,this.removePoints); 
    // this.solution = replace2by1((GeomGraph)this); // 50 points -> 11, 12, 11, 10, 11
    // this.solution = repeatNtimes(2,(GeomGraph)this,this.replace2by1); // long
    // this.solution = repeatWhileCanDoBetter((GeomGraph)this,this.replace2by1s); // too long
    // this.solution = replace3by2((GeomGraph)this); // too long, 50 points -> 13, 13, 13, 13, 13
    // this.solution = this.greedyAlgo();

    UDP.cycles = elementaryCycles();
    allCycles();
	System.out.println("cycles : "+UDP.cyclesToString());
    return null;	  
  }
  
  public UDP twoApproxitaionBafnaBermanFujitoAlgo() {
    // GeomGraph solution = new GeomGraph(edgeThreshold); // F
    // GeomGraph rest     = this.shuffledClone();     // V
    // for (int i=0; rest.isNotEmpty(); i++) {
    
    ArrayList<Point> points =  new ArrayList<Point>();
    UDP toReturn = new UDP();
	ArrayList<Point> f = new ArrayList<Point>(); // F de l'article
	ArrayList<Point> g = new ArrayList<Point>();
	Stack<Point>     s = new Stack<Point>(); 
	Point            u = new Point();
/*	Cycle c; // pour le calcul du cycle semi-disjoint
	double gamma;

	this.cleanup();
		
	while(!points.isEmpty()){
	  c = new Cycle(points);
	  if( (g = c.calculSdCycle()) != null) // contains des cycles semi-disjoint
		points = changeWeight(points, g, gamma);  // poid -= gamma, gamma=minWeight(g)=1
	  else {
		gamma = minWeight2(points); // min des w(u)/(d(u) - 1) du graphe
		points = changeWeight2(points, gamma); // set w(u) <- w(u) - gamma(d(u) - 1)
	  }
	  for(int i = 0; i < points.size(); i++){
		if(points.get(i).getWeight() == 0){
		  f.add(points.get(i));
		  s.push(points.get(i));
		  points.remove(i);
		  i--; // Pour s'assurer qu'on parcourt bien tous les points du graphe
		}
	  }
	  points = cleanup(points);
	}
	while(!s.isEmpty()){		
	  u = s.pop(); 
	  f.remove(u);
	  // Conversion PointDeg -> Point pour utiliser la fonction isValide
	  toReturn = new ArrayList<Point>();
	  for(PointDeg re : f)
	    toReturn.add(new Point(re.x, re.y));
	  //Si F -{u} n'est pas un FVS dans le graphe original, on ajoute le point precedemment supprime
	  if(!isValide(data, toReturn)){
		f.add(u);
		toReturn.add(new Point(u.x, u.y));
	  }
	}*/
	return toReturn;
  }
}