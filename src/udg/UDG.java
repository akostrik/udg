package udg; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import functionalInterfaces.IsSolution;
import functionalInterfaces.AlgoImproveSolution;
import functionalInterfaces.Algo;
import functionalInterfaces.ShouldTryToReplace2Points;
import functionalInterfaces.ShouldTryToReplace3Points;
import functionalInterfaces.ShouldContinueGreedy;
import functionalInterfaces.ToRemoveBeforeContinueGreedy;
import java.awt.Point;

public class UDG {
  public ArrayList<Vertex>             vertices                      = null; // initialisation in the constructor
  public static int                    edgeThreshold;                       // the only init., here ? in the calling class ?
  public Map<Vertex,UDG>               mapBlackBlueComponents       = null;
  public AlgoImproveSolution           tryToRemovePoints            = null; // initialisation in the constructor
  public AlgoImproveSolution           tryToReplace2by1             = null; // initialisation in the constructor
  public AlgoImproveSolution           tryToReplace3by2             = null; // initialisation in the constructor
  public Algo                          greedyAlgo                   = null; // initialisation in the constructor
  public ShouldContinueGreedy          shouldContinueGreedy         = null; // initialisation in sub-classes
  public ShouldTryToReplace2Points     shouldTryToReplace2Points    = null; // initialisation in sub-classes
  public ShouldTryToReplace3Points     shouldTryToReplace3Points    = null; // initialisation in sub-classes
  public ToRemoveBeforeContinueGreedy  toRemoveBeforeContinueGreedy = null; // initialisation in sub-classes
  public IsSolution                    isSolution                   = null; // initialisation in sub-classes

  // // // // // // // // // // // // // // CONTRUCTORS
  
  public UDG(ArrayList<Vertex> vertices) {
    this.vertices            = vertices;   
    this.tryToRemovePoints = (firstSolution) -> { return this.tryToRemovePoints(firstSolution); };
    this.tryToReplace2by1  = (firstSolution) -> { return this.tryToReplace2by1 (firstSolution); };
    this.tryToReplace3by2  = (firstSolution) -> { return this.tryToReplace3by2 (firstSolution); };
    this.greedyAlgo        = (             ) -> { return this.greedyAlgo       (             ); };
  }
  
  public UDG(HashSet<Vertex> vertex) {
	this(new ArrayList<Vertex>(vertex));  
  }
  
  public UDG(ArrayList<Vertex> vertex, Vertex p) {
    this(vertex);
    this.add(p);
  }
  
  public UDG(Vertex p1, Vertex p2) {
    this(new ArrayList<Vertex>());
	this.add(p1); 
	this.add(p2);
  }

  public UDG(Vertex p) {
    this(new ArrayList<Vertex>());
	this.add(p);
  }

  public UDG() {
  	this(new ArrayList<Vertex>());
  }
	  
  // // // // // // // // // // // // // // ALGOS

  public static UDG repeatNtimes(int N, UDG firstSolution, AlgoImproveSolution func) { 
    UDG currentSolution  = firstSolution.clone(); /// serialization insteaf of clone ?	
    UDG solutionCandidat = null; 
    for (int i=0;i<N;i++) { 
   	  solutionCandidat = func.method(currentSolution); // greedyAlgo();
      if (solutionCandidat.score()<currentSolution.score()) 
    	currentSolution = solutionCandidat;
      System.out.println("repeatN: " + currentSolution.size() + ", found next "+solutionCandidat.size());
    }
    return currentSolution;
  }

  public static UDG repeatWhileCanDoBetter(UDG firstValidSolution, AlgoImproveSolution funcTry) { // = Local Search
    UDG currentSolution  = null;	
    UDG solutionCandidat = firstValidSolution.clone(); 
    do {
  	  currentSolution = solutionCandidat;
  	  solutionCandidat = funcTry.method(currentSolution); // func = tryToRemovePoints, tryToreplace2by1, ...
  	  System.out.println("repCanDB, current "+currentSolution.size()+", found next "+solutionCandidat.size());
  	} while (solutionCandidat.score()<currentSolution.score());
    return currentSolution; 
  }

  public UDG tryToRemovePoints(UDG firstSolution) { 
    UDG solutionCandidat = firstSolution.shuffledClone(); 
    for (int i=0;i<solutionCandidat.size();i++) 
      tryToRemove_i(i,solutionCandidat);     
    return solutionCandidat;
  }
  
  private void tryToRemove_i(int i, UDG solutionCandidat) {
    Vertex removed = solutionCandidat.get(i);
    solutionCandidat.remove(removed);
    if (this.isSolution.method(solutionCandidat)) 
   	  return;
    solutionCandidat.add(i,removed);
   }

  public UDG tryToReplace2by1(UDG firstSolution) { // two points by one
	UDG solutionCandidat = firstSolution.shuffledClone();
    //solutionCandidat = tryToRemovePoints(solutionCandidat); // do not do this, it spoils le resultat
    //if(solutionCandidat.size()==this.size()) return firstSolution;
    UDG rest = partExternalTo(solutionCandidat).clone(); 
    for (int i=0;i<solutionCandidat.size();i++) 
      for (int j=i+1;j<solutionCandidat.size();j++) 
    	if(this.shouldTryToReplace2Points.method(solutionCandidat.get(i),solutionCandidat.get(j)))
          tryToReplace_i_j_by1(i,j,solutionCandidat,rest); 
    return solutionCandidat;
  }

  public UDG tryToReplace3by2(UDG firstSolution) { // three points by two
	UDG solutionCandidat = firstSolution.shuffledClone();
	solutionCandidat = tryToRemovePoints(solutionCandidat); 
    if(solutionCandidat.size()==this.size()) return firstSolution;
    UDG rest = partExternalTo(solutionCandidat).clone();
    for (int i=0;i<solutionCandidat.size();i++) 
      for (int j=i+1;j<solutionCandidat.size();j++) 
        for (int k=j+1;k<solutionCandidat.size();k++) 
       	  if(this.shouldTryToReplace3Points.method(solutionCandidat.get(i),solutionCandidat.get(j),solutionCandidat.get(k)))
            tryToReplace_i_j_k_by2(i,j,k,solutionCandidat,rest); 
    return solutionCandidat;
  }

  public void tryToReplace_i_j_by1(int i, int j, UDG solution, UDG rest) { // j>i
    System.out.println("try to replace "+i+" "+j+" "+solution.toString());
    Vertex removedJ = solution.remove(j); // j>i
    Vertex removedI = solution.remove(i);
    for (Vertex added: rest.vertices) {
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

  public void tryToReplace_i_j_k_by2(int i, int j, int k, UDG solution, UDG rest) { // i<j<k
    System.out.println("try to replace "+i+" "+j+" "+k+" "+solution.toString());
    Vertex removedK = solution.remove(k);
    Vertex removedJ = solution.remove(j);
    Vertex removedI = solution.remove(i);
    for (Vertex added1: rest.vertices) {
      solution.add(added1);
      for (Vertex added2: rest.vertices) { 
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

  public UDG greedyAlgo() { // FVS, MDS 
    UDG rest            = this.shuffledClone(); 
    UDG currentSolution = new UDG(); // future MDS or FVS
    while(shouldContinueGreedy.method(currentSolution,rest)) { // FVS while(!isSolution(currentSolution)), MDS while(rest.isNotEmpty()) 
      Vertex theMostConnectedPoint=rest.theMostConnectedPoint();
      currentSolution.add(theMostConnectedPoint.clone());
      rest.removeAll(toRemoveBeforeContinueGreedy.method(theMostConnectedPoint)); // FVS remove(theMostConnectedPointOfRest), MDS removePointAndNeigborhood(theMostConnectedPointOfRest)
    }
    return currentSolution;
  }
	  
  // // // // // // // // // // // // CYCLES 

  public boolean cyclesExist() {
	UDG rest    = this.clone(); 
	UDG visited = new UDG(); 
	while(!rest.isEmpty()) {
      Vertex newlyVisitedP = anyPointFromNeighborhoodOfVisited(visited,rest); 
      rest.remove(newlyVisitedP);
      for (Vertex visitedP1 : visited.vertices) 
        for (Vertex visitedP2 : sublistStartingAfterPoint(visitedP1,visited)) 
          if(isEdge(newlyVisitedP,visitedP1)&&isEdge(newlyVisitedP,visitedP2)) 
            return true;
      visited.add(newlyVisitedP.clone()); 
    }
    return false;
  }
  
/*  private UDG rotateFromNewPointToNewPoint(Vertex newPoint) {
	UDG cycle = new UDG(); 
    for(int i=this.vertices.size()-1; i>=0; i--) {
      cycle.add(vertices.get(i));
      if(vertices.get(i).equals(newPoint)) 
    	break; /// ?
    }
    return cycle;
  }*/

  // // // // // // // // // // // // CYCLES 

  public ArrayList<UDG> elementaryCycles() { // DNS
    markAllVertexWhite(); 

	ArrayList<UDG> allCycles = new ArrayList<UDG>();
    for(Vertex p : vertices) 
   	  allCycles.addAll(calculateElementaryCyclesFromPath(new UDG(p)));
    for(UDG cycle : allCycles) // normalize cycles
	  //cycle = cycle.rotateFromNewPointToNewPoint(newPoint);
	  cycle.rotateAndMayBeInvertDirectionCycle();

    ArrayList<UDG> cyclesWithouDuplicata = new ArrayList<UDG>();
    for(UDG cycle1 : allCycles) {
      boolean cycle1isUnique = true;
      for(UDG cycle2 : allCycles) 
  	    if(cycle1.equalsNormalizedCycle(cycle2)) {
  	      cycle1isUnique=false;
  	      break;
  	    }
      if(cycle1isUnique)	
    	cyclesWithouDuplicata.add(cycle1);
    }

    return cyclesWithouDuplicata;
  }

  public ArrayList<UDG> calculateElementaryCyclesFromPath(UDG path) { 
    Vertex lastPointPath = path.get(path.size()-1);
    lastPointPath.markDominatee(); // grey = points of the path = under verification
    ArrayList<UDG> newlyComputedCycles = new ArrayList<UDG>(); 
	for(Vertex newPoint : this.neighborhoodWithoutCentralPoint(lastPointPath).vertices) {
      if(newPoint.isNotExplored()) {
    	path.add(newPoint);
    	calculateElementaryCyclesFromPath(path);
	  }
	  else if(newPoint.isWhite() && !newPoint.equals(path.get(path.size()-2))) 
	    newlyComputedCycles.add(path); // duplicata possible
    }  
	lastPointPath.markBlack();
    return newlyComputedCycles;
  }

  public boolean equalsNormalizedCycle(UDG cycle2) {
	if(this.size()!=cycle2.size()) 
	  return false;
    for(int i=0; i<this.size(); i++)
	  if(!(this.get(i).x==cycle2.get(i).x && this.get(i).y==cycle2.get(i).y)) 
	    return false;
    return true;
  }
  
  public static String cyclesToString(ArrayList<UDG> cycles) {
    String toReturn = cycles.size()+" cycles:\n";
	  for(UDG cycle : cycles) 
   	    toReturn += cycle.toString()+"\n";
    return toReturn;
  }

  // // // // // // // // // // // // // UTILS - CLONE

  public UDG clone() {
    // to serialize a lambda in Java 8 : possible, strongly discouraged, lambdas may not deserialize properly on another JRE ?
	return new UDG(clone(this.vertices)); 
  }

  public UDG clonePartExternalTo2(UDG g) {
    UDG rest = this.clone();
    rest.removeAll(g);
    return rest; 
  }

  public UDG partExternalTo(UDG g) { // not clone
    ArrayList<Vertex> extrenalPart = new ArrayList<Vertex>(); 
    for(Vertex p : vertices)
      if(!g.contains(p))
    	extrenalPart.add(p);
    return new UDG(extrenalPart);
  }

  public UDG cloneAndAddVertex(Vertex p) {
	UDG clone = this.clone();
	clone.add(p);
    return clone;
  }
  
  public UDG shuffledClone() { 
	UDG clone = this.clone();
    Collections.shuffle(clone.vertices,new Random(System.nanoTime())); 
    return clone;
  }

  public UDG cloneWithoutDuplicata() {
	return new UDG(cloneWithoutDuplicata(this.vertices));
  }	  

  public UDG shuffledCloneWithoutDuplicata() {
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
  
  public UDG neighborhoodWithCentralPoint(Vertex p) { // not clone 
	UDG neighborhood = new UDG();
	for (Vertex candidat: this.vertices) 
	  if (candidat.distance(p)<UDG.edgeThreshold) 
        neighborhood.add(candidat);
	return neighborhood; 
  }

  public UDG neighborhoodWithoutCentralPoint(Vertex p) { // not clone
	UDG neighborhood = neighborhoodWithCentralPoint(p);
	neighborhood.remove(p);
	return neighborhood;
  }

  public UDG notExploredNeighborhoodWithoutCentralPoint(Vertex p) { // not clone 
	UDG neighborhood = new UDG();
	for (Vertex candidat: this.vertices) 
	  if (candidat.distance(p)<UDG.edgeThreshold && candidat.isNotExplored()) 
	    neighborhood.add(candidat);
	neighborhood.remove(p);
	return neighborhood; 
  }

  public UDG notExploredActiveNeighborhoodWithCentralPoint(Vertex p) { // not clone 
	UDG neighborhood = new UDG();
	for (Vertex candidat: this.vertices)
	  if (candidat.distance(p)<UDG.edgeThreshold && candidat.isNotExplored() && candidat.active==true) 
	    neighborhood.add(candidat);
	return neighborhood; 
  }

  public UDG blackNeighborhoodWithoutCentralPoint(Vertex p) { // not clone 
    UDG neighborhood = new UDG();
    // System.out.println("  @ find black neighborhood of "+p.toString()+" in "+this.vertex.toString());
    for (Vertex candidat: this.vertices) 
	  if (candidat.distance(p)<UDG.edgeThreshold && candidat.isBlack()) 
	    neighborhood.add(candidat);
	neighborhood.remove(p);
	return neighborhood; 
  }
  public UDG neighborhoodWithInitialPoints(UDG initialPoints) { // not clone 
	HashSet<Vertex> pointsToAdd = new HashSet<Vertex>(); 
	for(Vertex p : initialPoints.vertices)
	  pointsToAdd.addAll(this.neighborhoodWithCentralPoint(p).vertices);
	initialPoints.addAll(new ArrayList<Vertex>(pointsToAdd));
	return initialPoints; 
  }

  public UDG blackNeighborhoodWithoutInitialPoints(UDG initialPoints) { // not clone 
    return new UDG(blackNeighborhoodWithoutInitialPoints(new HashSet<Vertex>(initialPoints.vertices)));
  }
	  
  public HashSet<Vertex> blackNeighborhoodWithoutInitialPoints(HashSet<Vertex> initialPoints) { // not clone 
	HashSet<Vertex> blackNeighborhood = new HashSet<Vertex>(); 
	for(Vertex initialPoint : initialPoints) 
	  for(Vertex pointCandidat : this.blackNeighborhoodWithoutCentralPoint(initialPoint).vertices) 
		if(!initialPoints.contains(pointCandidat)) 
		  blackNeighborhood.add(pointCandidat);
	return blackNeighborhood; 
  }

  public UDG neighborhoodWithoutInitialPoints(UDG initialPoints) { // not clone 
	UDG toReturn = neighborhoodWithInitialPoints(initialPoints); 
	toReturn.removeAll(initialPoints);
	return toReturn;
  }

  public UDG notExploredVertex() {
	return whiteVertex();
  }
  
  public UDG whiteVertex() {
	UDG white = new UDG();
	for(Vertex p : vertices)
	  if(p.isWhite())
		white.add(p); 
	return white;
  }

  public UDG greyVertex() {
	UDG grey = new UDG();
	for(Vertex p : vertices)
	  if(p.isGrey())
		grey.add(p); 
	return grey;
  }

  public UDG dominatorsVertex() { /// vertices
	return blackVertex();
  }
  
  public UDG blackVertex() {
	UDG black = new UDG();
	for(Vertex p : vertices)
	  if(p.isBlack())
		black.add(p); 
	return black;
  }

  public UDG blackAndBlueVertex() {
	UDG blackAndBlueVertex = new UDG();
	for(Vertex p : vertices)
	  if(p.isBlack() || p.isBlue())
		blackAndBlueVertex.add(p); 
	return blackAndBlueVertex;
  }

  public UDG notExploredActiveVertex() {
    UDG whiteActiveVertex = new UDG();
    for(Vertex p : this.vertices)
      if(p.isNotExploredActive())
    	whiteActiveVertex.add(p);
    return whiteActiveVertex;
  }
	  
  public Map<Vertex,UDG> blackComponents() { // not clone
	Map<Vertex,UDG> mapBlackComponents = new HashMap<Vertex, UDG>(); 
    UDG rest = this.blackVertex().clone();
	while(rest.size()>0) {
	  Vertex blackPoint = rest.get(0);
	  UDG blackComponent = blackComponent(blackPoint);
	  mapBlackComponents.put(blackPoint,blackComponent);
	  rest.removeAll(blackComponent);
	}
    return mapBlackComponents;
  }
  
  public UDG blackComponent(Vertex blackPoint) { // not clone
	HashSet<Vertex> blackComponent = new HashSet<Vertex>();
	blackComponent.add(blackPoint);
	while(true) {
	  HashSet<Vertex> toAdd = blackNeighborhoodWithoutInitialPoints(blackComponent);
	  blackComponent.addAll(toAdd);
	  if(toAdd.size()==0) break; /// ?
	} 
	return new UDG(blackComponent);
  }

  public UDG connectedComponent(Vertex p0) { // not clone
	HashSet<Vertex> component = new HashSet<Vertex>();
	component.add(p0);
	while(true) {
	  HashSet<Vertex> toAdd = new HashSet<Vertex>();
      for(Vertex candidat : this.partExternalTo(new UDG(component)).vertices)
	    for(Vertex pointComponent : component)
		  if(isEdge(candidat,pointComponent))
		    toAdd.add(candidat);
	  component.addAll(toAdd);
	  if(toAdd.size()==0) break; /// ?
	} 
	UDG componentAsUDP = new UDG(component);
	return componentAsUDP;
  }

  public boolean isConnected() { 
	return this.cloneWithoutDuplicata().size()==this.connectedComponent(this.get(0)).size();
  }
  
  // // // // // // // // // // // // // UTILS

  public Vertex barycenter() {
    if(vertices.size()==0) 
	  return null; 
    int summeOfX=0, summeOfY=0;
    for(Vertex p : this.vertices) { /// stream ?
	  summeOfX+=p.x;  
	  summeOfY+=p.y;  
    }
    return new Vertex(summeOfX/vertices.size(),summeOfY/vertices.size());
  }

  public boolean deplacingCentreToBarycenterHasMadeChanges() { /// tmp
	if(this.vertices.size()==0)
	  return false;
	Vertex barycenter=barycenter();
    ///if(barycenter.x==center.x && barycenter.y==center.y ) // compare as objects ?
      ///return false;
   	///this.center = barycenter;
	  //System.out.println("new center = barycenter = "+barycenter);
   	return true;
  }

  public boolean distanceExactlyTwoHops(Vertex p1, Vertex p2) {
    if(isEdge(p1,p2)) return false;
    for(Vertex p : this.vertices)
      if(isEdge(p,p1) && isEdge(p,p2)) return true;
    return false;
  }
  
  public boolean canAddToMisKeepingPropriety(Vertex candidat, UDG misWithPropriety) { 
	//  propriety of lemma 2 "On greedy construction of CDS in wireless networks" Yingshu Thai Wang Yi Wan Du 
    if(misWithPropriety.contains(candidat)) // ?
      return true; 
	for(Vertex pointMis: misWithPropriety.vertices) 
      if(isEdge(candidat,pointMis) || candidat.equals(pointMis))
        return false;
    for(Vertex pointMis: misWithPropriety.vertices) 
      if(distanceExactlyTwoHops(candidat,pointMis))
    	return true;
	return false;
  }

  public boolean hasAsMisWithPropriety(UDG misToVerify) { 
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

  public boolean hasAsMis(UDG misToVerify) { // with or without propriety "2 hops distance"
    for(Vertex p : this.partExternalTo(misToVerify).clone().vertices) 
      if(new UDG(misToVerify.clone().vertices,p).isIndependentSet()) // optimisation possible - verify only p ?
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

  public boolean hasAsCDS(UDG cdsToVerify) { 
    return this.hasAsDS(cdsToVerify) && cdsToVerify.isConnected();
  }
  
  public boolean hasAsDS(UDG dsToVerify) { 
	if(dsToVerify.isEmpty()) return false;
    for(Vertex p : vertices) {
      boolean pointIsVisited=false;
      for(Vertex pDs : dsToVerify.vertices)  
	    if (isEdgeOrEqualPoints(p,pDs)) 
	      pointIsVisited=true;
      if(!pointIsVisited)
        return false;
    }
   return true;
  }

  public boolean hasAsFVS(UDG fvsToVerify) {
	return !this.partExternalTo(fvsToVerify).clone().cyclesExist();
  }
  
  public Vertex anyNotExploredActiveVertex() { 
	for(Vertex p : vertices)
	  if(p.isWhite() && p.active==true)
		return p;
    return null;
  }
  
  public Vertex anyWhiteVertex() {
	for(Vertex p : vertices)
	  if(p.isWhite())
		return p;
    return null;
  }
  
  public Vertex anyGreyVertex() {
	for(Vertex p : vertices)
	  if(p.isGrey())
		return p;
    return null;
  }
  
  public Vertex vertexHighest_dAsterix_id() {
	Vertex vertexHighest_dAsterix_id=vertices.get(0);
	for(Vertex candidat : vertices)
	  if(   candidat.countEffectiveDegree(this)> vertexHighest_dAsterix_id.countEffectiveDegree(this) 
	    || (candidat.countEffectiveDegree(this)==vertexHighest_dAsterix_id.countEffectiveDegree(this) 
	        &&
		    this.indexOf(candidat) > this.indexOf(vertexHighest_dAsterix_id)                         )  )           
        vertexHighest_dAsterix_id=candidat;
    return vertexHighest_dAsterix_id;
  }
  
  private static ArrayList<Vertex> sublistStartingAfterPoint(Vertex p, UDG g) {
    return new ArrayList<Vertex>(g.vertices.subList(g.vertices.indexOf(p)+1,g.vertices.size()));
  }
	  
  private void rotateAndMayBeInvertDirectionCycle() { // start with left top point 
	ArrayList<Vertex> normalizedPath = clone(this.vertices);
	Vertex leftTopPoint = this.leftTopPoint();
	while (normalizedPath.get(0) != leftTopPoint) {
	  Vertex toDeplace = normalizedPath.get(0);
	  normalizedPath.remove(toDeplace);
	  normalizedPath.add(toDeplace);
	}
    this.vertices = normalizedPath;

    Vertex secondPoint = normalizedPath.get(1);
	Vertex lastPoint   = normalizedPath.get(normalizedPath.size()-1);
    if ( secondPoint.x>lastPoint.x || (secondPoint.x==lastPoint.x && secondPoint.y>lastPoint.y) ) 
      this.invertDirectionCycle();
  }

  private void invertDirectionCycle() { 
    UDG invertedPoints = new UDG();
    invertedPoints.add(this.vertices.get(0));
	for (int i=this.vertices.size()-1; i>=1; i--)
	  invertedPoints.add(vertices.get(i));
	this.vertices = invertedPoints.vertices;
  }

  private Vertex leftTopPoint() {
    Vertex leftTopPoint = this.vertices.get(0);
	for (Vertex p : this.vertices)
	  if ( p.x<leftTopPoint.x || (p.x==leftTopPoint.x && p.y<leftTopPoint.y) )
		leftTopPoint = p;
	return leftTopPoint;
  }

  public boolean everyPointMayBeExceptOneHasDegreeTwo() {
    boolean weHaveAlreadySeeDegreeNoEqualsToTwo=false;
	for(Vertex p : this.vertices){
      if(p.getDegree(this)!=2) {
        if(weHaveAlreadySeeDegreeNoEqualsToTwo) 
  	      return false;
        else 
  	      weHaveAlreadySeeDegreeNoEqualsToTwo = true;
      }
    }
    return true;
  }

  private Vertex anyPointFromNeighborhoodOfVisited(UDG visitedConnComp, UDG rest) {
	if(rest.size()==0) 
	  return null;
	if(rest.neighborhoodWithoutInitialPoints(visitedConnComp).size()>0) 
	  return rest.neighborhoodWithoutInitialPoints(visitedConnComp).get(0);
	return rest.get(0); 
  }
  
  public Vertex theMostConnectedPoint() {
    if(vertices.size()==0) return null;
    Vertex theMostConnectedPoint=vertices.get(0);
    for (Vertex p: vertices) 
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
	if(n>=this.vertices.size() || n<0) return null;
	return this.vertices.get(n);
  }
  
  public void markAllVertexWhite() {
    for(Vertex p : this.vertices) 
  	  p.color = Color.WHITE;
  }

  public void markAllVertexBlack() {
    for(Vertex p : this.vertices) 
  	  p.color = Color.BLACK;
  }

  public void markVertexBlack(UDG subG) {
    for(Vertex p : this.vertices)
      for(Vertex q : subG.vertices) 
  	    if(p.equals(q))
  	      p.color = Color.BLACK;
  }

  public void markAllVertexGrey() {
    for(Vertex p : this.vertices) 
  	  p.color = Color.GREY;
  }

  public int nbPointsOfColor(Color color) {
	int nb=0;
    for (Vertex p : this.vertices)
      if(p.color.equals(color))
    	nb++;
    return nb;
  }
	  
  public void add(Vertex p) {
	this.vertices.add(p);
  }

  public void add(int n, Vertex p) {
	vertices.add(n,p);
  }
  
  public void add(UDG g) {
	vertices.addAll(g.vertices);
  }
  
  public void addAll(ArrayList<Vertex> pp) {
	vertices.addAll(pp);
  }

  public Vertex remove(int n) {
	if(n>=vertices.size()) return null;
	return vertices.remove(n); 
  }

  public boolean remove(Vertex toRemove) {
	return vertices.remove(toRemove);
  }

  public boolean remove(ArrayList<Vertex> toRemove) {
	return vertices.removeAll(toRemove);
  }

  public boolean removeAll(UDG g) {
	ArrayList<Vertex> toRemove = g.clone().vertices;
	return vertices.removeAll(toRemove);
  }

  public int indexOf(Vertex p) {
	return this.vertices.indexOf(p);  
  }
  
  public boolean removePointAndItsNeigborhood(Vertex p) {
	ArrayList<Vertex> toRemove = this.neighborhoodWithCentralPoint(p).clone().vertices;
	return vertices.removeAll(toRemove);
  }
 
  public boolean contains(Vertex p) {
	return vertices.contains(p);
  }
  
  public int size() {
	return vertices.size();
  }
  
  public int length() {
	return vertices.size();
  }
  
  public boolean isEmpty() {
	return vertices.size()==0;
  }
  
  public boolean isNotEmpty() {
	return vertices.size()!=0;
  }
  
  public void clear() {
	vertices.clear();
  }

  public ArrayList<Point> getSolutionAsPoints() {
	ArrayList<Point> toReturn = new ArrayList<Point>();
	for(Vertex p : vertices)
	  toReturn.add(new Point(p.x,p.y));
	return toReturn;
  }

  public ArrayList<Point> convertToPoints() {
	ArrayList<Point> points = new ArrayList<Point>();
	for(Vertex p : this.vertices)
	  points.add(new Point(p.x,p.y));
	return points;
  }

  public static boolean isEdge(Vertex p1, Vertex p2) {
	return !p1.equals(p2) && p1.distance(p2) < edgeThreshold; /// p1==p2 ?
  }
  
/*  public ArrayList<Edge> edges() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
	for(int i=0; i<this.size(); i++) {
	  Vertex p1 = vertices.get(i);
	  for(int j=i+1; j<this.size(); j++) {
	    Vertex p2 = vertices.get(j);
	    if(isEdgeNotEqualPoints(p1,p2))
	      edges.add(new Edge(p1,p2));
	  }
	}
	return edges;
  }*/
  
/*  public ArrayList<Edge> allEdgesContaining(Vertex p) {
    ArrayList<Edge> edgesContainingPoint = new ArrayList<Edge>();
	for(Edge edge : this.edges())
	  if(edge.contains(p))
  	    edgesContainingPoint.add(edge);
    return edgesContainingPoint;
  }*/

  public String toString() {
    if(this.size()==0) return " vide";
	String toReturn=" "+this.size()+":";
    for(Vertex p : vertices)
      if(p!=null)
    	toReturn += "["+p.x+","+p.y+"]";
      else
    	toReturn += "[null]";
	return toReturn;
  }
  
  public String toStringWithColorsDegrees() {
    if(this.size()==0) return " vide";
    String toReturn=this.size()+" vertex: ";
    for(Vertex p : vertices) 
      if(p==null) toReturn += "[null]";
      else        toReturn += p.toString();
	return toReturn;
  }
  
  public void cleanup() { // enleve les points à degrée<=1
	ArrayList<Vertex> toRemove = new ArrayList<Vertex>();
	for(Vertex p : this.vertices)
	  if(p.getDegree(this)>1)
		toRemove.add(p);
	this.vertices.removeAll(toRemove);
  }
  
  public int score() {
	return this.size();
  }
}  