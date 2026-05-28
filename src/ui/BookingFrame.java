package ui;

import database.Database;
import models.Booking;
import models.Car;
import models.Customer;
import utils.ColorScheme;
import utils.GradientPanel;
import utils.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BookingFrame extends JFrame {
    private Customer customer;
    private Car car;
    private Database database;
    private MainDashboardFrame parentFrame;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JLabel totalCostLabel;

    public BookingFrame(Customer customer, Car car, MainDashboardFrame parentFrame) {
        this.customer = customer;
        this.car = car;
        this.database = Database.getInstance();
        this.parentFrame = parentFrame;

        setTitle("Luxury Car Rental - Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        GradientPanel mainPanel = new GradientPanel(ColorScheme.PRIMARY_DARK,
                ColorScheme.PRIMARY_LIGHT, GradientPanel.VERTICAL);
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("🏎️ Book: " + car.getBrand() + " " + car.getModel());
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(ColorScheme.ACCENT_GOLD);
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Car Details
        JLabel carDetailsLabel = new JLabel("Car Details");
        carDetailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        carDetailsLabel.setForeground(ColorScheme.ACCENT_GOLD);
        gbc.gridy = 0;
        contentPanel.add(carDetailsLabel, gbc);

        JLabel carInfoLabel = new JLabel(car.getFullDetails());
        carInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        carInfoLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        gbc.gridy = 1;
        contentPanel.add(carInfoLabel, gbc);

        // Start Date
        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        startDateLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy = 2;
        contentPanel.add(startDateLabel, gbc);

        startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startEditor);
        startDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        startDateSpinner.setBackground(ColorScheme.BACKGROUND_LIGHT);
        gbc.gridy = 3;
        gbc.ipady = 8;
        contentPanel.add(startDateSpinner, gbc);

        // End Date
        JLabel endDateLabel = new JLabel("End Date:");
        endDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        endDateLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        gbc.gridy = 4;
        gbc.ipady = 0;
        contentPanel.add(endDateLabel, gbc);

        endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endEditor);
        endDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));
        endDateSpinner.setBackground(ColorScheme.BACKGROUND_LIGHT);
        gbc.gridy = 5;
        gbc.ipady = 8;
        contentPanel.add(endDateSpinner, gbc);

        // Pricing Info
        JPanel pricingPanel = new JPanel();
        pricingPanel.setOpaque(false);
        pricingPanel.setLayout(new BoxLayout(pricingPanel, BoxLayout.Y_AXIS));
        pricingPanel.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER_COLOR, 1));
        pricingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel pricePerDayLabel = new JLabel("Price per day: ₹" + String.format("%.2f", car.getPricePerDay()));
        pricePerDayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pricePerDayLabel.setForeground(ColorScheme.SUCCESS_GREEN);

        totalCostLabel = new JLabel("Total Cost: ₹" + String.format("%.2f", calculateCost()));
        totalCostLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalCostLabel.setForeground(ColorScheme.ACCENT_GOLD);

        pricingPanel.add(pricePerDayLabel);
        pricingPanel.add(Box.createVerticalStrut(5));
        pricingPanel.add(totalCostLabel);

        gbc.gridy = 6;
        contentPanel.add(pricingPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        RoundedButton bookBtn = new RoundedButton("CONFIRM BOOKING", ColorScheme.ACCENT_GOLD,
                new Color(255, 211, 20), new Color(255, 179, 0));
        bookBtn.addActionListener(e -> handleBooking());

        RoundedButton cancelBtn = new RoundedButton("CANCEL", ColorScheme.BUTTON_DEFAULT,
                ColorScheme.BUTTON_HOVER, ColorScheme.BUTTON_PRESSED);
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridy = 7;
        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(buttonPanel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Add listener for date changes
        startDateSpinner.addChangeListener(e -> updateTotalCost());
        endDateSpinner.addChangeListener(e -> updateTotalCost());
    }

    private void updateTotalCost() {
        totalCostLabel.setText("Total Cost: ₹" + String.format("%.2f", calculateCost()));
    }

    private double calculateCost() {
        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long days = diffInMillies / (24 * 60 * 60 * 1000);

        if (days == 0) days = 1;

        return days * car.getPricePerDay();
    }

    private void handleBooking() {
        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        LocalDate start = new java.sql.Date(startDate.getTime()).toLocalDate();
        LocalDate end = new java.sql.Date(endDate.getTime()).toLocalDate();

        if (end.isBefore(start) || end.isEqual(start)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date!", 
                    "Invalid Dates", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalCost = calculateCost();
        Booking booking = new Booking(database.getNextBookingId(), customer.getCustomerId(),
                car.getCarId(), start, end, "confirmed", totalCost);
        
        database.addBooking(booking);

        JOptionPane.showMessageDialog(this, 
                "Booking Confirmed!\n\nBooking ID: #" + booking.getBookingId() + 
                "\nTotal Cost: ₹" + String.format("%.2f", totalCost) +
                "\n\nThank you for your booking!",
                "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
