package RideSharing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

class User {
    String id;
    String name;
    String passwordHash;
    List<Ride> rideHistory;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.passwordHash = hashPassword(password);
        this.rideHistory = new ArrayList<>();
    }

    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }

    public void viewRideHistory() {
        System.out.println(name + "'s Ride History:");
        for (Ride ride : rideHistory) {
            System.out.println("Ride ID: " + ride.rideId + " | Driver: " + ride.driver.name + " | Destination: "
                    + ride.destination + " | Rating: " + ride.rating);
        }
    }

	public void sortRideHistoryByDestination() {
		if (rideHistory.isEmpty()) {
			System.out.println("No rides to sort.");
			return;
		}
		rideHistory = mergeSort(rideHistory);
		System.out.println("Ride history sorted by destination.");
	}

	private List<Ride> mergeSort(List<Ride> rides) {
		if (rides.size() <= 1) {
			return rides;
		}
		int mid = rides.size() / 2;
		List<Ride> left = new ArrayList<>(rides.subList(0, mid));
		List<Ride> right = new ArrayList<>(rides.subList(mid, rides.size()));
		return merge(mergeSort(left), mergeSort(right));
	}

	private List<Ride> merge(List<Ride> left, List<Ride> right) {
		List<Ride> merged = new ArrayList<>();
		int i = 0, j = 0;
		while (i < left.size() && j < right.size()) {
			if (left.get(i).destination.compareTo(right.get(j).destination) <= 0) {
				merged.add(left.get(i++));
			} else {
				merged.add(right.get(j++));
			}
		}
		while (i < left.size()) {
			merged.add(left.get(i++));
		}
		while (j < right.size()) {
			merged.add(right.get(j++));
		}
		return merged;
	}

	public static String hashPassword(String password) {
		try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean validatePassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}