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
        JLayeredPane homepagePane = new JLayeredPane();
        setContentPane(homepagePane);

        addBackgroundImage(homepagePane);
        addDashboardPanel(homepagePane);
        addWelcomePanel(homepagePane);
    }

    private void addWelcomePanel(JLayeredPane homepagePane) {
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        welcomePanel.setBounds(480, 185, 895, 722);
        welcomePanel.setBackground(new Color(102, 77, 77, 178));
        welcomePanel.setLayout(null);
        welcomePanel.setOpaque(false);

        homepagePane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);
        
        JLabel lblWelcome = new JLabel("Welcome!");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Times New Roman", Font.BOLD, 47));
        lblWelcome.setBounds(328, 25, 272, 106);
        welcomePanel.add(lblWelcome);
        
        JLabel logoLabel = new JLabel(new ImageIcon("Visuals/databaseLogoIcon.png"));
        logoLabel.setBounds(191, 123, 551, 529);
        welcomePanel.add(logoLabel);
        
        JLabel lblBrgyMonitorIncidentReport = new JLabel("Barangay Monitoring and Incident Reporting System");
        lblBrgyMonitorIncidentReport.setHorizontalAlignment(SwingConstants.CENTER);
        lblBrgyMonitorIncidentReport.setForeground(Color.WHITE);
        lblBrgyMonitorIncidentReport.setFont(new Font("Times New Roman", Font.BOLD, 27));
        lblBrgyMonitorIncidentReport.setBounds(98, 619, 752, 92);
        welcomePanel.add(lblBrgyMonitorIncidentReport);
    }
}
