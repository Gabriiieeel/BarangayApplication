// This line organizes our file under the "system.BarrioSeguro" package  
// Think of a package like a folder name for better file grouping
package system.BarrioSeguro;

// These lines bring in tools from Java that we need  
// Like color settings, fonts, pictures, and text fields
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;

import javax.swing.plaf.basic.BasicButtonUI;

// "BaseForm" is an abstract class, extending "JFrame"  
// A class is like a blueprint, and "JFrame" is a window on the screen
public abstract class BaseForm extends JFrame {

    // This is a "static final String" for the database path  
    // It's a text label that doesn't change, telling us where our database is
    protected static final String DATABASE_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";

    // This is a variable named "appController" of type "BarrioSeguro"  
    // It lets us call functions from the "BarrioSeguro" class
    protected BarrioSeguro appController;

    // These are three 'Color' variables for default, click, and hover states  
    // They help us make nice color changes when someone clicks a button
    protected final Color DEFAULT_COLOR = new Color(220, 20, 60);
    protected final Color CLICK_COLOR = new Color(180, 0, 40);
    protected final Color HOVER_COLOR = new Color(200, 0, 50);

    // This is the constructor for "BaseForm"  
    // A constructor sets up the window, including its size and behavior
    public BaseForm(BarrioSeguro accessAppControl) {
        // We set our 'appController' to the one given from outside  
        // So we can use it to navigate or open different screens
        appController = accessAppControl;

        // The window closes when you hit the exit button (X)  
        // That means the program will stop if you click the close button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets the window position and size: (100, 100) is the top-left corner, 
        // 1440 wide x 1024 tall  
        setBounds(100, 100, 1440, 1024);

        // We don’t want the user to resize this window  
        // So the window’s size cannot be changed
        setResizable(false);

        // Puts the window in the center of the screen  
        // This makes it easier to see
        setLocationRelativeTo(null);
        
        // Calls a separate function to set up our app’s icon  
        // The icon is basically the small picture on the top-left of the window
        setApplicationIcon();
    }

    // This method sets the window icon if the file exists  
    // If not, we print an error message for debugging
    private void setApplicationIcon() {
        // Creates a File object pointing to "Visuals/logoIcon.png"  
        // That’s our icon image
        File iconFile = new File("Visuals/logoIcon.png");
        // Checks if the file is really there  
        // If yes, we set it as our window icon
        if (iconFile.exists()) {
            ImageIcon appIcon = new ImageIcon(iconFile.getAbsolutePath());
            setIconImage(appIcon.getImage());
        } else {
            System.err.println("Error: Window icon not found at " + iconFile.getAbsolutePath());
        }
    }

    // This function gives us a database connection  
    // "throws SQLException" means it might show an error if we can’t connect
    protected Connection getConnection() throws SQLException {
        // Uses "DriverManager" to get a connection to our Access database  
        // We must have that .accdb file for it to work
        return DriverManager.getConnection(DATABASE_PATH);
    }

    // Adds a background image to a layered pane  
    // A layered pane can stack items (like pictures and panels) on top of each other
    protected void addBackgroundImage(JLayeredPane mainPane) {
        // We load the background picture from "Visuals/backgroundImage.png"  
        // "ImageIcon" holds the picture data
        ImageIcon backgroundImage = new ImageIcon("Visuals/backgroundImage.png");
        // We create a "JLabel" for showing that picture  
        // Inside "paintComponent", we draw the image to fill the whole panel
        JLabel backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                // Draw the image so it covers the label  
                paintGraphicsWith2D.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);

                // Draw a dark translucent rectangle on top  
                // This gives a shaded effect
                paintGraphicsWith2D.setColor(new Color(0, 0, 0, 160));
                paintGraphicsWith2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // We place the label at (0,0) with size 1440 x 1024  
        // That means it covers our entire window background
        backgroundLabel.setBounds(0, 0, 1440, 1024);

        // We add this label to the mainPane, behind everything else  
        // "DEFAULT_LAYER" is basically the bottom layer
        mainPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
    }

    // Adds a dashboard side panel to our main window  
    // This panel has buttons and a logo so users can navigate
    protected void addDashboardPanel(JLayeredPane mainPane) {
        // Creates a "JPanel" that’s semi-transparent (with RGBA color)  
        // The last value (178) is the alpha channel for transparency
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(new Color(102, 77, 77, 178));
        // We put this panel on the left side: 0,0 to 430 wide, 1024 tall  
        // It spans from top to bottom
        dashboardPanel.setBounds(0, 0, 430, 1024);
        // We place it on a layer above the default background
        mainPane.add(dashboardPanel, JLayeredPane.PALETTE_LAYER);

        // We call separate methods to put items on the dashboard
        addLogoToDashboard(dashboardPanel);
        addTextToDashboard(dashboardPanel);
        addDashboardButtons(dashboardPanel);
    }

    // This method adds a logo image onto the dashboard panel  
    // The logo is just a small picture at the top
    private void addLogoToDashboard(JPanel dashboardPanel) {
        // Load the "logoIcon.png" file for display  
        // "ImageIcon" prepares the picture so we can show it
        ImageIcon logoImage = new ImageIcon("Visuals/logoIcon.png");
    
        // Create a "JLabel" that holds that icon  
        JLabel logoLabel = new JLabel(logoImage);
        // Position the icon in the panel at (18, 33), with size 150x150
        logoLabel.setBounds(18, 33, 150, 150); 
        // Use no layout manager so we can manually place items
        dashboardPanel.setLayout(null);
        // Add the label to the dashboard panel
        dashboardPanel.add(logoLabel);
    }

    // This method adds text to the dashboard panel, like the app’s name  
    // "JLabel" shows words on the screen
    private void addTextToDashboard(JPanel dashboardPanel) {
        // "BarrioSeguro" is the text we want to display  
        // We set the color to white for visibility
        JLabel logonamelabel = new JLabel("BarrioSeguro");
        logonamelabel.setForeground(Color.WHITE);
        // We set a big font style so people can notice it
        logonamelabel.setFont(new Font("Times New Roman", Font.BOLD, 39)); 
        // Position it in the panel
        logonamelabel.setBounds(178, 69, 237, 78);
        // Actually add it to our dashboard
        dashboardPanel.add(logonamelabel);
    }

    // This sets up all the buttons on the dashboard panel, like "Announcement" or "Log Out"  
    // We create each button, style it, and tell it what to do when clicked
    private void addDashboardButtons(JPanel dashboardPanel) {
        // First button is called "announceBtn" with text "Announcement"  
        // We use "styleButton" to make it look nice
        JButton announceBtn = new JButton("Announcement");
        styleButton(announceBtn);
        announceBtn.setBounds(0, 250, 430, 78);
    
        // This is what happens when we click "Announcement"  
        announceBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForAnnounceBtn) {
                // Move to a new screen called the Announcement Form  
                // We do it with EventQueue.invokeLater to handle it safely
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openAnnouncementForm();
                        } catch (Exception handleAnnounceException) {
                            handleAnnounceException.printStackTrace();
                        }
                        // Close this window after opening the next
                        dispose();
                    }
                });
            }
        });
        // Finally, add this button to the dashboard panel
        dashboardPanel.add(announceBtn);


        // Next button: "Resident Database"
        JButton residentBtn = new JButton("Resident Database");
        styleButton(residentBtn);
        residentBtn.setBounds(0, 371, 430, 78);

        // When we click "Resident Database," open the Resident Form
        residentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForResidentBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openResidentForm();
                        } catch (Exception handleResidentException) {
                            handleResidentException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(residentBtn);


        // Another button: "Incident Reports"
        JButton incidentBtn = new JButton("Incident Reports");
        styleButton(incidentBtn);
        incidentBtn.setBounds(0, 489, 430, 78);

        // Clicking "Incident Reports" calls the method to open it
        incidentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForIncidentBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openIncidentForm();
                        } catch (Exception handleIncidentException) {
                            handleIncidentException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(incidentBtn);


        // Button for "Summary Reports"
        JButton summaryBtn = new JButton("Summary Reports");
        styleButton(summaryBtn);

        // Click "Summary Reports" to open that form
        summaryBtn.setBounds(0, 611, 430, 78);
        summaryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSummaryBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openSummaryForm();
                        } catch (Exception handleSummaryException) {
                            handleSummaryException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(summaryBtn);

        // "Log Out" button for leaving the program
        JButton logoutBtn = new JButton("Log Out");
        styleRoundedButton(logoutBtn);
        logoutBtn.setBounds(135, 805, 150, 55);

        // When clicked, close this form and go to the login screen
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSummaryBtn) {
                dispose();
                
                EventQueue.invokeLater(() -> {
                    logoutAndGoToLogin();
                });
            }
        });
        dashboardPanel.add(logoutBtn);
    }

    // This method changes how a normal button looks and behaves  
    // We set colors, fonts, and events for pressing, releasing, and hovering
    private void styleButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setBackground(DEFAULT_COLOR);

        // We use a "MouseAdapter" so we can respond to mouse interactions  
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent eventButtonPress) {
                // Change color when you press the button
                button.setBackground(CLICK_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent eventButtonRelease) {
                 // Reset color when you lift the mouse button
                button.setBackground(DEFAULT_COLOR);
            }

            @Override
            public void mouseEntered(MouseEvent eventButtonEnter) {
                // Slightly different color when you hover over it
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent eventButtonExit) {
                 // Go back to the usual color if your mouse leaves
                button.setBackground(DEFAULT_COLOR);
            }
        });
    }

    // Styles a "rounded button" using the same base style, with curved edges  
    // Rounded corners are done by painting a round rectangle in the background
    protected void styleRoundedButton(JButton button) {
        // First, apply the normal styling
        styleButton(button);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        // Make the button see-through for the round effect
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent eventButtonPress) {
                button.setBackground(CLICK_COLOR);
            }
    
            @Override
            public void mouseReleased(MouseEvent eventButtonRelease) {
                button.setBackground(DEFAULT_COLOR);
            }
        });
    
        // Change how the button is drawn (the UI) to get those rounded edges
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics paintGraphics, JComponent paintComponent) {
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                // Turn on smoother edges
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Use the button’s current background color
                paintGraphics2D.setColor(button.getBackground());
                // Paint a round rectangle in the entire shape of the component
                paintGraphics2D.fillRoundRect(0, 0, paintComponent.getWidth(), paintComponent.getHeight(), 30, 30);
                paintGraphics2D.dispose();
                // Call the usual painting so the text shows up
                super.paint(paintGraphics, paintComponent);
            }
        });
    }

    // A small function to log out and show the login screen again  
    // It closes the current window and creates a new "LoginForm"
    private void logoutAndGoToLogin() {
        // Stop showing this window
        dispose();
        // Create a new "LoginForm" using our controller and show it
        LoginForm login = new LoginForm(appController);
        login.setVisible(true);
    }

    // Creates a text field with rounded corners and optional initial text  
    // Good for user input boxes like name or address
    protected JTextField createRoundedTextField(String text, int cornerRadius) {
        JTextField givenTextField = new JTextField(text) {
            // "Override" the paint methods so we can draw the curved shape
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill with white color inside the rectangle
                paintGraphics2D.setColor(Color.WHITE);
                paintGraphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                // Call the normal painting to show text
                super.paintComponent(paintGraphics);
                paintGraphics2D.dispose();
            }

            @Override
            protected void paintBorder(Graphics paintGraphics) {
                // Draw a light-gray border around the curved rectangle
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphics2D.setColor(Color.GRAY);
                paintGraphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
                paintGraphics2D.dispose();
            }
        };
        // Make the text field see-through, except the white shape we draw
        givenTextField.setOpaque(false);
        // Add extra space inside the text field
        givenTextField.setBorder(new EmptyBorder(10, 20, 10, 20));
        // Finally, return the completed text field so we can use it
        return givenTextField;
    }

    // Creates a rounded text area for typing multiple lines of text  
    // Like writing longer comments or messages
    public JTextArea createRoundedTextArea(String placeholder, int width, int height) {
        // We override painting again for the shape
        JTextArea givenTextArea = new JTextArea(placeholder) {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill with the background color (light shade)
                paintGraphics2D.setColor(getBackground());
                paintGraphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                paintGraphics2D.dispose();
                // Then do the normal text painting
                super.paintComponent(paintGraphics);
            }

            @Override
            protected void paintBorder(Graphics paintGraphics) {
                // Draw a gray border around the curved rectangle
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphics2D.setColor(Color.GRAY);
                paintGraphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                paintGraphics2D.dispose();
            }
        };

        // Make it see-through so the round shape is shown
        givenTextArea.setOpaque(false);
        // Light pinkish background to differentiate
        givenTextArea.setBackground(new Color(255, 244, 244));
        // The text color is soft
        givenTextArea.setForeground(new Color(0,0,0,50));
        // The font is "SansSerif" size 14
        givenTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        // Add a border so text has some padding on all sides
        givenTextArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        // Set the location and size in our layout
        givenTextArea.setBounds(51, 136, width, height);
        // Wrap words properly to the next line (no horizontal scrolling)
        givenTextArea.setWrapStyleWord(true);
        // Automatically move words to next line
        givenTextArea.setLineWrap(true);
        // Extra margin around the text
        givenTextArea.setMargin(new Insets(20, 20, 20, 20));

        // Listen for when people click inside or leave the text area
        givenTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent eventFocusTextArea) {
                // If our text area still has the placeholder text, clear it 
                if (givenTextArea.getText().equals(placeholder)) {
                    givenTextArea.setText("");
                    givenTextArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventFocusTextArea) {
                // If the user left it empty, bring back the placeholder
                if (givenTextArea.getText().isEmpty()) {
                    givenTextArea.setText(placeholder);
                    givenTextArea.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        // Return the final ready-to-use text area
        return givenTextArea;
    }
}
