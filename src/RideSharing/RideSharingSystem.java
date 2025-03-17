package RideSharing;

import java.util.*;

class RideSharingSystem {
	private Map<String, User> users = new HashMap<>();
	private Queue<Ride> rideRequests = new LinkedList<>();
	private Set<String> rideIds = new HashSet<>();
	private CityGraph cityGraph = new CityGraph();

	public RideSharingSystem() {
		cityGraph.initializeCitiesAndRoutes();
	}

	public List<String> getAvailableCities() {
		return cityGraph.getAvailableCities();
	}

	public void registerUser(User user, String parentId) {
		if (!users.containsKey(user.id)) {
			users.put(user.id, user);
			System.out.println(user.name + " registered successfully!");
		} else {
			System.out.println("User already exists!");
		}
	}

	public boolean loginUser(String userId, String password) {
		User user = users.get(userId);
		if (user != null && user.validatePassword(password)) {
			System.out.println("Login successful! Welcome, " + user.name);
			return true;
		}
		System.out.println("Invalid credentials!");
		return false;
	}

	public void requestRide(String riderId, String driverId, String destination) {
		if (!cityGraph.getAvailableCities().contains(destination)) {
			System.out.println("Sorry, we don't offer rides to the requested city!");
			return;
		}
		if (users.containsKey(riderId) && users.containsKey(driverId)) {
			Rider rider = (Rider) users.get(riderId);
			Driver driver = (Driver) users.get(driverId);
			String rideId = "RIDE" + (rideIds.size() + 1);
			Ride ride = new Ride(rideId, rider, driver, destination);
			rideRequests.add(ride);
			rideIds.add(rideId);
			rider.addRideToHistory(ride);
			driver.addRideToHistory(ride);
			System.out.println("Ride requested: " + rideId + " | Rider: " + rider.name + " | Driver: " + driver.name
					+ " | Destination: " + destination);
		} else {
			System.out.println("Invalid rider or driver ID.");
		}
	}

	public void processNextRide() {
		if (!rideRequests.isEmpty()) {
			Ride ride = rideRequests.poll();
			System.out.println("Processing ride: " + ride.rideId + " for " + ride.rider.name + " with driver "
					+ ride.driver.name + " to " + ride.destination);
		} else {
			System.out.println("No rides in queue.");
		}
	}

	public void rateRide(String rideId, int rating) {
		for (User user : users.values()) {
			for (Ride ride : user.rideHistory) {
				if (ride.rideId.equals(rideId)) {
					ride.rateRide(rating);
					System.out.println("Ride " + rideId + " rated with " + rating + " stars.");
					return;
				}
			}
		}
		System.out.println("Ride ID not found.");
	}

	public User getUser(String userId) {
		return users.get(userId);
	}

	public List<String> searchDriversByLocation(String destination) {
		List<String> driverNames = new ArrayList<>();
		for (User user : users.values()) {
			if (user instanceof Driver) {
				for (Ride ride : user.rideHistory) {
					if (ride.destination.equals(destination)) {
						driverNames.add(user.name);
						break;
					}
				}
			}
		}
		if (driverNames.isEmpty()) {
			System.out.println("No drivers found for the given destination.");
			return driverNames;
		}
		Collections.sort(driverNames);
		return driverNames;
	}

	public String findShortestPathWithDistance(String start, String end) {
		List<String> path = cityGraph.findShortestPath(start, end);
		if (path.isEmpty()) {
			return "No path found between " + start + " and " + end;
		}
		int distance = cityGraph.calculateTotalDistance(path);
		return "Shortest Path: " + String.join(" -> ", path) + " | Total Distance: " + distance + " km";
	}

	public List<String> getAllDrivers() {
		List<String> drivers = new ArrayList<>();
		for (User user : users.values()) {
			if (user instanceof Driver) {
				drivers.add(user.id + " - " + user.name);
			}
		}
		return drivers;
	}
}