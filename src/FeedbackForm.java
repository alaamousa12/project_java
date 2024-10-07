import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FeedbackForm extends JFrame {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "aly123";

    // UI Components
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel feedbackLabel;
    private JTextArea feedbackArea;
    private JScrollPane feedbackScrollPane;
    private JLabel contactLabel;
    private JButton submitButton;
    private JButton cancelButton;

    private Connection connection;

    public FeedbackForm() {
        // Window settings
        setTitle("Customer Feedback");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize components
        initializeComponents();

        // Add components to the window
        addComponents();
        // Show the window
        setVisible(true);
    }

    private void initializeComponents() {
        // Initialize UI components
        titleLabel = new JLabel("Customer Feedback");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        feedbackLabel = new JLabel("Feedback:");
        feedbackArea = new JTextArea(5, 20);
        feedbackScrollPane = new JScrollPane(feedbackArea);

        contactLabel = new JLabel("Contact us: email@email.com");

        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 102, 102));
        submitButton.setForeground(Color.WHITE);

        cancelButton = new JButton("Cancel");
        // Add event handlers for buttons
        submitButton.addActionListener(this::handleSubmitAction);
        cancelButton.addActionListener(this::handleCancelAction);
    }

    private void addComponents() {
        // Set layout for the window
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add components using GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);

        gbc.gridx++;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(emailLabel, gbc);

        gbc.gridx++;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(feedbackLabel, gbc);

        gbc.gridx++;
        add(feedbackScrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(contactLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        gbc.gridx++;
        add(cancelButton, gbc);
    }

    private void handleSubmitAction(ActionEvent evt) {
        // Collect input data
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String feedback = feedbackArea.getText().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and handle feedback submission
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            submitFeedback(email, name, feedback, connection);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void submitFeedback(String email, String name, String feedback, Connection connection) throws SQLException {
        // Insert feedback into the database
        String query = "INSERT INTO feedback (email, name, feedback) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, feedback);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error submitting feedback. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCancelAction(ActionEvent evt) {
        // Clear all input fields and reset the form
        nameField.setText("");
        emailField.setText("");
        feedbackArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FeedbackForm::new);
    }
}