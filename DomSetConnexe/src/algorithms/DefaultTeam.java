package algorithms;

import java.awt.Point;
import java.util.ArrayList;


public class DefaultTeam {

	

	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {

		DomSet d = new DomSet();
		Steiner st = new Steiner();
		System.out.println("begin domSet");
		ArrayList<Point> domSet = d.calculDominatingSet(points, edgeThreshold);
		System.out.println(domSet.size());
		System.out.println("begin steiner");
		Tree2D steiner = st.calculSteiner(points, edgeThreshold, domSet);
		
		return steiner.getPoints();
//		return domSet;
	}

	

	

}
