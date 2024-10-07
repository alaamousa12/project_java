import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignUp_customer extends JFrame {

    // تفاصيل الاتصال بقاعدة البيانات
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "aly123";

    // إعلان المكونات
    private JPanel mainPanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JLabel logoLabel;
    private JLabel systemNameLabel;
    private JLabel signUpLabel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    private JLabel phoneNumberLabel;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JButton registerButton;
    private JButton backButton;
    public SignUp_customer() {
        // إعداد خصائص الإطار
        setTitle("SIGN UP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // إنشاء اللوحة الرئيسية مع التخطيط المطلق
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        // تهيئة اللوحات اليمنى واليسرى
        rightPanel = createRightPanel();
        leftPanel = createLeftPanel();

        // إضافة اللوحات إلى اللوحة الرئيسية
        mainPanel.add(rightPanel);
        mainPanel.add(leftPanel);

        // تحديد الحدود للوحات
        rightPanel.setBounds(0, 0, 400, 500);
        leftPanel.setBounds(400, 0, 400, 500);
    }

    private JPanel createRightPanel() {
        // إنشاء اللوحة اليمنى مع التخطيط المطلق
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 102));
        panel.setPreferredSize(new Dimension(400, 500));

        // تهيئة المكونات
        logoLabel = new JLabel(new ImageIcon("D:\\project_java\\src\\imagescars.jpg"));
        logoLabel.setBounds(85, 72, 227, 183);
        systemNameLabel = new JLabel("Car Rental System");
        systemNameLabel.setFont(new Font("Edwardian Script ITC", Font.BOLD, 48));
        systemNameLabel.setForeground(Color.WHITE);
        systemNameLabel.setBounds(39, 281, 322, 82);

        // إضافة المكونات إلى اللوحة
        panel.add(logoLabel);
        panel.add(systemNameLabel);

        return panel;
    }

    private JPanel createLeftPanel() {
        // إنشاء اللوحة اليسرى مع التخطيط المطلق
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(400, 500));

        // تهيئة المكونات
        signUpLabel = new JLabel("SIGN UP");
        signUpLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        signUpLabel.setForeground(new Color(0, 102, 102));
        signUpLabel.setBounds(130, 20, 200, 38);

        userNameLabel = new JLabel("User Name");
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userNameLabel.setBounds(20, 70, 90, 20);

        userNameField = new JTextField();
        userNameField.setBounds(20, 90, 348, 35);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setBounds(20, 145, 90, 20);

        passwordField = new JPasswordField();
        passwordField.setBounds(20, 165, 348, 35);
        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setBounds(20, 220, 90, 20);

        emailField = new JTextField();
        emailField.setBounds(20, 240, 348, 35);

        phoneNumberLabel = new JLabel("Phone Number");
        phoneNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        phoneNumberLabel.setBounds(20, 295, 120, 20);

        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(20, 315, 348, 35);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 102, 102));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setBounds(200, 365, 130, 40);
        registerButton.addActionListener(this::handleRegisterButton);

        backButton = new JButton("Back to login");
        backButton.setBackground(new Color(0, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBounds(20, 365, 140, 40);
        backButton.addActionListener(this::handleBackButton);

        // إضافة المكونات إلى اللوحة
        panel.add(signUpLabel);
        panel.add(userNameLabel);
        panel.add(userNameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);
        panel.add(registerButton);
        panel.add(backButton);

        return panel;
    }

    private void handleRegisterButton(ActionEvent evt) {
        // الحصول على المدخلات من حقول النصوص
        String userName = userNameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();

        // التحقق من عدم وجود مدخلات فارغة
        if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // إضافة المستخدم إلى قاعدة البيانات
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // التحقق مما إذا كان اسم المستخدم أو البريد الإلكتروني موجودًا بالفعل
            String checkQuery = "SELECT COUNT(*) FROM users WHERE userName = ? OR email = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, userName);
                checkStatement.setString(2, email);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Username or email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            // إضافة المستخدم الجديد إلى قاعدة البيانات
            String insertQuery = "INSERT INTO users (userName, password, email, phoneNumber) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, userName);
                insertStatement.setString(2, password);
                insertStatement.setString(3, email);
                insertStatement.setString(4, phoneNumber);
                insertStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // بعد التسجيل الناجح، عد إلى شاشة تسجيل الدخول
                this.dispose();
                new LogIn().setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBackButton(ActionEvent evt) {
        // العودة إلى شاشة تسجيل الدخول
        this.dispose();
        new LogIn().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignUp_customer signUpFrame = new SignUp_customer();
            signUpFrame.setVisible(true);
        });
    }
}