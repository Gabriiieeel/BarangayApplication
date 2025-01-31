package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Font;

/**
 * The main dashboard or “homepage” after login,
 * with buttons to open other forms: CrimeForm, ResidentForm, etc.
 */
public class HomepageForm extends BaseForm {

    public HomepageForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Homepage");
        initialize();
    }

    private void initialize() {
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        addBackgroundImage(layeredPane);

        // Add DashboardPanel
        addDashboardPanel(layeredPane);


        addWelcomePanel(layeredPane);

        // If your original code had additional panels, frames, or layered content,
        // replicate that here.
        // // Additional logic from your original “HomepageForm” portion
    }

    private void addWelcomePanel(JLayeredPane layeredPane) {
      JPanel welcomePanel = new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2d = (Graphics2D) g;
              g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              g2d.setColor(getBackground());
              g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
          }
      };
      welcomePanel.setBounds(480, 185, 895, 722);
      welcomePanel.setBackground(new Color(102, 77, 77, 178));
      welcomePanel.setLayout(null);
      welcomePanel.setOpaque(false);

      layeredPane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);
      
      JLabel lblWelcome = new JLabel("Welcome!");
      lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
      lblWelcome.setForeground(Color.WHITE);
      lblWelcome.setFont(new Font("Times New Roman", Font.BOLD, 47));
      lblWelcome.setBounds(328, 25, 272, 106);
      welcomePanel.add(lblWelcome);
      
      JLabel logoLabel = new JLabel(new ImageIcon("Visuals/dblogo.png"));
      logoLabel.setBounds(191, 123, 551, 529);
      welcomePanel.add(logoLabel);
      
      JLabel lblBarangayMonitoringAnd = new JLabel("Barangay Monitoring and Crime Reporting System");
      lblBarangayMonitoringAnd.setHorizontalAlignment(SwingConstants.CENTER);
      lblBarangayMonitoringAnd.setForeground(Color.WHITE);
      lblBarangayMonitoringAnd.setFont(new Font("Times New Roman", Font.BOLD, 27));
      lblBarangayMonitoringAnd.setBounds(98, 619, 752, 92);
      welcomePanel.add(lblBarangayMonitoringAnd);

  }
}
