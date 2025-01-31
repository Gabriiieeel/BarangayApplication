import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomepageForm {

    public JFrame frame;
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HomepageForm window = new HomepageForm();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HomepageForm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("BarrioSeguro-Homepage");
        frame.setBounds(100, 100, 1440, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // managing layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1440, 1024);
        frame.setContentPane(layeredPane);

        // Add background image
        addBackgroundImage(layeredPane);

        // Add DashboardPanel
        addDashboardPanel(layeredPane);

        addWelcomePanel(layeredPane);
    }

    private void addBackgroundImage(JLayeredPane layeredPane) {
        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("Visuals/bg.png");

        // Create a JLabel to hold the image
        JLabel backgroundLabel = new JLabel(backgroundImage) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image stretched to fit the panel size
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundLabel.setBounds(0, 0, 1440, 1024); 
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER); 
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

    private void addDashboardPanel(JLayeredPane layeredPane) {
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
    	                    AnnouncementForm announcementForm = new AnnouncementForm();
    	                    announcementForm.frame.setVisible(true); // Make the AnnouncementForm visible
    	                } catch (Exception ex) {
    	                    ex.printStackTrace();
    	                }
    	                frame.dispose();
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
      	                    ResidentForm residentform = new ResidentForm();
      	                  residentform.frame.setVisible(true); // Make the residentform visible
      	                } catch (Exception ex) {
      	                    ex.printStackTrace();
      	                }
      	                frame.dispose();
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
    	                    CrimeForm crimeform = new CrimeForm();
    	                    crimeform.frame.setVisible(true); // Make the residentform visible
    	                } catch (Exception ex) {
    	                    ex.printStackTrace();
    	                }
    	                frame.dispose();
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
      	                    SummaryForm summaryform = new SummaryForm();
      	                  summaryform.frame.setVisible(true); // Make the residentform visible
      	                } catch (Exception ex) {
      	                    ex.printStackTrace();
      	                }
      	                frame.dispose();
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
                frame.dispose();
                
                // Open the LoginForm again
                EventQueue.invokeLater(() -> {
                    LoginForm loginWindow = new LoginForm(); // Create a new instance of LoginForm
                    loginWindow.frame.setVisible(true); // Show the LoginForm
                });
            }
        });

        logoutBtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        logoutBtn.setBounds(135, 805, 150, 55);
        dashboardPanel.add(logoutBtn);
    }
}
