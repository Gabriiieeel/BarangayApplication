package system.BarrioSeguro;

import java.awt.Font;
import java.awt.Color;
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
import javax.swing.BorderFactory;

import javax.swing.border.EmptyBorder;

/**
 * The form that appears first, prompting user login.
 * Replaces old “main” usage in your original login code.
 */
public class LoginForm extends BaseForm {

    public LoginForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Login");
        initialize();
    }

    private void initialize() {
        // Use layered pane for background + foreground
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Add background
        addBackgroundImage(layeredPane);

       // Add login panel
      JPanel loginPanel = createLoginPanel();
      layeredPane.add(loginPanel, JLayeredPane.PALETTE_LAYER);
  }

  private JPanel createLoginPanel() {
      JPanel loginPanel = new JPanel();
      loginPanel.setBounds(405, 135, 685, 814);
      loginPanel.setBackground(new Color(0, 0, 0, 0));
      loginPanel.setLayout(null);

      //logo
      addLogoImage(loginPanel);

      //"BarrioSeguro"
      addTextLabel(loginPanel);

      // Admin panel
      JPanel adminPanel = createAdminPanel();
      loginPanel.add(adminPanel);

      return loginPanel;
  }

  private void addLogoImage(JPanel loginPanel) {
      ImageIcon logoImage = new ImageIcon("Visuals/logo.png");
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

  private JPanel createAdminPanel() {
      JPanel adminPanel = new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2d = (Graphics2D) g;
              g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              g2d.setColor(getBackground());
              g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
          }
      };

      adminPanel.setBounds(61, 196, 561, 540);
      adminPanel.setBackground(new Color(102, 77, 77, 178));
      adminPanel.setLayout(null);
      adminPanel.setOpaque(false);

      // "ADMINISTRATOR" 
      JLabel adminTextLabel = new JLabel("ADMINISTRATOR");
      adminTextLabel.setForeground(Color.WHITE);
      adminTextLabel.setFont(new Font("Times New Roman", Font.BOLD, 41));
      adminTextLabel.setBounds(108, 24, 375, 88);
      adminPanel.add(adminTextLabel);

      // "Enter ID Number" 
      JLabel idNumberTextLabel = new JLabel("Enter ID Number");
      idNumberTextLabel.setForeground(Color.WHITE);
      idNumberTextLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
      idNumberTextLabel.setBounds(36, 201, 160, 26);
      adminPanel.add(idNumberTextLabel);

      // PassTextbox
      JPasswordField idNumberTextBox = new JPasswordField();
      idNumberTextBox.setFont(new Font("SansSerif", Font.PLAIN, 30));
      idNumberTextBox.setBounds(36, 234, 486, 72);
      idNumberTextBox.setBorder(new EmptyBorder(10, 20, 10, 20));
      adminPanel.add(idNumberTextBox);


      // LogIn button
      JButton loginButton = new JButton("Log In");
      loginButton.setFont(new Font("Times New Roman", Font.PLAIN, 25));
      loginButton.setForeground(Color.WHITE);
      loginButton.setBackground(new Color(220, 20, 60)); // Crimson color
      loginButton.setOpaque(true);
      loginButton.setContentAreaFilled(true);
      loginButton.setBorder(BorderFactory.createEmptyBorder());
      loginButton.setBounds(184, 346, 193, 53); // Adjusted placement
      adminPanel.add(loginButton);

      // btn fucntion
      loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           if (isValidID(new String(idNumberTextBox.getPassword()))) {
                dispose();
                appController.openHomepageForm();
            } else {
                JOptionPane.showMessageDialog(LoginForm.this,
                    "Invalid ID Number","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
      });

      return adminPanel;
  }

    /**
     * Check if ID is valid using DB. 
     * This was repeated code in your original snippet, 
     * now encapsulated here.
     */
  private boolean isValidID(String idNumber) {
      String query = "SELECT COUNT(*) FROM Barangay_Official WHERE idnumber = ?";

      try (Connection conn = getConnection()) {
          try (PreparedStatement pstmt = conn.prepareStatement(query)) {
              pstmt.setString(1, idNumber);

              ResultSet rs = pstmt.executeQuery();
              rs.next();
              int count = rs.getInt(1);
              return count > 0;
          }
      } catch (SQLException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }

      return false;
  }
}
