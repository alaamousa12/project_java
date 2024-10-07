//import com.toedter.calendar.JDateChooser;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.*;
//import java.util.Date;
//
//public class c_rents extends JFrame {
//
//    // Database connection details
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "aly123"; // Replace with your MySQL password
//
//    // Define UI components
//    private JComboBox<String> bookIDComboBox;
//    private JComboBox<Integer> customerIDComboBox;
//    private JDateChooser bookDateChooser;
//    private JDateChooser returnDateChooser;
//    private JTable bookedCarsTable;
//    private JTable availableCarsTable;
//    private JButton confirmButton;
//    private JLabel bookIDLabel;
//    private JLabel bookDateLabel;
//    private JLabel returnDateLabel;
//    private JLabel customerIDLabel;
//    private JLabel titleLabel;
//    private JPanel sidebarPanel;
//
//    public c_rents() {
//        initializeComponents();
//        setupLayout();
//        // Prevent frame from being resized
//        setResizable(false);
//        // Set the frame size to match the preferred size of the layout
//        setSize(1000, 600);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setTitle("Car Rental System");
//
//        // Set the frame location to the center of the screen
//        setLocationRelativeTo(null);
//
//        // Load available cars and customers
//        loadAvailableCars();
//        loadCustomers();
//        setVisible(true);
//    }
//
//    // Initialize components
//    private void initializeComponents() {
//        // Initialize the combo box for book ID and customer ID
//        bookIDComboBox = new JComboBox<>();
//        customerIDComboBox = new JComboBox<>();
//        bookIDComboBox.setPreferredSize(new Dimension(150, 20));
//        customerIDComboBox.setPreferredSize(new Dimension(150, 20));
//
//        // Initialize date choosers for booking and return dates
//        bookDateChooser = new JDateChooser();
//        returnDateChooser = new JDateChooser();
//        bookDateChooser.setPreferredSize(new Dimension(150, 20));
//        returnDateChooser.setPreferredSize(new Dimension(150, 20));
//
//        // Initialize the tables for available and booked cars
//        DefaultTableModel availableCarsModel = new DefaultTableModel(
//                new Object[][]{},
//                new String[]{"Car ID", "Brand", "Model", "Price"}
//        );
//        availableCarsTable = new JTable(availableCarsModel);
//        availableCarsTable.setPreferredSize(new Dimension(600, 200));
//        DefaultTableModel bookedCarsModel = new DefaultTableModel(
//                new Object[][]{},
//                new String[]{"Car ID", "Book Date", "Return Date", "Price", "Book ID", "Customer ID"}
//        );
//        bookedCarsTable = new JTable(bookedCarsModel);
//        bookedCarsTable.setPreferredSize(new Dimension(600, 200));
//
//        // Initialize the confirm button
//        confirmButton = new JButton("Confirm");
//        confirmButton.addActionListener(this::onConfirmButtonClicked);
//        confirmButton.setPreferredSize(new Dimension(150, 30));
//
//        // Initialize labels
//        bookIDLabel = new JLabel("Car ID:");
//        bookDateLabel = new JLabel("Book Date:");
//        returnDateLabel = new JLabel("Return Date:");
//        customerIDLabel = new JLabel("Customer ID:");
//        titleLabel = new JLabel("Book a Car");
//        titleLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
//        titleLabel.setForeground(new Color(0, 102, 102));
//
//        // Initialize sidebar panel
//        sidebarPanel = createSidebarPanel();
//    }
//
//    // Create the sidebar panel
//    private JPanel createSidebarPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBackground(new Color(0, 102, 102));
//
//        // Initialize sidebar labels and buttons
//        JLabel hiCustomerLabel = new JLabel("HI, Customer");
//        hiCustomerLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
//        hiCustomerLabel.setForeground(Color.WHITE);
//        hiCustomerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel bookLabel = new JLabel("Book a Car");
//        bookLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
//        bookLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        bookLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent evt) {
//                onBookCarClicked(evt);
//            }
//        });
//
//        JLabel returnCarLabel = new JLabel("Return Car");
//        returnCarLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
//        returnCarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        returnCarLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent evt) {
//                onReturnCarClicked(evt);
//            }
//        });
//
//        JLabel feedbackLabel = new JLabel("Feedback");
//        feedbackLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
//        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        feedbackLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent evt) {
//                onFeedbackClicked(evt);
//            }
//        });
//
//        // Add labels and buttons to the sidebar panel
//        panel.add(Box.createVerticalStrut(60));
//        panel.add(hiCustomerLabel);
//        panel.add(Box.createVerticalStrut(40));
//        panel.add(bookLabel);
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(returnCarLabel);
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(feedbackLabel);
//        panel.add(Box.createVerticalGlue());
//
//        return panel;
//    }
//    // Setup layout for the main frame
//    private void setupLayout() {
//        // Use BorderLayout for the main frame
//        setLayout(new BorderLayout());
//
//        // Add the sidebar panel
//        add(sidebarPanel, BorderLayout.WEST);
//
//        // Create the main panel
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Add title label
//        JPanel titlePanel = new JPanel();
//        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        titlePanel.add(titleLabel);
//
//        // Add components for booking cars
//        JPanel bookingPanel = new JPanel();
//        bookingPanel.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        bookingPanel.add(bookIDLabel, gbc);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        bookingPanel.add(bookIDComboBox, gbc);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        bookingPanel.add(bookDateLabel, gbc);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        bookingPanel.add(bookDateChooser, gbc);
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        bookingPanel.add(returnDateLabel, gbc);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        bookingPanel.add(returnDateChooser, gbc);
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        bookingPanel.add(customerIDLabel, gbc);
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        bookingPanel.add(customerIDComboBox, gbc);
//
//        // Add the confirm button
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        buttonPanel.add(confirmButton);
//
//        // Add the available cars and booked cars tables
//        JScrollPane availableCarsScrollPane = new JScrollPane(availableCarsTable);
//        JScrollPane bookedCarsScrollPane = new JScrollPane(bookedCarsTable);
//
//        availableCarsScrollPane.setPreferredSize(new Dimension(900, 200));
//        bookedCarsScrollPane.setPreferredSize(new Dimension(900, 200));
//
//        JLabel availableCarsLabel = new JLabel("Cars in Stock");
//        availableCarsLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
//        availableCarsLabel.setForeground(new Color(0, 102, 102));
//        availableCarsLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//        JLabel bookedCarsLabel = new JLabel("Cars Booked");
//        bookedCarsLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
//        bookedCarsLabel.setForeground(new Color(0, 102, 102));
//        bookedCarsLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // Add all panels to the main panel
//        mainPanel.add(titlePanel);
//        mainPanel.add(Box.createVerticalStrut(10));
//        mainPanel.add(bookingPanel);
//        mainPanel.add(Box.createVerticalStrut(20));
//        mainPanel.add(buttonPanel);
//        mainPanel.add(Box.createVerticalStrut(40));
//        mainPanel.add(availableCarsLabel);
//        mainPanel.add(availableCarsScrollPane);
//        mainPanel.add(Box.createVerticalStrut(20));
//        mainPanel.add(bookedCarsLabel);
//        mainPanel.add(bookedCarsScrollPane);
//        mainPanel.add(Box.createVerticalGlue());
//
//        // Add the main panel to the frame
//        add(mainPanel, BorderLayout.CENTER);
//    }
//
//    // Event handling methods
//
//    private void onConfirmButtonClicked(ActionEvent evt) {
//        // Handle confirm button click
//        String bookID = (String) bookIDComboBox.getSelectedItem();
//        Date bookDate = bookDateChooser.getDate();
//        Date returnDate = returnDateChooser.getDate();
//        Integer customerID = (Integer) customerIDComboBox.getSelectedItem();
//
//        if (bookID == null || bookDate == null || returnDate == null || customerID == null) {
//            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Calculate the number of days between booking and return date
//        long difference = returnDate.getTime() - bookDate.getTime();
//        int numberOfDays = (int) (difference / (1000 * 60 * 60 * 24));
//
//        // Calculate total price based on daily rate and number of days
//        double dailyRate = getDailyRateForCarID(bookID);
//        double totalPrice = dailyRate * numberOfDays;
//
//        // Add the booking information to the booked cars table and save to database
//        DefaultTableModel bookedModel = (DefaultTableModel) bookedCarsTable.getModel();
//        bookedModel.addRow(new Object[]{bookID, new java.sql.Date(bookDate.getTime()), new java.sql.Date(returnDate.getTime()), totalPrice, bookID, customerID});
//
//        addBookingToDatabase(bookID, bookDate, returnDate, totalPrice, customerID);
//
//        // Provide feedback to the user
//        JOptionPane.showMessageDialog(this, "Booking confirmed! Total price: $" + String.format("%.2f", totalPrice), "Confirmation", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    // Add booking information to the database
//    private void addBookingToDatabase(String bookID, Date bookDate, Date returnDate, double totalPrice, int customerID) {
//        String query = "INSERT INTO bookings (car_id, customer_id, book_date, return_date, total_price) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, bookID);
//            statement.setInt(2, customerID);
//            statement.setDate(3, new java.sql.Date(bookDate.getTime()));
//            statement.setDate(4, new java.sql.Date(returnDate.getTime()));
//            statement.setDouble(5, totalPrice);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    // Helper method to establish a connection to the database
//    private Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//    }
//
//    // Retrieve the daily rate for a given car ID
//    private double getDailyRateForCarID(String carID) {
//        String query = "SELECT price FROM cars WHERE car_id = ?";
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, carID);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getDouble("price");
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        // Return a default daily rate if not found
//        return 50.0; // Placeholder daily rate
//    }
//
//    // Load available cars from the database and populate the available cars table and combo box
//    private void loadAvailableCars() {
//        String query = "SELECT car_id, brand, model, price FROM cars WHERE available = 1";
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query);
//             ResultSet resultSet = statement.executeQuery()) {
//
//            DefaultTableModel model = (DefaultTableModel) availableCarsTable.getModel();
//            model.setRowCount(0); // Clear existing data
//            bookIDComboBox.removeAllItems(); // Clear existing items in the combo box
//
//            while (resultSet.next()) {
//                String carID = resultSet.getString("car_id");
//                String brand = resultSet.getString("brand");
//                String modelCar = resultSet.getString("model");
//                double price = resultSet.getDouble("price");
//
//                // Add row to the available cars table
//                model.addRow(new Object[]{carID, brand, modelCar, price});
//
//                // Add car ID to the combo box
//                bookIDComboBox.addItem(carID);
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    // Load customers from the database and populate the customer ID combo box
//    private void loadCustomers() {
//        String query = "SELECT customer_id FROM customers";
//
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query);
//             ResultSet resultSet = statement.executeQuery()) {
//
//            customerIDComboBox.removeAllItems(); // Clear existing items in the combo box
//
//            while (resultSet.next()) {
//                int customerID = resultSet.getInt("customer_id");
//
//                // Add customer ID to the combo box
//                customerIDComboBox.addItem(customerID);
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onBookCarClicked(MouseEvent evt) {
//        // Handle book car option click
//        // You can navigate to the book car section or perform other actions as needed
//        JOptionPane.showMessageDialog(this, "Navigate to the book car section.");
//    }
//
//    private void onReturnCarClicked(MouseEvent evt) {
//        // Handle return car option click
//        // Navigate to the return car section or perform other actions as needed
//        JOptionPane.showMessageDialog(this, "Navigate to the return car section.");
//    }
//
//    private void onFeedbackClicked(MouseEvent evt) {
//        // Handle feedback option click
//        // Navigate to the feedback section or perform other actions as needed
//        FeedbackForm feedbackForm = new FeedbackForm();
//        feedbackForm.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        // Start the application
//        SwingUtilities.invokeLater(() -> new c_rents().setVisible(true));
//}
//}