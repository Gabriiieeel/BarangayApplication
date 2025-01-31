package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * BaseForm is an abstract parent class for all forms (Login, Crime, etc.).
 * It includes repeated logic for:
 *  - Window initialization
 *  - DB connection path
 *  - Shared “logoutAndGoToLogin” usage
 *  - Handling background images or logos
 */
public abstract class BaseForm extends JFrame {

    // The universal DB path repeated in your original code:
    protected static final String DB_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";

    // Reference to the main controller
    protected BarrioSeguro appController;

    public BaseForm(BarrioSeguro appController) {
        this.appController = appController;
        // Window defaults
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1440, 1024);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Shared DB connection utility (instead of repeating in every form).
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_PATH);
    }

    /**
     * Add a background image from “Visuals/bg.png” or any image.
     * This was repeated code in your original forms.
     */
    protected void addBackgroundImage(JLayeredPane layeredPane) {
      ImageIcon backgroundImage = new ImageIcon("Visuals/bg.png");
      JLabel backgroundLabel = new JLabel() {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              Graphics2D g2d = (Graphics2D) g;
              g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
              // for opacity
              g2d.setColor(new Color(0, 0, 0, 160));
              g2d.fillRect(0, 0, getWidth(), getHeight());
          }
      };

      backgroundLabel.setBounds(0, 0, 1440, 1024);
      layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
  }

  protected void addDashboardPanel(JLayeredPane layeredPane) {
    JPanel dashboardPanel = new JPanel();
    dashboardPanel.setBackground(new Color(102, 77, 77, 178)); // Set red background
    dashboardPanel.setBounds(0, 0, 430, 1024);
    layeredPane.add(dashboardPanel, JLayeredPane.PALETTE_LAYER);

    addLogoToDashboard(dashboardPanel);

    // Add "BarrioSeguro" text inside the DashboardPanel
    addTextToDashboard(dashboardPanel);

    // Buttons inside the dashboard panel
    addDashboardButtons(dashboardPanel);
  }

  private void addLogoToDashboard(JPanel dashboardPanel) {
    ImageIcon logoImage = new ImageIcon("Visuals/logo.png");
    JLabel logoLabel = new JLabel(logoImage);
    logoLabel.setBounds(10, 33, 150, 150); 
    dashboardPanel.setLayout(null);
    dashboardPanel.add(logoLabel);
}

private void addTextToDashboard(JPanel dashboardPanel) {
    JLabel logonamelabel = new JLabel("BarrioSeguro");
    logonamelabel.setForeground(Color.WHITE);
    logonamelabel.setFont(new Font("Times New Roman", Font.BOLD, 39)); 
    logonamelabel.setBounds(170, 69, 237, 78);
    dashboardPanel.add(logonamelabel);
}

private void addDashboardButtons(JPanel dashboardPanel) {
  JButton announceBtn = new JButton("Announcement");
  announceBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
  announceBtn.setBounds(0, 250, 430, 78);
  announceBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          // Open the AnnouncementForm
          EventQueue.invokeLater(new Runnable() {
              public void run() {
                  try {
                      appController.openAnnouncementForm();
                  } catch (Exception ex) {
                      ex.printStackTrace();
                  }
                  dispose();
              }
          });
      }
  });
  dashboardPanel.add(announceBtn);

    announceBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
    announceBtn.setBounds(0, 250, 430, 78);
    dashboardPanel.add(announceBtn);

    JButton resdatBtn = new JButton("Resident Database");
    resdatBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                      appController.openResidentForm();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dispose();
                }
            });
        }
    });
    resdatBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
    resdatBtn.setBounds(0, 371, 430, 78);
    dashboardPanel.add(resdatBtn);

    JButton crimeBtn = new JButton("Incident Reports");
    crimeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EventQueue.invokeLater(new Runnable() {
              public void run() {
                  try {
                    appController.openCrimeForm();
                  } catch (Exception ex) {
                      ex.printStackTrace();
                  }
                  dispose();
              }
          });
      }
    });
    crimeBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
    crimeBtn.setBounds(0, 489, 430, 78);
    dashboardPanel.add(crimeBtn);

    JButton summBtn = new JButton("Summary Reports");
    summBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                      appController.openSummaryForm();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dispose();
                }
            });
        }
    });
    summBtn.setFont(new Font("Times New Roman", Font.PLAIN, 25));
    summBtn.setBounds(0, 611, 430, 78);
    dashboardPanel.add(summBtn);

    JButton logoutBtn = new JButton("Log Out");
    logoutBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Close the current HomepageForm
            dispose();
            
            // Open the LoginForm again
            EventQueue.invokeLater(() -> {
              logoutAndGoToLogin();
            });
        }
    });

    logoutBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
    logoutBtn.setBounds(135, 805, 150, 55);
    dashboardPanel.add(logoutBtn);
}

    /**
     * Example: “logout” or “return to login” if multiple forms had the same code snippet.
     */
    private void logoutAndGoToLogin() {
        dispose();
        LoginForm login = new LoginForm(appController);
        login.setVisible(true);
    }

    /**
     * Example placeholder for repeated date normalization or utility 
     * methods that you had scattered around. Add them here if multiple 
     * forms need them. Otherwise, keep them in the relevant form class.
     */

    /*
    protected java.sql.Date normalizeDate(String dob) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        java.util.Date parsedDate = dateFormat.parse(dob);
        return new java.sql.Date(parsedDate.getTime());
    }
    */
}
