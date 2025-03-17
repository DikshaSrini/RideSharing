package RideSharing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RideSharingAppUI {
	private RideSharingSystem system;
	private JFrame frame;
	private JTextArea outputArea;
	private User loggedInUser;

	public RideSharingAppUI() {
		system = new RideSharingSystem();
		frame = new JFrame("Ride Sharing Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLayout(new BorderLayout());
		showLoginScreen();
		frame.setVisible(true);
	}

	private void showLoginScreen() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(new Color(230, 230, 250));

		JLabel titleLabel = new JLabel("Ride Sharing Application");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(72, 61, 139));

		JButton registerBtn = new JButton("Register");
		JButton loginBtn = new JButton("Login");
		JButton exitBtn = new JButton("Exit");

		styleButton(registerBtn, new Color(255, 223, 0)); 
		styleButton(loginBtn, new Color(255, 179, 71)); 
		styleButton(exitBtn, new Color(169, 169, 169)); 

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(titleLabel, gbc);
		gbc.gridy = 1;
		panel.add(registerBtn, gbc);
		gbc.gridy = 2;
		panel.add(loginBtn, gbc);
		gbc.gridy = 3;
		panel.add(exitBtn, gbc);

		frame.getContentPane().removeAll();
		frame.add(panel, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();

		registerBtn.addActionListener(e -> registerUser());
		loginBtn.addActionListener(e -> loginUser());
		exitBtn.addActionListener(e -> System.exit(0));
	}

	private void loginUser() {
		String id = JOptionPane.showInputDialog("Enter User ID:");
		if (id == null || id.trim().isEmpty())
			return;
		JPasswordField passwordField = new JPasswordField();
		int option = JOptionPane.showConfirmDialog(null, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (option != JOptionPane.OK_OPTION)
			return;
		String enteredPassword = new String(passwordField.getPassword());
		User user = system.getUser(id);
		if (user != null) {
			String enteredPasswordHash = User.hashPassword(enteredPassword);
			if (user.getPasswordHash().equals(enteredPasswordHash)) {
				loggedInUser = user;
				JOptionPane.showMessageDialog(frame, "Login successful!");
				showMainMenu();
			} else {
				JOptionPane.showMessageDialog(frame, "Invalid credentials! Please try again.");
			}
		}
	}

	private void registerUser() {
		String id = JOptionPane.showInputDialog("Enter User ID:");
		String name = JOptionPane.showInputDialog("Enter User Name:");
		JPasswordField passwordField = new JPasswordField();
		int option = JOptionPane.showConfirmDialog(null, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (option != JOptionPane.OK_OPTION)
			return;
		String password = new String(passwordField.getPassword());
		String[] options = { "Rider", "Driver" };
		int choice = JOptionPane.showOptionDialog(null, "Select User Type:", "Register User",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (id == null || name == null || choice == -1)
			return;
		if (choice == 0)
			system.registerUser(new Rider(id, name, password), "root");
		else if (choice == 1)
			system.registerUser(new Driver(id, name, password), "root");
		JOptionPane.showMessageDialog(frame, "Registration successful! Please log in.");
	}

	private void showMainMenu() {
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8, 1)); 
		panel.setBackground(new Color(240, 248, 255));

		JButton showCitiesBtn = new JButton("Show Available Cities");
		JButton requestRideBtn = new JButton("Request Ride");
		JButton processRideBtn = new JButton("Process Next Ride");
		JButton viewRideHistoryBtn = new JButton("View Ride History");
		JButton rateRideBtn = new JButton("Rate a Ride");
		JButton searchDriverBtn = new JButton("Search Drivers by Destination");
		JButton planTripBtn = new JButton("Plan Trip");
		JButton logoutBtn = new JButton("Logout");

		styleButton(requestRideBtn, new Color(255, 179, 71)); 
		styleButton(processRideBtn, new Color(255, 140, 0)); 
		styleButton(viewRideHistoryBtn, new Color(211, 211, 211)); 
		styleButton(rateRideBtn, new Color(169, 169, 169)); 
		styleButton(searchDriverBtn, new Color(128, 128, 128)); 
		styleButton(planTripBtn, new Color(255, 255, 224)); 
		styleButton(showCitiesBtn, new Color(255, 223, 186)); 
		styleButton(logoutBtn, new Color(255, 255, 0)); 

		requestRideBtn.setForeground(Color.BLACK);
		processRideBtn.setForeground(Color.BLACK);
		viewRideHistoryBtn.setForeground(Color.BLACK);
		rateRideBtn.setForeground(Color.BLACK);
		searchDriverBtn.setForeground(Color.BLACK);
		planTripBtn.setForeground(Color.BLACK);
		showCitiesBtn.setForeground(Color.BLACK);
		logoutBtn.setForeground(Color.BLACK);

		if (loggedInUser instanceof Rider) {
			panel.add(showCitiesBtn);
			panel.add(requestRideBtn);
			panel.add(viewRideHistoryBtn);
			panel.add(rateRideBtn);
			panel.add(planTripBtn);
		} else if (loggedInUser instanceof Driver) {
			panel.add(showCitiesBtn);
			panel.add(processRideBtn);
			panel.add(viewRideHistoryBtn);
			panel.add(rateRideBtn);
			panel.add(planTripBtn);
		}
		panel.add(logoutBtn);

		requestRideBtn.addActionListener(e -> requestRide());
		processRideBtn.addActionListener(e -> processRide());
		viewRideHistoryBtn.addActionListener(e -> viewRideHistory());
		rateRideBtn.addActionListener(e -> rateRide());
		searchDriverBtn.addActionListener(e -> searchDrivers());
		planTripBtn.addActionListener(e -> planTrip());
		showCitiesBtn.addActionListener(e -> showAvailableCities());
		logoutBtn.addActionListener(e -> logoutUser());

		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setBackground(new Color(255, 255, 255));
		JScrollPane scrollPane = new JScrollPane(outputArea);

		frame.add(panel, BorderLayout.CENTER);
		frame.add(scrollPane, BorderLayout.SOUTH);

		frame.revalidate();
		frame.repaint();
	}

	private void styleButton(JButton button, Color color) {
		button.setBackground(color);
		button.setForeground(Color.BLACK); 
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(150, 30));
	}

	private void requestRide() {
		if (!(loggedInUser instanceof Rider)) {
			JOptionPane.showMessageDialog(frame, "Only riders can request rides!");
			return;
		}

		List<String> drivers = system.getAllDrivers();
		if (drivers.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "No drivers available!");
			return;
		}

		String driverId = (String) JOptionPane.showInputDialog(frame, "Select a driver:", "Available Drivers",
				JOptionPane.PLAIN_MESSAGE, null, drivers.toArray(), drivers.get(0));

		if (driverId == null || driverId.trim().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Invalid driver selection!");
			return;
		}

		driverId = driverId.split(" - ")[0];

		String destination = JOptionPane.showInputDialog("Enter Destination:");
		if (destination == null || destination.trim().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Invalid destination!");
			return;
		}

		system.requestRide(loggedInUser.id, driverId, destination);
	}

	private void processRide() {
		if (!(loggedInUser instanceof Driver)) {
			JOptionPane.showMessageDialog(frame, "Only drivers can process rides!");
			return;
		}
		system.processNextRide();
	}

	private void viewRideHistory() {
		outputArea.setText("");
		loggedInUser.viewRideHistory();
	}

	private void rateRide() {
		String rideId = JOptionPane.showInputDialog("Enter Ride ID:");
		int rating = Integer.parseInt(JOptionPane.showInputDialog("Enter Rating (1-5):"));
		system.rateRide(rideId, rating);
	}

	private void searchDrivers() {
		String destination = JOptionPane.showInputDialog("Enter Destination:");
		system.searchDriversByLocation(destination);
	}

	private void planTrip() {
		String start = JOptionPane.showInputDialog("Enter Start City:");
		String end = JOptionPane.showInputDialog("Enter End City:");
		String result = system.findShortestPathWithDistance(start, end);
		System.out.println(result);
	}

	private void showAvailableCities() {
	    List<String> cities = system.getAvailableCities();
	    StringBuilder citiesList = new StringBuilder("Available Cities:\n");
	    for (String city : cities) {
	        citiesList.append("- ").append(city).append("\n");
	    }
	    System.out.println(citiesList.toString()); 
	}

	private void logoutUser() {
		loggedInUser = null;
		JOptionPane.showMessageDialog(frame, "Logged out successfully!");
		showLoginScreen();
	}

	public static void main(String[] args) {
		new RideSharingAppUI();
	}
}