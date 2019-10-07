package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tools {

	public static Tree2D calculKruskal(ArrayList<Point> initpoints) {

		ArrayList<Edge> kruskal = new ArrayList<Edge>();

		try {

			Set<Edge> edgeSet = new HashSet<Edge>();

			for (int i = 0; i < initpoints.size(); i++) {
				for (int j = 0; j < initpoints.size(); j++) {
					if (i == j)
						continue;
					edgeSet.add(new Edge(initpoints.get(i), initpoints.get(j),
							initpoints.get(i).distance(initpoints.get(j))));
				}
			}

			List<Edge> edgeList = new ArrayList<Edge>();
			edgeList.addAll(edgeSet);

			edgeList.sort(Comparator.comparingDouble(Edge::dist));

			NameTag forest = new NameTag(initpoints);
			while (edgeList.size() != 0) {
				Edge current;
				current = edgeList.remove(0);
				if (forest.tag(current.p) != forest.tag(current.q)) {
					kruskal.add(current);
					forest.reTag(forest.tag(current.p), forest.tag(current.q));
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Tree2D myResult = edgeListToTree(kruskal);
		return myResult;
	}

	public static Tree2D edgeListToTree(ArrayList<Edge> edges) {
		return edgeListToTreeR(edges.get(0).p, edges);
	}

	private static Tree2D edgeListToTreeR(Point r, ArrayList<Edge> initEdge) {
		ArrayList<Edge> edges = (ArrayList<Edge>) initEdge.clone();
		ArrayList<Point> subRoots = new ArrayList<>();

		for (Edge e : initEdge) {
			if (r.equals(e.p)) {
				subRoots.add(e.q);
				edges.remove(e);
			}
			if (r.equals(e.q)) {
				subRoots.add(e.p);
				edges.remove(e);
			}
		}
		ArrayList<Tree2D> subTrees = new ArrayList<Tree2D>();

		for (Point subRoot : subRoots) {
			subTrees.add(edgeListToTreeR(subRoot, edges));
		}

		return new Tree2D(r, subTrees);
	}

	public static ArrayList<Edge> krus1(ArrayList<Point> points, ArrayList<Edge> edges) {

		Collections.sort(edges, new Comparator<Edge>() {

			public int compare(Edge e1, Edge e2) {
				return (int) (e1.dist - e2.dist);
			}
		});

		ArrayList<Edge> kruskal = new ArrayList<Edge>();

		NameTag forest = new NameTag(points);
		while (edges.size() != 0) {
			Edge current;
			current = edges.remove(0);
			if (forest.tag(current.p) != forest.tag(current.q)) {
				kruskal.add(current);
				forest.reTag(forest.tag(current.p), forest.tag(current.q));
			}
		}

		return kruskal;
	}

	public static boolean contains(ArrayList<Edge> edges, Point p, Point q) {
		for (Edge e : edges) {
			if (e.p.equals(p) && e.q.equals(q) || e.p.equals(q) && e.q.equals(p))
				return true;
		}
		return false;
	}

	public static ArrayList<Integer> pathFinder(int u, int v, int[][] paths) {
		ArrayList<Integer> chemin = new ArrayList<>();
		if (paths[u][v] == -1) {
			return chemin;
		}
		chemin.add(u);
		while (u != v) {
			u = paths[u][v];
			chemin.add(u);
		}
		return chemin;
	}

}