package RideSharing;

import java.util.*;

class CityGraph {
	private Map<String, Map<String, Integer>> adjacencyList;

	public CityGraph() {
		adjacencyList = new HashMap<>();
		initializeCitiesAndRoutes();
	}

	void initializeCitiesAndRoutes() {
		addCity("Bengaluru");
		addCity("Mysore");
		addCity("Hampi");
		addCity("Udupi");
		addCity("Mangalore");
		addCity("Chennai");
		addCity("Madurai");
		addCity("Salem");
		addCity("Trichy");
		addCity("Pondicherry");

		addRoute("Bengaluru", "Mysore", 150);
		addRoute("Bengaluru", "Hampi", 340);
		addRoute("Bengaluru", "Udupi", 400);
		addRoute("Bengaluru", "Mangalore", 350);
		addRoute("Mysore", "Chennai", 500);
		addRoute("Mysore", "Salem", 200);
		addRoute("Hampi", "Udupi", 300);
		addRoute("Udupi", "Mangalore", 100);
		addRoute("Mangalore", "Chennai", 700);
		addRoute("Chennai", "Madurai", 450);
		addRoute("Chennai", "Trichy", 320);
		addRoute("Madurai", "Trichy", 150);
		addRoute("Trichy", "Pondicherry", 250);
		addRoute("Salem", "Pondicherry", 300);
	}

	public void addCity(String city) {
		adjacencyList.putIfAbsent(city, new HashMap<>());
	}

	public void addRoute(String city1, String city2, int distance) {
		adjacencyList.get(city1).put(city2, distance);
		adjacencyList.get(city2).put(city1, distance);
	}

	public List<String> getAvailableCities() {
		return new ArrayList<>(adjacencyList.keySet());
	}

	public List<String> findShortestPath(String start, String end) {
		if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
			return new ArrayList<>();
		}

		Map<String, Integer> distances = new HashMap<>();
		Map<String, String> previous = new HashMap<>();
		PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

		for (String city : adjacencyList.keySet()) {
			distances.put(city, Integer.MAX_VALUE);
		}
		distances.put(start, 0);
		queue.add(start);

		while (!queue.isEmpty()) {
			String current = queue.poll();
			if (current.equals(end)) {
				break;
			}
			for (Map.Entry<String, Integer> neighbor : adjacencyList.get(current).entrySet()) {
				int newDistance = distances.get(current) + neighbor.getValue();
				if (newDistance < distances.get(neighbor.getKey())) {
					distances.put(neighbor.getKey(), newDistance);
					previous.put(neighbor.getKey(), current);
					queue.add(neighbor.getKey());
				}
			}
		}

		List<String> path = new ArrayList<>();
		for (String at = end; at != null; at = previous.get(at)) {
			path.add(at);
		}
		Collections.reverse(path);

		if (path.size() == 1 && !path.get(0).equals(start)) {
			return new ArrayList<>();
		}
		return path;
	}

	public int calculateTotalDistance(List<String> path) {
		int totalDistance = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			String currentCity = path.get(i);
			String nextCity = path.get(i + 1);
			totalDistance += adjacencyList.get(currentCity).get(nextCity);
		}
		return totalDistance;
	}
}