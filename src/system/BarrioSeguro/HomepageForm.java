package system.BarrioSeguro;

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

public class HomepageForm extends BaseForm {

    public HomepageForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Homepage");
        initialize();
    }

    private void initialize() {
        JLayeredPane homepagePane = new JLayeredPane(); // Create a layered pane for the homepage
        setContentPane(homepagePane);

        addBackgroundImage(homepagePane); // Add background image to the pane
        addDashboardPanel(homepagePane); // Add dashboard panel to the pane
        addWelcomePanel(homepagePane); // Add welcome panel to the pane
    }

    private void addWelcomePanel(JLayeredPane homepagePane) {
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Enable anti-aliasing for smoother graphics
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75); // Draw a rounded rectangle as the background
            }
        };
        welcomePanel.setBounds(480, 185, 895, 722); // Set the position and size of the welcome panel
        welcomePanel.setBackground(new Color(102, 77, 77, 178)); // Set the background color with transparency
        welcomePanel.setLayout(null); // Use absolute positioning
        welcomePanel.setOpaque(false); // Make the panel transparent

        homepagePane.add(welcomePanel, JLayeredPane.PALETTE_LAYER); // Add welcome panel to the layered pane
        
        JLabel lblWelcome = new JLabel("Welcome!"); // Create a welcome label
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text
        lblWelcome.setForeground(Color.WHITE); // Set the text color to white
        lblWelcome.setFont(new Font("Times New Roman", Font.BOLD, 47)); // Set the font style and size
        lblWelcome.setBounds(328, 25, 272, 106); // Set the position and size of the label
        welcomePanel.add(lblWelcome); // Add the welcome label to the welcome panel
        
        JLabel logoLabel = new JLabel(new ImageIcon("Visuals/databaseLogoIcon.png")); // Create a label with an image icon
        logoLabel.setBounds(191, 123, 551, 529); // Set the position and size of the image label
        welcomePanel.add(logoLabel); // Add the image label to the welcome panel
        
        JLabel lblBrgyMonitorIncidentReport = new JLabel("Barangay Monitoring and Incident Reporting System"); // Create a label for the system name
        lblBrgyMonitorIncidentReport.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text
        lblBrgyMonitorIncidentReport.setForeground(Color.WHITE); // Set the text color to white
        lblBrgyMonitorIncidentReport.setFont(new Font("Times New Roman", Font.BOLD, 27)); // Set the font style and size
        lblBrgyMonitorIncidentReport.setBounds(98, 619, 752, 92); // Set the position and size of the label
        welcomePanel.add(lblBrgyMonitorIncidentReport); // Add the system name label to the welcome panel
    }
}
