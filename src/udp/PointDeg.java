package udp;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe PointDeg heritant de point. Elle contient le degre et le poids de ce point
 * @author Eric, Kevin
 *
 */
public class PointDeg extends Point {
	public int degree;
	public double weight = 1;

	public PointDeg(Point p, UDP graph){
		super(p);
		// this.degree = graph.getDegree(p); ///
		this.weight = 1;
	}
	
	public PointDeg(Point p, int degree){
		super(p);
		this.degree = degree;
		this.weight = 1;
	}

	public PointDeg(int x, int y){
		super(x, y);
		degree = 0;
		this.weight = 1;
	}
	
	public PointDeg(){
		super();
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public PointDeg clone(){
		PointDeg p = new PointDeg(this.x, this.y);
		p.setDegree(this.degree);
		p.setWeight(this.weight);
		return p;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof PointDeg){
			PointDeg o = (PointDeg) obj;
			return this.x == o.x && this.y == o.y && this.weight == o.weight && this.degree == o.degree;
		}
		
		return false;
	}
}