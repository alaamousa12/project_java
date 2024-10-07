import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogIn extends JFrame {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "aly123";

    // Declare components as instance variables
    private JPanel mainPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JLabel logoLabel;
    private JLabel systemNameLabel;
    private JLabel loginLabel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JTextField userNameField;
    private JButton signUpButton;
    private JButton loginButton;
    private JLabel noAccountLabel;

    public LogIn() {
        // Initialize the main frame
        setTitle("LOGIN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with absolute layout
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // Create right and left panels
        rightPanel = createRightPanel();
        leftPanel = createLeftPanel();

        // Add panels to the main panel with specific bounds
        mainPanel.add(rightPanel);
        mainPanel.add(leftPanel);
        rightPanel.setBounds(0, 0, 400, 500);
        leftPanel.setBounds(400, 0, 400, 500);

        // Set the main panel as the content pane of the frame
        setContentPane(mainPanel);
    }

    private JPanel createRightPanel() {
        // Create the right panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 500));
        panel.setBackground(new Color(0, 102, 102));
        panel.setLayout(null);

        // Initialize components
        logoLabel = new JLabel(new ImageIcon("D:\\project_java\\src\\imagescars.jpg"));
        logoLabel.setBounds(85, 72, 227, 183);
        systemNameLabel = new JLabel("Car Rental System");
        systemNameLabel.setFont(new Font("Edwardian Script ITC", Font.BOLD, 48));
        systemNameLabel.setForeground(Color.WHITE);
        systemNameLabel.setBounds(39, 281, 322, 82);

        // Add components to the panel
        panel.add(logoLabel);
        panel.add(systemNameLabel);

        return panel;
    }
    private JPanel createLeftPanel() {
        // Create the left panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 500));
        panel.setLayout(null);

        // Initialize components
        loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        loginLabel.setForeground(new Color(0, 102, 102));
        loginLabel.setBounds(140, 50, 120, 38);

        userNameLabel = new JLabel("User Name");
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userNameLabel.setBounds(20, 117, 90, 20);

        userNameField = new JTextField();
        userNameField.setBounds(20, 137, 348, 49);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setBounds(20, 204, 90, 20);

        passwordField = new JPasswordField();
        passwordField.setBounds(20, 224, 348, 46);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBounds(20, 299, 108, 42);
        loginButton.addActionListener(this::handleLoginButton);

        noAccountLabel = new JLabel("I don't have an account");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        noAccountLabel.setBounds(20, 359, 170, 20);

        signUpButton = new JButton("Sign Up");
        signUpButton.setForeground(new Color(255, 51, 51));
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        signUpButton.setBounds(210, 354, 158, 30);
        signUpButton.addActionListener(this::handleSignUpButton);

        // Add components to the panel
        panel.add(loginLabel);
        panel.add(userNameLabel);
        panel.add(userNameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(noAccountLabel);
        panel.add(signUpButton);

        return panel;
    }
    private void handleLoginButton(ActionEvent evt) {
        // Handle login button action
        String userNameInput = userNameField.getText();
        String passwordInput = new String(passwordField.getPassword());

        // Perform login logic
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userNameInput);
                statement.setString(2, passwordInput);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Welcome, " + userNameInput + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignUpButton(ActionEvent evt) {
        // Handle sign up button action
        this.dispose();
        new SignUp_customer().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogIn loginFrame = new LogIn();
            loginFrame.setVisible(true);
        });
    }
}
