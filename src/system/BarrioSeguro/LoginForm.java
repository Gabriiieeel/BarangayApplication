// We're declaring our file under "system.BarrioSeguro"  
// A package is basically a folder that groups similar code together
package system.BarrioSeguro;

// These lines give us the tools we need, like colors, fonts, and images  
// We also import classes for handling actions, layouts, and database connections
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

// "LoginForm" is a public class that extends "BaseForm"  
// This means "LoginForm" is a special type of window with extra functions built on "BaseForm"
public class LoginForm extends BaseForm {

    // Here is our constructor named "LoginForm" which takes a "BarrioSeguro" object  
    // This sets up the basic window settings and calls "initialize"
    public LoginForm(BarrioSeguro appController) {
        // Tells "BaseForm" to set up the window and pass in our app logic
        super(appController);
        // Title text at the top of the window
        setTitle("BarrioSeguro - Login");
        // Calls a separate method to build and add the various elements like panels
        initialize();
    }

    // "initialize" is a private method, meaning it's only used here  
    // It sets up the main layered pane and calls code to add the background and login controls
    private void initialize() {
        // "JLayeredPane" lets us stack panels or images  
        // We assign it to our form as the main area
        JLayeredPane loginPane = new JLayeredPane();
        setContentPane(loginPane);

        // Use a function from "BaseForm" to add the background image  
        // This puts a picture and a shading layer behind everything
        addBackgroundImage(loginPane);

        // We create a "JPanel" that holds the login controls and add it on top
        JPanel loginPanel = createLoginPanel();
        loginPane.add(loginPanel, JLayeredPane.PALETTE_LAYER);
    }

    // We build a "JPanel" called "createLoginPanel"  
    // This panel holds the logo, the title, and the admin section
    private JPanel createLoginPanel() {
        // Make a new panel, size it, and give it a transparent color so we can see the background
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(405, 135, 685, 814);
        loginPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        loginPanel.setLayout(null);
    
        // Puts the logo image on the panel’s top
        addLogoImage(loginPanel);
        // Adds the big “BarrioSeguro” text
        addTextLabel(loginPanel);

        // This panel is for admin login controls, like the username or ID text field
        JPanel adminPanel = createAdminPanel();
        loginPanel.add(adminPanel);

        // Return this panel so we can place it in the layered pane
        return loginPanel;
    }

    // Adds a small icon or picture on the loginPanel  
    // Typically a brand or app icon
    private void addLogoImage(JPanel loginPanel) {
        // "ImageIcon" loads the picture file called "logoIcon.png"
        ImageIcon logoImage = new ImageIcon("Visuals/logoIcon.png");
        // We create a JLabel that actually shows that icon
        JLabel imageLabel = new JLabel(logoImage);
        // Position the label at (120, 30) with a 151x151 size
        imageLabel.setBounds(120, 30, 151, 151);
        // Finally, add this label onto the panel
        loginPanel.add(imageLabel);
    }

    // Adds the "BarrioSeguro" text to our login panel
    private void addTextLabel(JPanel loginPanel) {
         // A simple label with white text
        JLabel textLabel = new JLabel("BarrioSeguro");
        textLabel.setForeground(Color.WHITE);
        // Pick a large, bold Times New Roman font, size 45
        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
        // Put it at (280, 61) in the panel with 265x88 size
        textLabel.setBounds(280, 61, 265, 88);
        // Place it on the login panel
        loginPanel.add(textLabel);
    }

    // This private method creates a panel for administrator login  
    // It has a background color and a text field for entering an ID number
    @SuppressWarnings("unused")
    private JPanel createAdminPanel() {
        // We override "paintComponent" to give this panel rounded corners or special style
        JPanel adminPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                // Make edges smooth
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Paint the panel’s background color in a rectangle with rounded corners
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75); // Rounded corners
            }
        };

        // Sets size and color for the panel (with a semitransparent dark shade)
        adminPanel.setBounds(61, 196, 561, 540);
        adminPanel.setBackground(new Color(102, 77, 77, 178)); // Semi-transparent dark background
        adminPanel.setLayout(null);
        adminPanel.setOpaque(false);

        // A heading inside the panel that says "ADMINISTRATOR"
        JLabel adminTextLabel = new JLabel("ADMINISTRATOR");
        adminTextLabel.setForeground(Color.WHITE);
        adminTextLabel.setFont(new Font("Times New Roman", Font.BOLD, 41));
        adminTextLabel.setBounds(100, 24, 375, 88);
        adminPanel.add(adminTextLabel);

        // Add a small label telling the user to "Enter ID Number"
        JLabel idNumberTextLabel = new JLabel("Enter ID Number");
        idNumberTextLabel.setForeground(Color.WHITE);
        idNumberTextLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        idNumberTextLabel.setBounds(36, 201, 160, 26);
        adminPanel.add(idNumberTextLabel);

        // A "JPasswordField" to keep the ID secret (like a password)  
        // This is where the user types their ID
        JPasswordField idNumberTextBox = new JPasswordField();
        idNumberTextBox.setFont(new Font("SansSerif", Font.PLAIN, 30));
        idNumberTextBox.setBounds(36, 234, 486, 72);
        // Add an empty border inside the text box for spacing
        idNumberTextBox.setBorder(new EmptyBorder(10, 20, 10, 20));
        adminPanel.add(idNumberTextBox);

        // We create a "Log In" button and give it rounded style
        JButton loginButton = new JButton("Log In");
        styleRoundedButton(loginButton); // Custom styling for button
        loginButton.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        loginButton.setBounds(184, 346, 193, 53);
        
        adminPanel.add(loginButton);
        
        // When the user clicks "Log In," check if the ID is correct
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent eventForLogIn) {
                // If "isValidID" is true, we close this form and open the homepage
                if (isValidID(new String(idNumberTextBox.getPassword()))) { 
                    dispose(); // Close login form
                    appController.openHomepageForm();
                } else {
                    // If it's not a valid ID, show an error box
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid ID Number","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Finally, return the panel so we can add it to the login screen
        return adminPanel;
    }

    // This method checks if the typed ID exists in the database table named "Barangay_Official"
    private boolean isValidID(String idNumber) {
        // A text command that counts how many times this ID number appears
        String query = "SELECT COUNT(*) FROM Barangay_Official WHERE idnumber = ?";

        // "try" ensures we close our connection each time  
        // "Connection" is how we talk to the database
        try (Connection connectIDnumber = getConnection()) {
            // Prepare the SQL statement so we can safely set the ID
            try (PreparedStatement statementIDnumber = connectIDnumber.prepareStatement(query)) {
                // Replace the question mark with the actual ID
                statementIDnumber.setString(1, idNumber);

                // Execute the query and get results
                ResultSet resultIDnumber = statementIDnumber.executeQuery();
                resultIDnumber.next();
                // Get the count from the first column. If more than 0, ID is valid
                int count = resultIDnumber.getInt(1);
                return count > 0; // Returns true if ID exists
            }
        } catch (SQLException handleDatabaseException) {
            // If something is wrong, we print the error and show a message box
            handleDatabaseException.printStackTrace();
            JOptionPane.showMessageDialog(LoginForm.this, "Database error: " + handleDatabaseException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // If all else fails, we return false (invalid ID)
        return false;
    }
}
