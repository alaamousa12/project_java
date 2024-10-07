import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class m_cars extends JFrame {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "aly123";

    // Declare UI components
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JLabel lblGreeting;
    private JLabel lblTitle;
    private JLabel lblCarID;
    private JLabel lblBrand;
    private JLabel lblModel;
    private JLabel lblPrice;
    private JLabel lblAvailability;
    private JTextField txtCarID;
    private JTextField txtBrand;
    private JTextField txtModel;
    private JTextField txtPrice;
    private JComboBox<String> comboAvailability;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnReset;
    private JLabel lblCarsInStock;
    private JTable tableCars;
    private JScrollPane scrollPane;

    // Constructor to initialize the components and set up the frame
    public m_cars() {
        // Set frame properties
        setTitle("Car Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 700);
        setResizable(true);
        setLocationRelativeTo(null);

        // Create the main panel with a border layout
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create and configure the sidebar panel
        sidebarPanel = createSidebarPanel();

        // Add sidebar panel to main panel
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content panel setup
        contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Set up top panel and title
        JPanel topPanel = new JPanel();
        lblTitle = new JLabel("Car Register", JLabel.CENTER);
        lblTitle.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 102));
        topPanel.add(lblTitle);
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Form panel setup
        JPanel formPanel = new JPanel(new GridLayout(3, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Create and add form labels and text fields
        lblCarID = new JLabel("Car ID");
        txtCarID = new JTextField(30);
        lblBrand = new JLabel("Brand");
        txtBrand = new JTextField(30);
        lblModel = new JLabel("Model");
        txtModel = new JTextField(30);
        lblPrice = new JLabel("Price");
        txtPrice = new JTextField(30);
        lblAvailability = new JLabel("Availability");
        comboAvailability = new JComboBox<>(new String[]{"Available", "Booked"});

        // Add labels and fields to the form panel
        formPanel.add(lblCarID);
        formPanel.add(txtCarID);
        formPanel.add(lblBrand);
        formPanel.add(txtBrand);
        formPanel.add(lblModel);
        formPanel.add(txtModel);
        formPanel.add(lblPrice);
        formPanel.add(txtPrice);
        formPanel.add(lblAvailability);
        formPanel.add(comboAvailability);

        // Add form panel to content panel
        contentPanel.add(formPanel, BorderLayout.NORTH);

        // Set up the "Cars in Stock" section
        JPanel stockPanel = new JPanel(new BorderLayout());
        lblCarsInStock = new JLabel("Cars in Stock", JLabel.CENTER);
        lblCarsInStock.setFont(new Font("Century Gothic", Font.BOLD, 18));
        lblCarsInStock.setForeground(new Color(0, 102, 102));
        stockPanel.add(lblCarsInStock, BorderLayout.NORTH);

        // Create a table to display car data
        String[] columnNames = {"Car ID", "Brand", "Model", "Price", "Availability"};
        DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);
        tableCars = new JTable(tableModel); // Initialize tableCars

        // Add the table to a scroll pane
        scrollPane = new JScrollPane(tableCars);
        stockPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the stock panel to content panel
        contentPanel.add(stockPanel, BorderLayout.CENTER);

        // Set up button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnReset = new JButton("Reset");
        // Add buttons to the panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReset);

        // Add the button panel at the bottom of the frame
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        addActionListeners();

        // Load car data into the table when the application starts
        loadCarsData();

        // Add a selection listener to the table
        tableCars.getSelectionModel().addListSelectionListener(e -> {
            onTableRowSelectionChanged();
        });
    }

    // Method to create and return the sidebar panel
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0, 102, 102));
        panel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        // Add greeting label
        lblGreeting = new JLabel("HI, Manager", JLabel.CENTER);
        lblGreeting.setFont(new Font("Century Gothic", Font.BOLD, 24));
        lblGreeting.setForeground(Color.WHITE);
        panel.add(lblGreeting);
        lblGreeting.setPreferredSize(new Dimension(lblGreeting.getPreferredSize().width, 50));

        // Add sidebar labels for sections
        JLabel lblCars = createSidebarLabel("Cars");
        JLabel lblCustomers = createSidebarLabel("Customers");
        JLabel lblEmployees = createSidebarLabel("Employees");

        panel.add(Box.createVerticalStrut(20));
        panel.add(lblCars);
        panel.add(lblCustomers);
        panel.add(lblEmployees);

        return panel;
    }

    // Method to create and return sidebar labels
    private JLabel createSidebarLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Century Gothic", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onSidebarLabelClick(evt, text);
            }
        });
        return label;
    }

    // Handle sidebar label click events
    private void onSidebarLabelClick(java.awt.event.MouseEvent evt, String text) {
        // Handle sidebar label click events
        JOptionPane.showMessageDialog(this, text + " section clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Add action listeners to the buttons
    private void addActionListeners() {
        btnAdd.addActionListener(this::onAddButtonClick);
        btnEdit.addActionListener(this::onEditButtonClick);
        btnDelete.addActionListener(this::onDeleteButtonClick);
        btnReset.addActionListener(this::onResetButtonClick);
    }
    // Handle the "Add" button click
    private void onAddButtonClick(ActionEvent evt) {
        try {
            // Gather data from form fields
            String carID = txtCarID.getText();
            String brand = txtBrand.getText();
            String model = txtModel.getText();
            double price = Double.parseDouble(txtPrice.getText());
            boolean available = comboAvailability.getSelectedItem().toString().equals("Available");

            // Validate data before adding
            if (carID.isEmpty() || brand.isEmpty() || model.isEmpty() || price == 0) {
                JOptionPane.showMessageDialog(this, "All fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add the new car to the database
            addCar(carID, brand, model, price, available);
            JOptionPane.showMessageDialog(this, "Car added successfully!");

            // Refresh data and reset form
            loadCarsData();
            resetFormFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTableRowSelectionChanged() {
        int selectedRow = tableCars.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tableCars.getModel();

            // Populate the form fields with data from the selected row
            txtCarID.setText((String) model.getValueAt(selectedRow, 0));
            txtBrand.setText((String) model.getValueAt(selectedRow, 1));
            txtModel.setText((String) model.getValueAt(selectedRow, 2));
            txtPrice.setText(model.getValueAt(selectedRow, 3).toString());
            comboAvailability.setSelectedItem(model.getValueAt(selectedRow, 4));
        }
    }

    private void onEditButtonClick(ActionEvent evt) {
        try {
            // Get the selected row in the table
            int selectedRow = tableCars.getSelectedRow();
            if (selectedRow != -1) {
                // Gather data from form fields
                String carID = txtCarID.getText();
                String brand = txtBrand.getText();
                String model = txtModel.getText();
                double price = Double.parseDouble(txtPrice.getText());
                boolean available = comboAvailability.getSelectedItem().toString().equals("Available");

                // Validate data before editing
                if (carID.isEmpty() || brand.isEmpty() || model.isEmpty() || price == 0) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the car in the database
                editCar(carID, brand, model, price, available);

                // Refresh data in the tableCars JTable
                DefaultTableModel tableModel = (DefaultTableModel) tableCars.getModel();
                tableModel.setValueAt(carID, selectedRow, 0);
                tableModel.setValueAt(brand, selectedRow, 1);
                tableModel.setValueAt(model, selectedRow, 2);
                tableModel.setValueAt(price, selectedRow, 3);
                tableModel.setValueAt(available ? "Available" : "Booked", selectedRow, 4);

                // Reset form fields after editing
                resetFormFields();

                // Notify the user
                JOptionPane.showMessageDialog(this, "Car edited successfully!");
            } else {
                // No row selected
                JOptionPane.showMessageDialog(this, "Please select a car to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle the "Delete" button click
    private void onDeleteButtonClick(ActionEvent evt) {
        try {
            // Get the selected row in the table
            int selectedRow = tableCars.getSelectedRow();
            if (selectedRow != -1) {
                // Confirm deletion
                int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected car?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Retrieve car ID and remove the car from the database
                    DefaultTableModel tableModel = (DefaultTableModel) tableCars.getModel();
                    String carID = (String) tableModel.getValueAt(selectedRow, 0);
                    deleteCar(carID);
                    // Remove the selected row from the table model
                    tableModel.removeRow(selectedRow);
                    // Refresh data and reset form
                    loadCarsData();
                    resetFormFields();
                }
            } else {
                // No row selected
                JOptionPane.showMessageDialog(this, "Please select a car to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle the "Reset" button click
    private void onResetButtonClick(ActionEvent evt) {
        // Reset form fields
        resetFormFields();
    }

    // Reset form fields
    private void resetFormFields() {
        txtCarID.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtPrice.setText("");
        comboAvailability.setSelectedIndex(0);
    }

    // Add a new car to the database
    private void addCar(String carID, String brand, String model, double price, boolean available) throws SQLException {
        String query = "INSERT INTO cars (car_id, brand, model, price, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, carID);
            statement.setString(2, brand);
            statement.setString(3, model);
            statement.setDouble(4, price);
            statement.setInt(5, available ? 1 : 0);
            statement.executeUpdate();
        }
    }

    // Edit a car in the database
    private void editCar(String carID, String brand, String model, double price, boolean available) throws SQLException {
        String query = "UPDATE cars SET brand = ?, model = ?, price = ?, available = ? WHERE car_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, brand);
            statement.setString(2, model);
            statement.setDouble(3, price);
            statement.setInt(4, available ? 1 : 0);
            statement.setString(5, carID);
            statement.executeUpdate();
        }
    }

    // Delete a car from the database
    private void deleteCar(String carID) throws SQLException {
        String query = "DELETE FROM cars WHERE car_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, carID);
            statement.executeUpdate();
        }
    }

    // Load car data from the database into the tableCars JTable
    private void loadCarsData() {
        String query = "SELECT car_id, brand, model, price, available FROM cars";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Get the DefaultTableModel of the tableCars
            DefaultTableModel model = (DefaultTableModel) tableCars.getModel();
            model.setRowCount(0); // Clear existing data

            // Iterate through the ResultSet and add rows to the model
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("car_id");
                row[1] = resultSet.getString("brand");
                row[2] = resultSet.getString("model");
                row[3] = resultSet.getDouble("price");
                row[4] = resultSet.getInt("available") == 1 ? "Available" : "Booked";
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Get a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static void main(String[] args) {
        // Create and display the form
        SwingUtilities.invokeLater(() -> new m_cars().setVisible(true));
    }
}