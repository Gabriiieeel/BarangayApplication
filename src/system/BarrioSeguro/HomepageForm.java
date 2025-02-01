// This line says our file is part of a package called "system.BarrioSeguro"  
// Think of a package as a folder or grouping for related classes
package system.BarrioSeguro;

// These lines let us use Java’s color, font, graphics features, images, labels,  
// layered panes, panels, and alignment options
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// "HomepageForm" is a class that extends "BaseForm"  
// "extends" means "HomepageForm" gets tools and abilities from "BaseForm"
public class HomepageForm extends BaseForm {

    // This is the constructor: it runs when we make a new "HomepageForm" object  
    // It calls the parent class constructor and sets the page title, then calls "initialize"
    public HomepageForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Homepage");
        initialize();
    }

    // This private method sets up everything on the homepage  
    // It makes a layered area and adds a background, dashboard, and welcome panel
    private void initialize() {
        // Create a new layered pane to hold our components  
        // Layered panes let us stack panels on top of the background
        JLayeredPane homepagePane = new JLayeredPane(); 
        // Tells Java that our main window content is this layered pane
        setContentPane(homepagePane);

        // Use a method from "BaseForm" to show the background image
        addBackgroundImage(homepagePane);
        // Also from "BaseForm," adding the left-hand dashboard panel
        addDashboardPanel(homepagePane);
        // A custom panel that says "Welcome!" and shows a big image
        addWelcomePanel(homepagePane);
    }

    // This method creates and adds a panel that says "Welcome!" and shows an app image  
    // It also has a label for "Barangay Monitoring and Incident Reporting System"
    private void addWelcomePanel(JLayeredPane homepagePane) {
        // Make a JPanel with a custom shape (rounded corners) using "paintComponent"
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                // Calls the usual painting code
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                // Make graphics edges smoother
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fill the panel with its background color
                paintGraphicsWith2D.setColor(getBackground());
                // Draw a round rectangle matching our panel’s size
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        // Place the panel on the screen at x=480, y=185; width=895, height=722
        welcomePanel.setBounds(480, 185, 895, 722);
        // Give it a semi-transparent dark background color (R=102, G=77, B=77, Alpha=178)
        welcomePanel.setBackground(new Color(102, 77, 77, 178));
        // Use no automatic layout manager, so we can position stuff ourselves
        welcomePanel.setLayout(null);
        // Make sure we can see the panel’s transparency
        welcomePanel.setOpaque(false);

        // Add the panel on top of other layers  
        // "PALETTE_LAYER" means it’s in front of the background
        homepagePane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);
        
        // This label says "Welcome!" in the center, large white text
        JLabel lblWelcome = new JLabel("Welcome!");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Times New Roman", Font.BOLD, 47));
        // Position the label at (305, 25) with width=272 and height=106
        lblWelcome.setBounds(305, 25, 272, 106);
        // Add it onto the welcomePanel
        welcomePanel.add(lblWelcome);
        
        // A label to hold an image from "databaseLogoIcon.png"
        JLabel logoLabel = new JLabel(new ImageIcon("Visuals/databaseLogoIcon.png"));
        // Position this image in the center area of the panel
        logoLabel.setBounds(166, 123, 551, 529);
        // Add the logo to the welcomePanel
        welcomePanel.add(logoLabel);
        
        // Another label that spells out the full system name for clarity
        JLabel lblBrgyMonitorIncidentReport = new JLabel("Barangay Monitoring and Incident Reporting System");
        // Align its text in the center
        lblBrgyMonitorIncidentReport.setHorizontalAlignment(SwingConstants.CENTER);
        // Make the text color white
        lblBrgyMonitorIncidentReport.setForeground(Color.WHITE);
        // Use Times New Roman, bold, size 27
        lblBrgyMonitorIncidentReport.setFont(new Font("Times New Roman", Font.BOLD, 27));
        // Position the label near the bottom of the panel
        lblBrgyMonitorIncidentReport.setBounds(73, 619, 752, 92);
        // Add it to the panel
        welcomePanel.add(lblBrgyMonitorIncidentReport);
    }
}
