package algorithms;


public class B {
/*    	 // le poids minimal des points passe en parametre
    	public static double minWeight(ArrayList<PointDeg> points){
    		double min = Double.MAX_VALUE;
    		for(PointDeg p : points){
    			if(p.weight < min){
    				min = p.weight;
    			}
    		}
    		return min;
    	}

    	// Retourne un double contenant le w(u)/(d(u) - 1) d'un point u minimal des points passes en parametre
    	public static double minWeight2(ArrayList<PointDeg> points){
    		double min = Double.MAX_VALUE;	

    		for(PointDeg p : points){
    			if((p.weight/(p.degree - 1.0)) < min){
    				min = p.weight/(p.degree - 1.0);
    			}
    		}

    		return min;
    	}*/
    	

	////
	/*   	// Modifie le poids de chaque point dans le cycle semi-disjoint (correspond a w(u)-=gamma, tout u in V(C)
   	public static ArrayList<Point> changeWeight(ArrayList<Point> src, ArrayList<Point> sscycle, double gamma){
	  for(Point p : src)
    	for(Point q : sscycle)
    	  if(p.equals(q)){
    		p.setWeight(p.getWeight() - gamma);
    		break;
    	  }
	  return src;
    }

    	// Modifie le poids de chaque point du graphe. (correspond a w(u) <- w(u) - gamma(d(u) - 1)
    	public static ArrayList<PointDeg> changeWeight2(ArrayList<PointDeg> src, double gamma){
    		for(PointDeg p : src){
    			p.setWeight(p.getWeight() - gamma * (p.getDegree() - 1));
    		}

    		return src;
    	}
    }

	private static boolean isMember(ArrayList<Point> points, Point p){
		for (Point point:points) if (point.equals(p)) return true; return false;
	}

	public static ArrayList<Point> neighbor(Point p, ArrayList<Point> vertices){
		ArrayList<Point> result = new ArrayList<Point>();

		for (Point point:vertices) if (point.distance(p)<100 && !point.equals(p)) result.add((Point)point.clone());

		return result;
	}
	
	*/

	/*// // // // // // // // // 
	 *     	public static boolean isValide(ArrayList<Point> origPoints, ArrayList<Point> fvs){
    		ArrayList<Point> vertices = new ArrayList<Point>();
    		for (Point p:origPoints) if (!isMember(fvs,p)) vertices.add((Point)p.clone());

    		//Looking for loops in subgraph induced by origPoint \setminus fvs
    		while (!vertices.isEmpty()){
    			ArrayList<Point> green = new ArrayList<Point>();
    			green.add((Point)vertices.get(0).clone());
    			ArrayList<Point> black = new ArrayList<Point>();

    			while (!green.isEmpty()){
    				for (Point p:neighbor(green.get(0),vertices)){
    					if (green.get(0).equals(p)) continue;
    					if (isMember(black,p)) return false;
    					if (isMember(green,p)) return false;
    					green.add((Point)p.clone());
    				}
    				black.add((Point)green.get(0).clone());
    				vertices.remove(green.get(0));
    				green.remove(0);
    			}
    		}

    		return true;
    	}
*/
}