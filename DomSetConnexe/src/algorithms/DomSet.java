package algorithms;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;

public class DomSet {

	static Map<Point, ArrayList<Point>> graph;
	static Map<Point, ArrayList<Point>> graphStatic;
	static ArrayList<Node> nodes;

	public void createGraph(ArrayList<Point> points, int edgeThreshold) {
		graph = new HashMap<>();
		nodes = new ArrayList<>();
		graphStatic = new HashMap<>();

		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (!graph.containsKey(p)) {
				ArrayList<Point> l = new ArrayList<>();
				graph.put(p, l);
				graphStatic.put(p, l);
			}

			for (int j = i + 1; j < points.size(); j++) {
				Point q = points.get(j);
				if (!graph.containsKey(q)) {
					ArrayList<Point> l2 = new ArrayList<>();
					graph.put(q, l2);
					graphStatic.put(q, l2);
				}

				if (p.distance(q) <= edgeThreshold) {

					if (graph.containsKey(p)) {
						graph.get(p).add(q);
						graphStatic.get(p).add(q);
					}

					if (graph.containsKey(q)) {
						graph.get(q).add(p);
						graphStatic.get(q).add(p);
					}
				}
			}
		}

		for (Entry<Point, ArrayList<Point>> e : graph.entrySet()) {

			Node x = new Node(e.getKey(), e.getValue().size());
			nodes.add(x);
		}

		sortNodes(nodes);

	}

	public void sortNodes(ArrayList<Node> nodes) {
		Collections.sort(nodes, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return (int) (n1.degree - n2.degree);
			}
		});
	}

	public ArrayList<Point> getNeighbors(ArrayList<Point> dominants) {
		ArrayList<Point> res = new ArrayList<Point>();
		for (Point p : dominants) {
			if (graphStatic.containsKey(p))
				res.addAll(graphStatic.get(p));
		}

		return res;
	}

	@SuppressWarnings("unchecked")
	public boolean isValid(ArrayList<Point> points, ArrayList<Point> dominants) {
		ArrayList<Point> clone = (ArrayList<Point>) points.clone();

		clone.removeAll(dominants);
		clone.removeAll(getNeighbors(dominants));

		if (clone.size() > 0)
			return false;

		return true;
	}

	public void synchronize(Point n) {

		if (graph.containsKey(n)) {
			nodes.clear();
			int siz = graph.get(n).size();
			for (int i = 0; i < siz; i++) {
				Point v = graph.get(n).get(i);
				graph.get(v).remove(n);
				siz = graph.get(n).size();
			}
			graph.remove(n);

			for (Entry<Point, ArrayList<Point>> e : graph.entrySet()) {

				Node x = new Node(e.getKey(), e.getValue().size());
				nodes.add(x);
			}
			sortNodes(nodes);
		}

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Point> getDominsNaif(ArrayList<Point> points) {

		ArrayList<Point> tmp = new ArrayList<Point>();
		while (!isValid(points, tmp) && nodes.size() > 0) {

			ArrayList<Point> minDegs = new ArrayList<>();
			Node n = nodes.get(0);

			int i = 0;
			while (i < nodes.size() && nodes.get(i).degree == n.degree) {
				minDegs.add(nodes.get(i).s);
				i++;
			}
			Collections.shuffle(minDegs);
			Point min = minDegs.get((int) Math.random() * minDegs.size());

			ArrayList<Point> voisins = graph.get(min);
			Point bestPoint = min;
			int bestDeg = Integer.MIN_VALUE;
			ArrayList<Point> maxDegs = new ArrayList<>();
			for (Point v : voisins) {
				if (graph.get(v).size() > bestDeg) {
					maxDegs.clear();
					maxDegs.add(v);
					bestDeg = graph.get(v).size();
				} else if (graph.get(v).size() == bestDeg) {
					maxDegs.add(v);
				}
			}

			Collections.shuffle(maxDegs);
			if (maxDegs.size() > 0) {
				bestPoint = maxDegs.get((int) Math.random() * maxDegs.size());
			}

			tmp.add(bestPoint);

			ArrayList<Point> voisinBest = (ArrayList<Point>) graph.get(bestPoint).clone();
			for (Point v1 : voisinBest) {
				synchronize(v1);
			}
			synchronize(bestPoint);

		}

		return tmp;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Point> improve(ArrayList<Point> origPoints, ArrayList<Point> solution, ArrayList<Point> reste,
			int edgeThreshold) {
		ArrayList<Point> solutionPrime;
		Collections.shuffle(solution);
		for (int i = 0; i < solution.size(); i++) {
			Point p = solution.get(i);

			for (int j = i + 1; j < solution.size(); j++) {
				Point q = solution.get(j);

				if (p.distance(q) > 3 * edgeThreshold)
					continue;

				for (int r = 0; r < reste.size(); r++) {
					Point re = reste.get(r);

					if ((p.distance(re) > 2 * edgeThreshold) || (q.distance(re) > 2 * edgeThreshold))
						continue;

					solutionPrime = (ArrayList<Point>) solution.clone();
					solutionPrime.remove(p);
					solutionPrime.remove(q);
					solutionPrime.add(re);
					if (isValid(origPoints, solutionPrime)) {
						return solutionPrime;
					}
				}
			}
		}

		return solution;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Point> localSearch(ArrayList<Point> points, ArrayList<Point> fvs, int edgeThreshold, int k) {

		ArrayList<Point> res = (ArrayList<Point>) fvs.clone();
		int oldSolSize = 0;
		Collections.shuffle(fvs);
		while (oldSolSize != res.size()) {
			ArrayList<Point> l1 = (ArrayList<Point>) points.clone();
			l1.removeAll(res);
			if (k == 2) {
				ArrayList<Point> res1 = improve(points, res, l1, edgeThreshold);
				oldSolSize = res.size();
				res = res1;
			} else {
				break;
			}
		}
		return res;
	}

	public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {

		createGraph(points, edgeThreshold);

		ArrayList<Point> naif = getDominsNaif(points);
		for (int i = 0; i < 200; i++) {
			Collections.shuffle(points);
			createGraph(points, edgeThreshold);
			ArrayList<Point> res1 = getDominsNaif(points);
			if (naif.size() > res1.size()) {
				naif = res1;
			}
		}
		Collections.shuffle(naif);
		createGraph(points, edgeThreshold);
		ArrayList<Point> res = localSearch(points, naif, edgeThreshold, 2);
		return res;
	}

}
