import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PaymentForm extends JFrame {

    private PaymentsDatabase paymentsDatabase;

    // Form components
    private JTextField cardHolderField;
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField phoneNumberField;
    private JTextField postalCodeField;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JButton submitButton;

    public PaymentForm() {
        setTitle("Payment");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        paymentsDatabase = new PaymentsDatabase();

        // Initialize form components
        cardHolderField = createTextField();
        cardNumberField = createTextField();
        cvvField = createTextField();
        phoneNumberField = createTextField();
        postalCodeField = createTextField();

        monthComboBox = new JComboBox<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});
        yearComboBox = new JComboBox<>(new String[]{"2024", "2025", "2026", "2027", "2028", "2029", "2030"});
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 102, 102));
        submitButton.setForeground(Color.WHITE);

        // Add components to the form
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Add labels and fields
        addLabelAndField("Card Holder Name:", cardHolderField, 0, gbc);
        addLabelAndField("Card Number:", cardNumberField, 1, gbc);
        addLabelAndField("CVV:", cvvField, 2, gbc);
        addLabelAndField("Phone Number:", phoneNumberField, 3, gbc);
        addLabelAndField("Postal Code:", postalCodeField, 4, gbc);

        // Add expiration date selection
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(new JLabel("Expiration Date:"), gbc);

        JPanel expiryPanel = new JPanel();
        expiryPanel.add(monthComboBox);
        expiryPanel.add(yearComboBox);
        gbc.gridx = 1;
        add(expiryPanel, gbc);

        // Add submit button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        // Add action listener to submit button
        submitButton.addActionListener(this::handleSubmit);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setHorizontalAlignment(JTextField.LEFT);
        return textField;
    }

    private void addLabelAndField(String labelText, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void handleSubmit(ActionEvent e) {
        String cardHolder = cardHolderField.getText();
        String cardNumber = cardNumberField.getText();
        String cvv = cvvField.getText();
        String phoneNumber = phoneNumberField.getText();
        String postalCode = postalCodeField.getText();
        String expiryMonth = (String) monthComboBox.getSelectedItem();
        String expiryYear = (String) yearComboBox.getSelectedItem();

        // Validate inputs
        if (!isValidCardNumber(cardNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid card number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidCVV(cvv)) {
            JOptionPane.showMessageDialog(this, "Invalid CVV!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add payment data to database
        paymentsDatabase.addPayment(cardHolder, cardNumber, cvv, phoneNumber, postalCode, expiryMonth, expiryYear);
        // Show success message and close the form
        JOptionPane.showMessageDialog(this, "Payment information verified! Thank you.", "Success", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{14}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaymentForm paymentForm = new PaymentForm();
            paymentForm.setVisible(true);
        });
    }
}

class PaymentsDatabase {

    // إعدادات الاتصال بقاعدة البيانات
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "aly123";

    // إضافة بيانات إلى جدول "payments"
    public void addPayment(String name, String cardNumber, String cvv, String phone, String postalCode, String expiryMonth, String expiryYear) {
        String sql = "INSERT INTO payments (name, card_number, cvv, phone, postal_code, expiry_month, expiry_year) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, cardNumber);
            statement.setString(3, cvv);
            statement.setString(4, phone);
            statement.setString(5, postalCode);
            statement.setString(6, expiryMonth);
            statement.setString(7, expiryYear);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment added successfully.");
            } else {
                System.out.println("Failed to add payment.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Example on how to use the program
    public static void main(String[] args) {
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setVisible(true);
    }
}