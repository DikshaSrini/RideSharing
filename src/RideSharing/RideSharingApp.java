package RideSharing;

import java.util.List;
import java.util.Scanner;

public class RideSharingApp {
	private RideSharingSystem system;
	private Scanner scanner;
	private User loggedInUser;

	public RideSharingApp() {
		system = new RideSharingSystem();
		scanner = new Scanner(System.in);
		loggedInUser = null;
	}

	public void start() {
		while (true) {
			if (loggedInUser == null) {
				showLoginScreen();
			} else {
				showMainMenu();
			}
		}
	}

	private void showLoginScreen() {
		System.out.println("\n=== Ride Sharing Application ===");
		System.out.println("1. Register");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
		case 1:
			registerUser();
			break;
		case 2:
			loginUser();
			break;
		case 3:
			System.out.println("Exiting...");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}
	}

	private void registerUser() {
		System.out.print("Enter User ID: ");
		String id = scanner.nextLine().trim();
		System.out.print("Enter Name: ");
		String name = scanner.nextLine().trim();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine().trim();
		System.out.print("Are you a Rider or Driver? (R/D): ");
		char userType = scanner.next().charAt(0);
		scanner.nextLine();

		String hashedPassword = User.hashPassword(password);
		System.out.println("Password Entered: " + password);
		System.out.println("Hashed Password: " + hashedPassword);

		if (userType == 'R' || userType == 'r') {
			system.registerUser(new Rider(id, name, password), "root");
		} else if (userType == 'D' || userType == 'd') {
			system.registerUser(new Driver(id, name, password), "root");
		} else {
			System.out.println("Invalid user type!");
		}
		System.out.println("Registration successful! Please log in.");
	}

	private void loginUser() {
		System.out.print("Enter User ID: ");
		String id = scanner.nextLine().trim();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine().trim();

		String hashedPassword = User.hashPassword(password);
		System.out.println("Password Entered: " + password);
		System.out.println("Hashed Password (Entered): " + hashedPassword);

		User user = system.getUser(id);
		if (user != null) {
			System.out.println("Stored Hashed Password: " + user.getPasswordHash());
			if (user.getPasswordHash().equals(hashedPassword)) {
				loggedInUser = user;
				System.out.println("Login successful! Welcome, " + loggedInUser.name);
			} else {
				System.out.println("Invalid credentials!");
			}
		} else {
			System.out.println("User not found!");
		}
	}

	private void showMainMenu() {
		System.out.println("\n=== Main Menu ===");
		System.out.println("1. Show Available Cities");
		System.out.println("2. Request Ride");
		System.out.println("3. Process Next Ride");
		System.out.println("4. View Ride History");
		System.out.println("5. Rate a Ride");
		System.out.println("6. Search Drivers by Destination");
		System.out.println("7. Plan Trip (Shortest Path)");
		System.out.println("8. Logout");
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
		case 1:
			showAvailableCities();
			break;
		case 2:
			requestRide();
			break;
		case 3:
			processRide();
			break;
		case 4:
			viewRideHistory();
			break;
		case 5:
			rateRide();
			break;
		case 6:
			searchDriversByLocation();
			break;
		case 7:
			planTrip();
			break;
		case 8:
			logoutUser();
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}
	}

	private void showAvailableCities() {
		List<String> cities = system.getAvailableCities();
		System.out.println("\nAvailable Cities:");
		for (String city : cities) {
			System.out.println("- " + city);
		}
	}

	private void requestRide() {
		if (!(loggedInUser instanceof Rider)) {
			System.out.println("Only riders can request rides!");
			return;
		}

		System.out.print("Enter Driver ID: ");
		String driverId = scanner.nextLine();
		System.out.print("Enter Destination: ");
		String destination = scanner.nextLine();

		system.requestRide(loggedInUser.id, driverId, destination);
	}

	private void processRide() {
		if (!(loggedInUser instanceof Driver)) {
			System.out.println("Only drivers can process rides!");
			return;
		}

		system.processNextRide();
	}

	private void viewRideHistory() {
		loggedInUser.viewRideHistory();
	}

	private void rateRide() {
		System.out.print("Enter Ride ID: ");
		String rideId = scanner.nextLine();
		System.out.print("Enter Rating (1-5): ");
		int rating = scanner.nextInt();
		scanner.nextLine();

		system.rateRide(rideId, rating);
	}

	private void searchDriversByLocation() {
		System.out.print("Enter Destination: ");
		String destination = scanner.nextLine();

		List<String> drivers = system.searchDriversByLocation(destination);
		if (drivers.isEmpty()) {
			System.out.println("No drivers found for the given destination.");
		} else {
				System.out.println("Drivers who have traveled to " + destination + ": " + drivers);
		}
	}

	private void planTrip() {
		System.out.print("Enter Start City: ");
		String start = scanner.nextLine();
		System.out.print("Enter End City: ");
		String end = scanner.nextLine();

		String result = system.findShortestPathWithDistance(start, end);
		System.out.println(result);
	}

	private void logoutUser() {
		loggedInUser = null;
		System.out.println("Logged out successfully!");
		showLoginScreen();
	}

	public static void main(String[] args) {
		RideSharingApp app = new RideSharingApp();
		app.start();
	}
}