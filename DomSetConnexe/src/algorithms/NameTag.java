package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class NameTag {
	private ArrayList<Point> points;
	private int[] tag;

	@SuppressWarnings("unchecked")
	public NameTag(ArrayList<Point> points) {
		this.points = (ArrayList<Point>) points.clone();
		tag = new int[points.size()];
		for (int i = 0; i < points.size(); i++)
			tag[i] = i;
	}

	public void reTag(int j, int k) {
		for (int i = 0; i < tag.length; i++)
			if (tag[i] == j)
				tag[i] = k;
	}

	public int tag(Point p) {
		for (int i = 0; i < points.size(); i++)
			if (p.equals(points.get(i)))
				return tag[i];
		return 0xBADC0DE;
	}
}