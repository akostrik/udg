package udp; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import functionalInterfaces.IsSolution;
import functionalInterfaces.AlgoImprove1stSolution;
import functionalInterfaces.Algo;
import functionalInterfaces.WillTryToReplace2Points;
import functionalInterfaces.WillTryToReplace3Points;
import functionalInterfaces.ShouldContinueGreedy;
import functionalInterfaces.ToRemoveBeforeContinueGreedy;
import java.awt.Point;

public class UDP {
  public ArrayList<Vertex>             vertex                       = null; // initialisation in the constructor
  public static int                    edgeThreshold;                       // the only init., here ? in the calling class ?
  public static ArrayList<UDP>         cycles;                              
  public Map<Vertex,UDP>               mapBlackBlueComponents       = null;
  public AlgoImprove1stSolution        tryToRemovePoints            = null; // initialisation in the constructor
  public AlgoImprove1stSolution        tryToReplace2by1             = null; // initialisation in the constructor
  public AlgoImprove1stSolution        tryToReplace3by2             = null; // initialisation in the constructor
  public Algo                          greedyAlgo                   = null; // initialisation in the constructor
  public WillTryToReplace2Points       willTryToReplace2Points      = null; // initialisation in sub-classes
  public WillTryToReplace3Points       willTryToReplace3Points      = null; // initialisation in sub-classes
  public ShouldContinueGreedy          shouldContinueGreedy         = null; // initialisation in sub-classes
  public ToRemoveBeforeContinueGreedy  toRemoveBeforeContinueGreedy = null; // initialisation in sub-classes
  public IsSolution                    isSolution                   = null; // initialisation in sub-classes

  // // // // // // // // // // // // // // CONTRUCTORS
  
  public UDP(ArrayList<Vertex> vertex) {
    this.vertex            = vertex;   
    this.tryToRemovePoints = (firstSolution) -> { return this.tryToRemovePoints(firstSolution); };
    this.tryToReplace2by1  = (firstSolution) -> { return this.tryToReplace2by1 (firstSolution); };
    this.tryToReplace3by2  = (firstSolution) -> { return this.tryToReplace3by2 (firstSolution); };
    this.greedyAlgo        = (             ) -> { return this.greedyAlgo       (             ); };
  }
  
  public UDP(HashSet<Vertex> vertex) {
	this(new ArrayList<Vertex>(vertex));  
  }
  
  public UDP(ArrayList<Vertex> vertex, Vertex p) {
    this(vertex);
    this.add(p);
  }
  
  public UDP(Vertex p1, Vertex p2) {
    this(new ArrayList<Vertex>());
	this.add(p1); 
	this.add(p2);
  }

  public UDP(Vertex p) {
    this(new ArrayList<Vertex>());
	this.add(p);
  }

  public UDP() {
  	this(new ArrayList<Vertex>());
  }
	  
  // // // // // // // // // // // // // // ALGOS

  public static UDP repeatNtimes(int N, UDP firstSolution, AlgoImprove1stSolution func) { 
    UDP currentSolution  = firstSolution.clone(); /// serialization insteaf of clone ?	
    UDP solutionCandidat = null; 
    for (int i=0;i<N;i++) { 
   	  solutionCandidat = func.method(currentSolution); // greedyAlgo();
      if (solutionCandidat.score()<currentSolution.score()) 
    	currentSolution = solutionCandidat;
      System.out.println("repeatN: " + currentSolution.size() + ", found next "+solutionCandidat.size());
    }
    return currentSolution;
  }

  public static UDP repeatWhileCanDoBetter(UDP firstSolution, AlgoImprove1stSolution func) { // = Local Search
	// firstSolution = a valid solution
	// func = tryToRemovePoints, tryToreplace2by1, tryToReplace3by2, ...
    UDP currentSolution  = null;	
    UDP solutionCandidat = firstSolution.clone(); 
    do {
  	  currentSolution = solutionCandidat;
  	  solutionCandidat = func.method(currentSolution); 
  	  System.out.println("repCanDB, current "+currentSolution.size()+", found next "+solutionCandidat.size());
  	} while (solutionCandidat.score()<currentSolution.score());
    return currentSolution; 
  }

  public UDP tryToRemovePoints(UDP firstSolution) { 
    UDP solutionCandidat = firstSolution.shuffledClone(); 
    for (int i=0;i<solutionCandidat.size();i++) 
      tryToRemove_i(i,solutionCandidat);     
    return solutionCandidat;
  }
  
  private void tryToRemove_i(int i, UDP solutionCandidat) {
    Vertex removed = solutionCandidat.get(i);
    solutionCandidat.remove(removed);
    if (this.isSolution.method(solutionCandidat)) 
   	  return;
    solutionCandidat.add(i,removed);
   }

  public UDP tryToReplace2by1(UDP firstSolution) { // two points by one
	UDP solutionCandidat = firstSolution.shuffledClone();
    //solutionCandidat = tryToRemovePoints(solutionCandidat); // do not do this
    //if(solutionCandidat.size()==this.size()) return firstSolution;
    UDP rest = partExternalTo(solutionCandidat).clone(); 
    for (int i=0;i<solutionCandidat.size();i++) 
      for (int j=i+1;j<solutionCandidat.size();j++) 
    	if(this.willTryToReplace2Points.method(solutionCandidat.get(i),solutionCandidat.get(j)))
          tryToReplace_i_j_by1(i,j,solutionCandidat,rest); 
    return solutionCandidat;
  }

  public UDP tryToReplace3by2(UDP firstSolution) { // three points by two
	UDP solutionCandidat = firstSolution.shuffledClone();
	solutionCandidat = tryToRemovePoints(solutionCandidat); 
    if(solutionCandidat.size()==this.size()) return firstSolution;
    UDP rest = partExternalTo(solutionCandidat).clone();
    for (int i=0;i<solutionCandidat.size();i++) 
      for (int j=i+1;j<solutionCandidat.size();j++) 
        for (int k=j+1;k<solutionCandidat.size();k++) 
       	  if(this.willTryToReplace3Points.method(solutionCandidat.get(i),solutionCandidat.get(j),solutionCandidat.get(k)))
            tryToReplace_i_j_k_by2(i,j,k,solutionCandidat,rest); 
    return solutionCandidat;
  }

  public void tryToReplace_i_j_by1(int i, int j, UDP solution, UDP rest) { // j>i
    System.out.println("try to replace "+i+" "+j+" "+solution.toString());
    Vertex removedJ = solution.remove(j); // j>i
    Vertex removedI = solution.remove(i);
    for (Vertex added: rest.vertex) {
      solution.add(added);
      if (this.isSolution.method(solution)) { 
    	rest.remove(added);
    	return; 
      }
      solution.remove(added);
    }
    solution.add(i,removedI);
    solution.add(j,removedJ);
  }

  public void tryToReplace_i_j_k_by2(int i, int j, int k, UDP solution, UDP rest) { // i<j<k
    System.out.println("try to replace "+i+" "+j+" "+k+" "+solution.toString());
    Vertex removedK = solution.remove(k);
    Vertex removedJ = solution.remove(j);
    Vertex removedI = solution.remove(i);
    for (Vertex added1: rest.vertex) {
      solution.add(added1);
      for (Vertex added2: rest.vertex) { 
        solution.add(added2);
        if (this.isSolution.method(solution)) {
       	  rest.remove(added1);
       	  rest.remove(added2);
       	  return; 
        }
        solution.remove(added2);
      }
      solution.remove(added1);
    }
    solution.add(i,removedI);
    solution.add(j,removedJ);
    solution.add(k,removedK);
  }

  public UDP greedyAlgo() { // FVS, MDS 
    UDP rest            = this.shuffledClone(); 
    UDP currentSolution = new UDP(); // future MDS or FVS
    while(shouldContinueGreedy.method(currentSolution,rest)) { // FVS while(!isSolution(currentSolution)), MDS while(rest.isNotEmpty()) 
      Vertex theMostConnectedPoint=rest.theMostConnectedPoint();
      currentSolution.add(theMostConnectedPoint.clone());
      rest.removeAll(toRemoveBeforeContinueGreedy.method(theMostConnectedPoint)); // FVS remove(theMostConnectedPointOfRest), MDS removePointAndNeigborhood(theMostConnectedPointOfRest)
    }
    return currentSolution;
  }
	  

  // // // // // // // // // // // // CYCLES 

  public boolean cyclesExist() {
	UDP rest    = this.clone(); 
	UDP visited = new UDP(); 
	while(!rest.isEmpty()) {
      Vertex newlyVisitedP = anyPointFromNeighborhoodOfVisited(visited,rest); 
      rest.remove(newlyVisitedP);
      for (Vertex visitedP1 : visited.vertex) 
        for (Vertex visitedP2 : sublistStartingAfterPoint(visitedP1,visited)) 
          if(isEdge(newlyVisitedP,visitedP1)&&isEdge(newlyVisitedP,visitedP2)) 
            return true;
      visited.add(newlyVisitedP.clone()); 
    }
    return false;
  }
  
  public ArrayList<UDP> elementaryCycles() { // DNS
	UDP.cycles = new ArrayList<UDP>();
    markAllVertexWhite(); 
    for (Vertex p : vertex) 
   	  calculateElementaryCyclesFromPath(new UDP(p));
    return UDP.cycles;
  }

  public void calculateElementaryCyclesFromPath(UDP path) { 
    Vertex lastPointPath = path.get(path.size()-1);
    lastPointPath.markDominatee(); // grey = points of the path = under verification
	for(Vertex newPoint : this.neighborhoodWithoutCentralPoint(lastPointPath).vertex) {
      if(newPoint.isNotExplored()) {
    	path.add(newPoint);
    	calculateElementaryCyclesFromPath(path);
	  }
	  else if(newPoint.isDominatee() && !newPoint.equals(path.get(path.size()-2))) {
	    UDP cycle = path.sublistFromNewPointToNewPoint(newPoint);
	    cycle.rotateAndMayBeInvertDirectionCycle();
	    if(isNewCycle(cycle)) 
	      UDP.cycles.add(cycle);
	  }
    }  
	lastPointPath.markBlack();
  }

  // // // // // // // // // // // // // UTILS - CLONE

  public UDP clone() {
    // to serialize a lambda in Java 8 : possible, strongly discouraged, lambdas may not deserialize properly on another JRE ?
	return new UDP(clone(this.vertex)); 
  }

  public UDP clonePartExternalTo2(UDP g) {
    UDP rest = this.clone();
    rest.removeAll(g);
    return rest; 
  }

  public UDP partExternalTo(UDP g) { // not clone
    ArrayList<Vertex> extrenalPart = new ArrayList<Vertex>(); 
    for(Vertex p : vertex)
      if(!g.contains(p))
    	extrenalPart.add(p);
    return new UDP(extrenalPart);
  }

  public UDP cloneAndAddVertex(Vertex p) {
	UDP clone = this.clone();
	clone.add(p);
    return clone;
  }
  
  public UDP shuffledClone() { 
	UDP clone = this.clone();
    Collections.shuffle(clone.vertex,new Random(System.nanoTime())); 
    return clone;
  }

  public UDP cloneWithoutDuplicata() {
	return new UDP(cloneWithoutDuplicata(this.vertex));
  }	  

  public UDP shuffledCloneWithoutDuplicata() {
    return this.cloneWithoutDuplicata().shuffledClone();
  }

  public ArrayList<Vertex> clone(ArrayList<Vertex> initialListVertex) {
	ArrayList<Vertex> cloneListVertex = new ArrayList<Vertex>();
	for(Vertex p : initialListVertex)
	  cloneListVertex.add(p.clone());
    return cloneListVertex;
  }

  public ArrayList<Vertex> cloneWithoutDuplicata(ArrayList<Vertex> initialListVertex) {
	ArrayList<Vertex> cloneListVertex = new ArrayList<Vertex>();
	for(Vertex p : initialListVertex)
	  cloneListVertex.add(p.clone());
	for (int i=0;i<cloneListVertex.size();i++) 
	  for (int j=i+1;j<cloneListVertex.size();j++) 
	 	if (cloneListVertex.get(i).equals(cloneListVertex.get(j))) {
	 	  cloneListVertex.remove(j);
	      j--;
	    }
    return cloneListVertex;
  }

  // // // // // // // // // // // // // UTILS - SUB-UDP
  
  public UDP neighborhoodWithCentralPoint(Vertex p) { // not clone 
	UDP neighborhood = new UDP();
	for (Vertex candidat: this.vertex) 
	  if (candidat.distance(p)<UDP.edgeThreshold) 
        neighborhood.add(candidat);
	return neighborhood; 
  }

  public UDP neighborhoodWithoutCentralPoint(Vertex p) { // not clone
	UDP neighborhood = neighborhoodWithCentralPoint(p);
	neighborhood.remove(p);
	return neighborhood;
  }

  public UDP notExploredNeighborhoodWithoutCentralPoint(Vertex p) { // not clone 
	UDP neighborhood = new UDP();
	for (Vertex candidat: this.vertex) 
	  if (candidat.distance(p)<UDP.edgeThreshold && candidat.isNotExplored()) 
	    neighborhood.add(candidat);
	neighborhood.remove(p);
	return neighborhood; 
  }

  public UDP notExploredActiveNeighborhoodWithCentralPoint(Vertex p) { // not clone 
	UDP neighborhood = new UDP();
	for (Vertex candidat: this.vertex)
	  if (candidat.distance(p)<UDP.edgeThreshold && candidat.isNotExplored() && candidat.active==true) 
	    neighborhood.add(candidat);
	return neighborhood; 
  }

  public UDP blackNeighborhoodWithoutCentralPoint(Vertex p) { // not clone 
    UDP neighborhood = new UDP();
    // System.out.println("  @ find black neighborhood of "+p.toString()+" in "+this.vertex.toString());
    for (Vertex candidat: this.vertex) 
	  if (candidat.distance(p)<UDP.edgeThreshold && candidat.isBlack()) 
	    neighborhood.add(candidat);
	neighborhood.remove(p);
	return neighborhood; 
  }
  public UDP neighborhoodWithInitialPoints(UDP initialPoints) { // not clone 
	HashSet<Vertex> pointsToAdd = new HashSet<Vertex>(); 
	for(Vertex p : initialPoints.vertex)
	  pointsToAdd.addAll(this.neighborhoodWithCentralPoint(p).vertex);
	initialPoints.addAll(new ArrayList<Vertex>(pointsToAdd));
	return initialPoints; 
  }

  public UDP blackNeighborhoodWithoutInitialPoints(UDP initialPoints) { // not clone 
    return new UDP(blackNeighborhoodWithoutInitialPoints(new HashSet<Vertex>(initialPoints.vertex)));
  }
	  
  public HashSet<Vertex> blackNeighborhoodWithoutInitialPoints(HashSet<Vertex> initialPoints) { // not clone 
	HashSet<Vertex> blackNeighborhood = new HashSet<Vertex>(); 
	for(Vertex initialPoint : initialPoints) 
	  for(Vertex pointCandidat : this.blackNeighborhoodWithoutCentralPoint(initialPoint).vertex) 
		if(!initialPoints.contains(pointCandidat)) 
		  blackNeighborhood.add(pointCandidat);
	return blackNeighborhood; 
  }

  public UDP neighborhoodWithoutInitialPoints(UDP initialPoints) { // not clone 
	UDP toReturn = neighborhoodWithInitialPoints(initialPoints); 
	toReturn.removeAll(initialPoints);
	return toReturn;
  }

  public UDP notExploredVertex() {
	return whiteVertex();
  }
  
  public UDP whiteVertex() {
	UDP white = new UDP();
	for(Vertex p : vertex)
	  if(p.isWhite())
		white.add(p); 
	return white;
  }

  public UDP greyVertex() {
	UDP grey = new UDP();
	for(Vertex p : vertex)
	  if(p.isGrey())
		grey.add(p); 
	return grey;
  }

  public UDP dominatorsVertex() {
	return blackVertex();
  }
  
  public UDP blackVertex() {
	UDP black = new UDP();
	for(Vertex p : vertex)
	  if(p.isBlack())
		black.add(p); 
	return black;
  }

  public UDP blackAndBlueVertex() {
	UDP blackAndBlueVertex = new UDP();
	for(Vertex p : vertex)
	  if(p.isBlack() || p.isBlue())
		blackAndBlueVertex.add(p); 
	return blackAndBlueVertex;
  }

  public UDP notExploredActiveVertex() {
    UDP whiteActiveVertex = new UDP();
    for(Vertex p : this.vertex)
      if(p.isNotExploredActive())
    	whiteActiveVertex.add(p);
    return whiteActiveVertex;
  }
	  
  private UDP sublistFromNewPointToNewPoint(Vertex newPoint) {
	UDP cycle = new UDP(); 
    for(int i=this.vertex.size()-1; i>=0; i--) { 
      cycle.add(vertex.get(i));
      if(vertex.get(i).equals(newPoint)) 
    	break; /// ?
    }
    return cycle;
  }

  public Map<Vertex,UDP> blackComponents() { // not clone
	Map<Vertex,UDP> mapBlackComponents = new HashMap<Vertex, UDP>(); 
    UDP rest = this.blackVertex().clone();
	while(rest.size()>0) {
	  Vertex blackPoint = rest.get(0);
	  UDP blackComponent = blackComponent(blackPoint);
	  mapBlackComponents.put(blackPoint,blackComponent);
	  rest.removeAll(blackComponent);
	}
    return mapBlackComponents;
  }
  
  public UDP blackComponent(Vertex blackPoint) { // not clone
	HashSet<Vertex> blackComponent = new HashSet<Vertex>();
	blackComponent.add(blackPoint);
	while(true) {
	  HashSet<Vertex> toAdd = blackNeighborhoodWithoutInitialPoints(blackComponent);
	  blackComponent.addAll(toAdd);
	  if(toAdd.size()==0) break; /// ?
	} 
	return new UDP(blackComponent);
  }

  public UDP connectedComponent(Vertex p0) { // not clone
	HashSet<Vertex> component = new HashSet<Vertex>();
	component.add(p0);
	while(true) {
	  HashSet<Vertex> toAdd = new HashSet<Vertex>();
      for(Vertex candidat : this.partExternalTo(new UDP(component)).vertex)
	    for(Vertex pointComponent : component)
		  if(isEdge(candidat,pointComponent))
		    toAdd.add(candidat);
	  component.addAll(toAdd);
	  if(toAdd.size()==0) break; /// ?
	} 
	UDP componentAsUDP = new UDP(component);
	return componentAsUDP;
  }

  public boolean isConnected() { 
	return this.cloneWithoutDuplicata().size()==this.connectedComponent(this.get(0)).size();
  }
  
  // // // // // // // // // // // // // UTILS

  public boolean distanceExactlyTwoHops(Vertex p1, Vertex p2) {
    if(isEdge(p1,p2)) return false;
    for(Vertex p : this.vertex)
      if(isEdge(p,p1) && isEdge(p,p2)) return true;
    return false;
  }
  
  public boolean canAddToMisKeepingPropriety(Vertex candidat, UDP misWithPropriety) { 
	//  propriety of lemma 2 "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
    if(misWithPropriety.contains(candidat)) // ?
      return true; 
	for(Vertex pointMis: misWithPropriety.vertex) 
      if(isEdge(candidat,pointMis) || candidat.equals(pointMis))
        return false;
    for(Vertex pointMis: misWithPropriety.vertex) 
      if(distanceExactlyTwoHops(candidat,pointMis))
    	return true;
	return false;
  }

  public boolean hasAsMisWithPropriety(UDP misToVerify) { 
    // distance 2 hops, lemma 2 "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
    for(int i = 0; i<misToVerify.size(); i++) {
      Vertex p1 = misToVerify.get(i);
      boolean p1hasAPointAt2hops=false;
      for(int j = i+1; j<misToVerify.size(); j++) {
        Vertex p2 = misToVerify.get(j);
    	if(p1.equals(p2)) return false;
    	if(isEdge(p1,p2)) return false;
        if(distanceExactlyTwoHops(p1,p2)) {
          p1hasAPointAt2hops = true;
          break;                        /// without break?
        }
      }
      if(!p1hasAPointAt2hops) return false;
    }
	return true;
  }

  public boolean hasAsMis(UDP misToVerify) { // with or without propriety "2 hops distance"
    for(Vertex p : this.partExternalTo(misToVerify).clone().vertex) 
      if(new UDP(misToVerify.clone().vertex,p).isIndependentSet()) // optimisation possible - verify only p ?
        return false;
	return true;
  }

  public boolean isIndependentSet() { 
    for(int i = 0; i<this.size(); i++) {
      Vertex p1 = this.get(i);
      for(int j = i+1; j<this.size(); j++) {
        Vertex p2 = this.get(j);
    	if(isEdge(p1,p2)) 
    	  return false;
      }
    }
 	return true;
  }

  public boolean hasAsCDS(UDP cdsToVerify) { 
    return this.hasAsDS(cdsToVerify) && cdsToVerify.isConnected();
  }
  
  public boolean hasAsDS(UDP dsToVerify) { 
	if(dsToVerify.isEmpty()) return false;
    for(Vertex p : vertex) {
      boolean pointIsVisited=false;
      for(Vertex pDs : dsToVerify.vertex)  
	    if (isEdgeOrEqualPoints(p,pDs)) 
	      pointIsVisited=true;
      if(!pointIsVisited)
        return false;
    }
   return true;
  }

  public boolean hasAsFVS(UDP fvsToVerify) {
	return !this.partExternalTo(fvsToVerify).clone().cyclesExist();
  }
  
  public Vertex anyNotExploredActiveVertex() { 
	for(Vertex p : vertex)
	  if(p.isWhite() && p.active==true)
		return p;
    return null;
  }
  
  public Vertex anyWhiteVertex() {
	for(Vertex p : vertex)
	  if(p.isWhite())
		return p;
    return null;
  }
  
  public Vertex anyGreyVertex() {
	for(Vertex p : vertex)
	  if(p.isGrey())
		return p;
    return null;
  }
  
  public Vertex vertexHighest_dAsterix_id() {
	Vertex vertexHighest_dAsterix_id=vertex.get(0);
	for(Vertex candidat : vertex)
	  if(   candidat.countEffectiveDegree(this)> vertexHighest_dAsterix_id.countEffectiveDegree(this) 
	    || (candidat.countEffectiveDegree(this)==vertexHighest_dAsterix_id.countEffectiveDegree(this) 
	        &&
		    this.indexOf(candidat) > this.indexOf(vertexHighest_dAsterix_id)                         )  )           
        vertexHighest_dAsterix_id=candidat;
    return vertexHighest_dAsterix_id;
  }
  
  private static ArrayList<Vertex> sublistStartingAfterPoint(Vertex p, UDP g) {
    return new ArrayList<Vertex>(g.vertex.subList(g.vertex.indexOf(p)+1,g.vertex.size()));
  }
	  
  private void rotateAndMayBeInvertDirectionCycle() { // start with left top point 
	ArrayList<Vertex> normalizedPath = clone(this.vertex);
	Vertex leftTopPoint = this.leftTopPoint();
	while (normalizedPath.get(0) != leftTopPoint) {
	  Vertex toDeplace = normalizedPath.get(0);
	  normalizedPath.remove(toDeplace);
	  normalizedPath.add(toDeplace);
	}
    this.vertex = normalizedPath;

    Vertex secondPoint = normalizedPath.get(1);
	Vertex lastPoint   = normalizedPath.get(normalizedPath.size()-1);
    if ( secondPoint.x>lastPoint.x || (secondPoint.x==lastPoint.x && secondPoint.y>lastPoint.y) ) 
      this.invertDirectionCycle();
  }

  private void invertDirectionCycle() { 
    UDP invertedPoints = new UDP();
    invertedPoints.add(this.vertex.get(0));
	for (int i=this.vertex.size()-1; i>=1; i--)
	  invertedPoints.add(vertex.get(i));
	this.vertex = invertedPoints.vertex;
  }

  private Vertex leftTopPoint() {
    Vertex leftTopPoint = this.vertex.get(0);
	for (Vertex p : this.vertex)
	  if ( p.x<leftTopPoint.x || (p.x==leftTopPoint.x && p.y<leftTopPoint.y) )
		leftTopPoint = p;
	return leftTopPoint;
  }

  public static boolean isNewCycle(UDP cycleToVerify) {
	for(UDP cycle : UDP.cycles)
	  if(cycle.equalsNormalizedCycle(cycleToVerify)) return false;
	return true;
  }
  
  public boolean equalsNormalizedCycle(UDP cycle2) {
	if(this.size()!=cycle2.size()) 
	  return false;
    for(int i=0; i<this.size(); i++)
	  if(!(this.get(i).x==cycle2.get(i).x && this.get(i).y==cycle2.get(i).y)) 
	    return false;
    return true;
  }
  
  public boolean everyPointMayBeExceptOneHasDegreeTwo() {
    boolean weHaveAlreadySeeDegreeNoEqualsToTwo=false;
	for(Vertex p : this.vertex){
      if(p.getDegree(this)!=2) {
        if(weHaveAlreadySeeDegreeNoEqualsToTwo) 
  	      return false;
        else 
  	      weHaveAlreadySeeDegreeNoEqualsToTwo = true;
      }
    }
    return true;
  }

  private Vertex anyPointFromNeighborhoodOfVisited(UDP visitedConnComp, UDP rest) {
	if(rest.size()==0) 
	  return null;
	if(rest.neighborhoodWithoutInitialPoints(visitedConnComp).size()>0) 
	  return rest.neighborhoodWithoutInitialPoints(visitedConnComp).get(0);
	return rest.get(0); 
  }
  
  public Vertex theMostConnectedPoint() {
    if(vertex.size()==0) return null;
    Vertex theMostConnectedPoint=vertex.get(0);
    for (Vertex p: vertex) 
      if (p.getDegree(this)>theMostConnectedPoint.getDegree(this)) 
        theMostConnectedPoint=p;
    return theMostConnectedPoint;
  }

  public boolean isEdgeNotEqualPoints(Vertex p, Vertex q) {
    return !p.equals(q) && p.distance(q)<edgeThreshold;
  }

  public boolean isEdgeOrEqualPoints(Vertex p, Vertex q) {
    return p.distance(q)<edgeThreshold;
  }
  
  public Vertex get(int n) {
	if(n>=this.vertex.size() || n<0) return null;
	return this.vertex.get(n);
  }
  
  public void markAllVertexWhite() {
    for(Vertex p : this.vertex) 
  	  p.color = Color.WHITE;
  }

  public void markAllVertexBlack() {
    for(Vertex p : this.vertex) 
  	  p.color = Color.BLACK;
  }

  public void markVertexBlack(UDP subG) {
    for(Vertex p : this.vertex)
      for(Vertex q : subG.vertex) 
  	    if(p.equals(q))
  	      p.color = Color.BLACK;
  }

  public void markAllVertexGrey() {
    for(Vertex p : this.vertex) 
  	  p.color = Color.GREY;
  }

  public int nbPointsOfColor(Color color) {
	int nb=0;
    for (Vertex p : this.vertex)
      if(p.color.equals(color))
    	nb++;
    return nb;
  }
	  
  public void add(Vertex p) {
	this.vertex.add(p);
  }

  public void add(int n, Vertex p) {
	vertex.add(n,p);
  }
  
  public void add(UDP g) {
	vertex.addAll(g.vertex);
  }
  
  public void addAll(ArrayList<Vertex> pp) {
	vertex.addAll(pp);
  }

  public Vertex remove(int n) {
	if(n>=vertex.size()) return null;
	return vertex.remove(n); 
  }

  public boolean remove(Vertex toRemove) {
	return vertex.remove(toRemove);
  }

  public boolean remove(ArrayList<Vertex> toRemove) {
	return vertex.removeAll(toRemove);
  }

  public boolean removeAll(UDP g) {
	ArrayList<Vertex> toRemove = g.clone().vertex;
	return vertex.removeAll(toRemove);
  }

  public int indexOf(Vertex p) {
	return this.vertex.indexOf(p);  
  }
  
  public boolean removePointAndItsNeigborhood(Vertex p) {
	ArrayList<Vertex> toRemove = this.neighborhoodWithCentralPoint(p).clone().vertex;
	return vertex.removeAll(toRemove);
  }
 
  public boolean contains(Vertex p) {
	return vertex.contains(p);
  }
  
  public int size() {
	return vertex.size();
  }
  
  public int length() {
	return vertex.size();
  }
  
  public boolean isEmpty() {
	return vertex.size()==0;
  }
  
  public boolean isNotEmpty() {
	return vertex.size()!=0;
  }
  
  public void clear() {
	vertex.clear();
  }

  public ArrayList<Point> getSolutionAsPoints() {
	ArrayList<Point> toReturn = new ArrayList<Point>();
	for(Vertex p : vertex)
	  toReturn.add(new Point(p.x,p.y));
	return toReturn;
  }

  public ArrayList<Point> convertToPoints() {
	ArrayList<Point> points = new ArrayList<Point>();
	for(Vertex p : this.vertex)
	  points.add(new Point(p.x,p.y));
	return points;
  }

  public static boolean isEdge(Vertex p1, Vertex p2) {
	return !p1.equals(p2) && p1.distance(p2) < edgeThreshold; /// p1==p2 ?
  }
  
  public ArrayList<Edge> edges() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
	for(int i=0; i<this.size(); i++) {
	  Vertex p1 = vertex.get(i);
	  for(int j=i+1; j<this.size(); j++) {
	    Vertex p2 = vertex.get(j);
	    if(isEdgeNotEqualPoints(p1,p2))
	      edges.add(new Edge(p1,p2));
	  }
	}
	return edges;
  }
  
  public ArrayList<Edge> allEdgesContaining(Vertex p) {
    ArrayList<Edge> edgesContainingPoint = new ArrayList<Edge>();
	for(Edge edge : this.edges())
	  if(edge.contains(p))
  	    edgesContainingPoint.add(edge);
    return edgesContainingPoint;
  }

  public String toString() {
    if(this.size()==0) return " vide";
	String toReturn=" "+this.size()+":";
    for(Vertex p : vertex)
      if(p!=null)
    	toReturn += "["+p.x+","+p.y+"]";
      else
    	toReturn += "[null]";
	return toReturn;
  }
  
  public String toStringWithColorsDegrees() {
    if(this.size()==0) return " vide";
    String toReturn=this.size()+" vertex: ";
    for(Vertex p : vertex) 
      if(p==null) toReturn += "[null]";
      else        toReturn += p.toString();
	return toReturn;
  }
  
  public static String cyclesToString() {
    String toReturn=UDP.cycles.size()+" cycles:\n";
	  for(UDP cycle : UDP.cycles) 
   	    toReturn += cycle.toString()+"\n";
    return toReturn;
  }

  public void cleanup() { // enleve les points à degrée<=1
	ArrayList<Vertex> toRemove = new ArrayList<Vertex>();
	for(Vertex p : this.vertex)
	  if(p.getDegree(this)>1)
		toRemove.add(p);
	this.vertex.removeAll(toRemove);
  }
  
  public int score() {
	return this.size();
  }
  
  public UDP allCycles() { // for debugging 
    UDP allCycles = this.clone();
    for(UDP cycle : cycles) 
   	  allCycles.removeAll(cycle);
    return allCycles;
  }
}  