package ui;

import database.Database;
import models.Customer;
import utils.ColorScheme;
import utils.GradientPanel;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private Database database;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField licenseField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public RegisterFrame() {
        database = Database.getInstance();

        setTitle("Luxury Car Rental System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        GradientPanel mainPanel = new GradientPanel(ColorScheme.PRIMARY_DARK,
                ColorScheme.PRIMARY_LIGHT, GradientPanel.VERTICAL);
        mainPanel.setLayout(new BorderLayout());

        // Top Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        JLabel logoLabel = new JLabel("🏎️ Create Your Account");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logoLabel.setForeground(ColorScheme.ACCENT_GOLD);
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        addLabeledTextField(formPanel, gbc, "Full Name", 0);
        nameField = (JTextField) formPanel.getClientProperty("field_0");

        // Email
        addLabeledTextField(formPanel, gbc, "Email Address", 2);
        emailField = (JTextField) formPanel.getClientProperty("field_2");

        // Phone
        addLabeledTextField(formPanel, gbc, "Phone Number", 4);
        phoneField = (JTextField) formPanel.getClientProperty("field_4");

        // License Number
        addLabeledTextField(formPanel, gbc, "License Number", 6);
        licenseField = (JTextField) formPanel.getClientProperty("field_6");

        // Address
        addLabeledTextField(formPanel, gbc, "Address", 8);
        addressField = (JTextField) formPanel.getClientProperty("field_8");

        // Password
        addLabeledPasswordField(formPanel, gbc, "Password", 10);
        passwordField = (JPasswordField) formPanel.getClientProperty("field_10");

        // Confirm Password
        addLabeledPasswordField(formPanel, gbc, "Confirm Password", 12);
        confirmField = (JPasswordField) formPanel.getClientProperty("field_12");

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        RoundedButton registerBtn = new RoundedButton("REGISTER", ColorScheme.ACCENT_GOLD,
                new Color(255, 211, 20), new Color(255, 179, 0));
        registerBtn.addActionListener(e -> handleRegister());

        RoundedButton backBtn = new RoundedButton("BACK", ColorScheme.BUTTON_DEFAULT,
                ColorScheme.BUTTON_HOVER, ColorScheme.BUTTON_PRESSED);
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        gbc.gridy = 14;
        gbc.insets = new Insets(20, 20, 20, 20);
        formPanel.add(buttonPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void addLabeledTextField(JPanel panel, GridBagConstraints gbc, String labelText, int gridyStart) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy = gridyStart;
        panel.add(label, gbc);

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setBackground(ColorScheme.BACKGROUND_LIGHT);
        field.setForeground(ColorScheme.PRIMARY_DARK);
        gbc.gridy = gridyStart + 1;
        gbc.ipady = 5;
        panel.add(field, gbc);
        gbc.ipady = 0;

        panel.putClientProperty("field_" + gridyStart, field);
    }

    private void addLabeledPasswordField(JPanel panel, GridBagConstraints gbc, String labelText, int gridyStart) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy = gridyStart;
        panel.add(label, gbc);

        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setBackground(ColorScheme.BACKGROUND_LIGHT);
        field.setForeground(ColorScheme.PRIMARY_DARK);
        gbc.gridy = gridyStart + 1;
        gbc.ipady = 5;
        panel.add(field, gbc);
        gbc.ipady = 0;

        panel.putClientProperty("field_" + gridyStart, field);
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || license.isEmpty() || 
            address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (database.getCustomerByEmail(email) != null) {
            JOptionPane.showMessageDialog(this, "Email already registered!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer newCustomer = new Customer(database.getNextCustomerId(), name, email, 
                phone, license, address, password);
        database.addCustomer(newCustomer);

        JOptionPane.showMessageDialog(this, "Registration successful! Please login.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        new LoginFrame();
    }
}
