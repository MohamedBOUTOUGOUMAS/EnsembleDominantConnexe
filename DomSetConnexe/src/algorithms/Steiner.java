package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Steiner {
	
	static ArrayList<Point> grapheH = new ArrayList<>();
	static double longueur = 0;
	static ArrayList<Point> bordure = new ArrayList<Point>();
	static double lastValueLong = 0;
	static double[][] dist;
	
	
	public int[][] calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {

		dist = new double[points.size()][points.size()];
		int[][] paths = new int[points.size()][points.size()];

		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist.length; j++) {

				if (points.get(i).distance(points.get(j)) <= edgeThreshold) {

					dist[i][j] = points.get(i).distance(points.get(j));
				} else {
					dist[i][j] = Double.POSITIVE_INFINITY;
				}
				paths[i][j] = j;

			}
		}

		for (int k = 0; k < dist.length; k++) {
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist.length; j++) {
					if (dist[i][j] > dist[i][k] + dist[k][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						paths[i][j] = paths[i][k];
					}
				}
			}
		}

		return paths;
	}
	
	public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {

		int[][] paths = calculShortestPaths(points, edgeThreshold);

		ArrayList<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < hitPoints.size(); i++) {
			for (int j = 0; j < hitPoints.size(); j++) {
				Point p = hitPoints.get(i);
				Point q = hitPoints.get(j);
				if (p.equals(q) || Tools.contains(edges, p, q))
					continue;

				Edge e = new Edge(p, q, dist[points.indexOf(p)][points.indexOf(q)]);
				edges.add(e);
			}
		}

		ArrayList<Edge> tzero = Tools.krus1(hitPoints, edges);

		ArrayList<Point> grapheH = createGrapheH(tzero, points, paths);
		Set<Point> set = new HashSet<>(grapheH);
		ArrayList<Point> h = new ArrayList<>(set);

		Tree2D tprim = Tools.calculKruskal(h);
		return tprim;
	}

	public static void createGrapheH(ArrayList<Edge> tzero, double budget, ArrayList<Point> points, int[][] paths,
			int hitpointsSize, int index) {

		Point racine = bordure.get(index);
		ArrayList<Edge> edges = (ArrayList<Edge>) tzero.clone();
		ArrayList<Point> subRoots = new ArrayList<>();

		for (Edge e : tzero) {
			if (racine.equals(e.p)) {

				ArrayList<Integer> chemin = Tools.pathFinder(points.indexOf(racine), points.indexOf(e.q), paths);
				double distMax = dist[chemin.get(0)][chemin.get(chemin.size() - 1)];
				double dinside = 0;

				for (int i = 0; i < chemin.size() - 1; i++) {

					if (longueur + (distMax - dinside) < budget) {
						if (grapheH.contains(points.get(chemin.get(i)))
								&& grapheH.contains(points.get(chemin.get(i + 1)))) {
							continue;
						}
						double d = dist[chemin.get(i)][chemin.get(i + 1)];
						grapheH.add(points.get(chemin.get(i)));
						grapheH.add(points.get(chemin.get(i + 1)));
						dinside += d;
					}
				}

				longueur += dinside;
				subRoots.add(e.q);
				edges.remove(e);
			}
			if (racine.equals(e.q)) {

				ArrayList<Integer> chemin = Tools.pathFinder(points.indexOf(racine), points.indexOf(e.p), paths);
				double distMax = dist[chemin.get(0)][chemin.get(chemin.size() - 1)];
				double dinside = 0;

				for (int i = 0; i < chemin.size() - 1; i++) {
					if (longueur + (distMax - dinside) < budget) {
						if (grapheH.contains(points.get(chemin.get(i)))
								&& grapheH.contains(points.get(chemin.get(i + 1)))) {
							continue;
						}
						double d = dist[chemin.get(i)][chemin.get(i + 1)];
						grapheH.add(points.get(chemin.get(i)));
						grapheH.add(points.get(chemin.get(i + 1)));
						dinside += d;
					}
				}
				longueur += dinside;
				subRoots.add(e.p);
				edges.remove(e);
			}
		}

		for (int i = 0; i < subRoots.size(); i++) {
			if (bordure.contains(subRoots.get(i))) {
				continue;
			}
			bordure.add(subRoots.get(i));
		}

		if (index < bordure.size() - 1) {
			index++;
			createGrapheH(tzero, budget, points, paths, hitpointsSize, index);
		}

	}

	public static ArrayList<Point> createGrapheH(ArrayList<Edge> tzero, ArrayList<Point> points, int[][] paths) {

		ArrayList<Point> grapheH = new ArrayList<>();

		for (Edge a : tzero) {

			Integer u = points.indexOf(a.p);
			Integer v = points.indexOf(a.q);

			ArrayList<Integer> chemin = Tools.pathFinder(u, v, paths);

			for (int i = 0; i < chemin.size(); i++) {

				if (grapheH.contains(points.get(chemin.get(i)))) {
					continue;
				}

				grapheH.add(points.get(chemin.get(i)));
			}
		}

		return grapheH;
	}

	public Tree2D calculSteinerBudget(ArrayList<Point> points, int edgeThreshold,
			ArrayList<Point> hitPoints) {

		int[][] paths = calculShortestPaths(points, edgeThreshold);

		ArrayList<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < hitPoints.size(); i++) {
			for (int j = 0; j < hitPoints.size(); j++) {
				Point p = hitPoints.get(i);
				Point q = hitPoints.get(j);
				if (p.equals(q) || Tools.contains(edges, p, q))
					continue;

				Edge e = new Edge(p, q, dist[points.indexOf(p)][points.indexOf(q)]);
				edges.add(e);
			}
		}

		ArrayList<Edge> tzero = Tools.krus1(hitPoints, edges);
		bordure.add(hitPoints.get(0));
		createGrapheH(tzero, 1664, points, paths, hitPoints.size(), 0);

		Set<Point> set = new HashSet<>(grapheH);
		ArrayList<Point> h = new ArrayList<>(set);
		Tree2D tprim = Tools.calculKruskal(h);

		return tprim;
	}

}
