package algorithms;

import java.awt.Point;

public class Edge {
	public Point p;
	public Point q;
	public double dist;

	public Edge(Point p, Point q, double d) {
		this.p = p;
		this.q = q;
		this.dist = d;
	}

	public double dist() {
		return dist;
	}

	Point getP() {
		return p;
	}

	Point getQ() {
		return q;
	}
}