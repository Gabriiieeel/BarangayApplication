package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.border.EmptyBorder;

public class LoginForm extends BaseForm {

    public LoginForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Login");
        initialize();
    }

    private void initialize() {
        JLayeredPane loginPane = new JLayeredPane();
        setContentPane(loginPane);

        addBackgroundImage(loginPane);

        JPanel loginPanel = createLoginPanel();
        loginPane.add(loginPanel, JLayeredPane.PALETTE_LAYER);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(405, 135, 685, 814);
        loginPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        loginPanel.setLayout(null);

        addLogoImage(loginPanel); // Adds the logo
        addTextLabel(loginPanel); // Adds the "BarrioSeguro" title

        JPanel adminPanel = createAdminPanel();
        loginPanel.add(adminPanel);

        return loginPanel;
    }

    private void addLogoImage(JPanel loginPanel) {
        ImageIcon logoImage = new ImageIcon("Visuals/logoIcon.png");
        JLabel imageLabel = new JLabel(logoImage);
        imageLabel.setBounds(97, 30, 151, 151);
        loginPanel.add(imageLabel);
    }

    private void addTextLabel(JPanel loginPanel) {
        JLabel textLabel = new JLabel("BarrioSeguro");
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
        textLabel.setBounds(260, 61, 265, 88);
        loginPanel.add(textLabel);
    }

    @SuppressWarnings("unused")
    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75); // Rounded corners
            }
        };
        adminPanel.setBounds(61, 196, 561, 540);
        adminPanel.setBackground(new Color(102, 77, 77, 178)); // Semi-transparent dark background
        adminPanel.setLayout(null);
        adminPanel.setOpaque(false);

        JLabel adminTextLabel = new JLabel("ADMINISTRATOR");
        adminTextLabel.setForeground(Color.WHITE);
        adminTextLabel.setFont(new Font("Times New Roman", Font.BOLD, 41));
        adminTextLabel.setBounds(108, 24, 375, 88);
        adminPanel.add(adminTextLabel);

        JLabel idNumberTextLabel = new JLabel("Enter ID Number");
        idNumberTextLabel.setForeground(Color.WHITE);
        idNumberTextLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        idNumberTextLabel.setBounds(36, 201, 160, 26);
        adminPanel.add(idNumberTextLabel);

        JPasswordField idNumberTextBox = new JPasswordField();
        idNumberTextBox.setFont(new Font("SansSerif", Font.PLAIN, 30));
        idNumberTextBox.setBounds(36, 234, 486, 72);
        idNumberTextBox.setBorder(new EmptyBorder(10, 20, 10, 20));
        adminPanel.add(idNumberTextBox);

        JButton loginButton = new JButton("Log In");
        styleRoundedButton(loginButton); // Custom styling for button
        loginButton.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        loginButton.setBounds(184, 346, 193, 53);
        
        adminPanel.add(loginButton);
        
        // Event handler for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent eventForLogIn) {
                if (isValidID(new String(idNumberTextBox.getPassword()))) { // Check if ID is valid
                    dispose(); // Close login form
                    appController.openHomepageForm(); // Open homepage if successful
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid ID Number","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return adminPanel;
    }

    // Validates the entered ID against the database
    private boolean isValidID(String idNumber) {
        String query = "SELECT COUNT(*) FROM Barangay_Official WHERE idnumber = ?";

        try (Connection connectIDnumber = getConnection()) { // Establish database connection
            try (PreparedStatement statementIDnumber = connectIDnumber.prepareStatement(query)) {
                statementIDnumber.setString(1, idNumber);

                ResultSet resultIDnumber = statementIDnumber.executeQuery();
                resultIDnumber.next();
                int count = resultIDnumber.getInt(1);
                return count > 0; // Returns true if ID exists
            }
        } catch (SQLException handleDatabaseException) {
            handleDatabaseException.printStackTrace();
            JOptionPane.showMessageDialog(LoginForm.this, "Database error: " + handleDatabaseException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }
}
