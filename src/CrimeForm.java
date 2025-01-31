import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

public class CrimeForm {

    public JFrame frame;
    private JTextField incFName;
    private JTextField incMidName;
    private JTextField incLName;
    private JTextField incSufName;
    private JTextField incDate;
    private JComboBox<String> incType;
    private JComboBox<String> incProg;
    private JTextArea incDescrip;

    private static final String DB_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";//db
    
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

    /**
     * @wbp.parser.entryPoint
     */
    public CrimeForm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("BarrioSeguro-Incident Reports");
        frame.setBounds(100, 100, 1440, 1024); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // for managing layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1440, 1024);
        frame.setContentPane(layeredPane);
        addBackgroundImage(layeredPane);
        addDashboardPanel(layeredPane);
        addWelcomePanel(layeredPane);
    }

    private void addBackgroundImage(JLayeredPane layeredPane) {
        //background image
        ImageIcon backgroundImage = new ImageIcon("Visuals/bg.png");
        JLabel backgroundLabel = new JLabel(backgroundImage) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
        welcomePanel.setBounds(480, 185, 895, 644);
        welcomePanel.setBackground(new Color(102, 77, 77, 178));
        welcomePanel.setLayout(null);
        welcomePanel.setOpaque(false);
        layeredPane.add(welcomePanel, JLayeredPane.PALETTE_LAYER);
        
        incFName = new JTextField("");
        incFName.setToolTipText("");
        incFName.setHorizontalAlignment(SwingConstants.LEFT);
        incFName.setForeground(new Color(0, 0, 0));
        incFName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incFName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incFName.setBounds(40, 80, 216, 36);
        
        welcomePanel.add(incFName);
        
        incMidName = new JTextField("");
        incMidName.setToolTipText("");
        incMidName.setHorizontalAlignment(SwingConstants.LEFT);
        incMidName.setForeground(new Color(0, 0, 0));
        incMidName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incMidName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incMidName.setBounds(40, 142, 216, 36);
        welcomePanel.add(incMidName);
        
        incLName = new JTextField("");
        incLName.setToolTipText("");
        incLName.setHorizontalAlignment(SwingConstants.LEFT);
        incLName.setForeground(new Color(0, 0, 0));
        incLName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incLName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incLName.setBounds(40, 204, 216, 36);
        welcomePanel.add(incLName);
        
        incSufName = new JTextField("");
        incSufName.setToolTipText("");
        incSufName.setHorizontalAlignment(SwingConstants.LEFT);
        incSufName.setForeground(new Color(0, 0, 0));
        incSufName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incSufName.setBorder(new EmptyBorder(10, 10, 10, 10));
        incSufName.setBounds(40, 266, 216, 36);
        welcomePanel.add(incSufName);
        
        incDate = new JTextField("");
        incDate.setToolTipText("");
        incDate.setHorizontalAlignment(SwingConstants.LEFT);
        incDate.setForeground(new Color(0, 0, 0));
        incDate.setFont(new Font("SansSerif", Font.PLAIN, 12));
        incDate.setBorder(new EmptyBorder(10, 10, 10, 10));
        incDate.setBounds(40, 328, 216, 36);
        welcomePanel.add(incDate);
        
        incType = new JComboBox();
        incType.setModel(new DefaultComboBoxModel(new String[] {"Assault", "Burglary", "Domestic Violence", "Drug Possession", "Fraud", "Harassment", "Physical Abuse", "Theft", "Traffic Violation", "Vandalism", "Arson", "Robbery", "Homicide", "Kidnapping", "Shoplifting", "Identity Theft", "Embezzlement", "Money Laundering", "Stalking", "Sexual Assault", "Child Abuse", "Human Trafficking", "Battery", "Bribery", "Extortion", "Counterfeiting", "Piracy", "Public Intoxication", "Rape", "Prostitution", "Weapons Possession", "Animal Cruelty", "Cybercrime", "Blackmail", "Tax Evasion", "Illegal Gambling", "Wire Fraud", "Trespassing", "Insurance Fraud", "Unlawful Detention", "Obstruction of Justice", "Racketeering", "Manslaughter", "Money Counterfeiting", "Corruption", "Illegal Search and Seizure", "Forgery", "Drug Trafficking", "Terrorism", "Public Disorder", "Bribing a Witness", "Reckless Driving", "Destruction of Property", "Coercion"}));
        incType.setBounds(40, 390, 216, 36);
        welcomePanel.add(incType);
        
        incProg = new JComboBox();
        incProg.setModel(new DefaultComboBoxModel(new String[] {"Under Investigation", "Ongoing", "Resolved", "Closed"}));
        incProg.setBounds(40, 452, 216, 36);
        welcomePanel.add(incProg);
        
        incDescrip = new JTextArea("Enter a message...") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && getForeground() == Color.LIGHT_GRAY) {
                    g.setColor(getForeground());
                    g.drawString("Enter a message...", 10, 20);
                }
            }
        };
        incDescrip.setFont(new Font("SansSerif", Font.PLAIN, 14));
        incDescrip.setForeground(new Color(0, 0, 0));
        incDescrip.setWrapStyleWord(true);
        incDescrip.setBounds(273, 65, 583, 423);
        incDescrip.setLineWrap(true);
      
        // Set margin padding in tf
        incDescrip.setMargin(new Insets(20, 20, 20, 20));
        //for placeholder
        incDescrip.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (incDescrip.getText().equals("Enter a message...")) {
                	incDescrip.setText(""); 
                	incDescrip.setForeground(Color.BLACK); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (incDescrip.getText().isEmpty()) {
                	incDescrip.setText("Enter a message..."); 
                	incDescrip.setForeground(Color.LIGHT_GRAY); 
                }
            }
        });
        welcomePanel.add(incDescrip);
        
        JLabel lblNewLabel = new JLabel("Enter First Name");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblNewLabel.setBounds(40, 65, 118, 19);
        welcomePanel.add(lblNewLabel);
        
        JLabel lblEnterMiddleName = new JLabel("Enter Middle Name (empty if none)");
        lblEnterMiddleName.setForeground(new Color(255, 255, 255));
        lblEnterMiddleName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterMiddleName.setBounds(40, 127, 216, 18);
        welcomePanel.add(lblEnterMiddleName);
        
        JLabel lblEnterLastName = new JLabel("Enter Last Name");
        lblEnterLastName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterLastName.setForeground(new Color(255, 255, 255));
        lblEnterLastName.setBounds(40, 189, 184, 18);
        welcomePanel.add(lblEnterLastName);
        
        JLabel lblEnterSuffixempty = new JLabel("Enter Suffix (empty if none)");
        lblEnterSuffixempty.setForeground(new Color(255, 255, 255));
        lblEnterSuffixempty.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterSuffixempty.setBounds(40, 251, 184, 18);
        welcomePanel.add(lblEnterSuffixempty);
        
        JLabel lblEnterDate = new JLabel("Enter Date: DD/MM/YYYY");
        lblEnterDate.setForeground(new Color(255, 255, 255));
        lblEnterDate.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblEnterDate.setBounds(40, 313, 184, 18);
        welcomePanel.add(lblEnterDate);
        
        JLabel lblChooseAType = new JLabel("Choose a type of Incident");
        lblChooseAType.setForeground(new Color(255, 255, 255));
        lblChooseAType.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblChooseAType.setBounds(40, 375, 184, 21);
        welcomePanel.add(lblChooseAType);
        
        JLabel lblIncidentProgress = new JLabel("Incident Progress");
        lblIncidentProgress.setForeground(new Color(255, 255, 255));
        lblIncidentProgress.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        lblIncidentProgress.setBounds(40, 437, 184, 18);
        welcomePanel.add(lblIncidentProgress);
        
        JButton submitbtn = new JButton("Submit");
        submitbtn.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        submitbtn.setBounds(378, 529, 150, 55);
        submitbtn.addActionListener(e -> submitIncident());
        welcomePanel.add(submitbtn);
    }
    
    private boolean validateFormFields() {
        if (incFName.getText().isEmpty() || incLName.getText().isEmpty() || incDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "First Name, Last Name, and Date are required fields.");
            return false;
        }
        return true;
    }
    private void submitIncident() {
        if (!validateFormFields()) {
            return;
        }

        try {
            //date should be in dd/MM/yyyy format
            String dateString = incDate.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            // Check if the date entered matches the expected format
            Date parsedDate = null;
            try {
                parsedDate = sdf.parse(dateString);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use dd/MM/yyyy.");
                return;
            }

            // Convert the Date object to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            // check if the record exists
            try (Connection conn = DriverManager.getConnection(DB_PATH)) {
                String checkQuery = "SELECT COUNT(*) FROM IncidentDB WHERE incident_firstName = ? " +
                                     "AND incident_lastName = ? AND incident_date = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, incFName.getText());
                    checkStmt.setString(2, incLName.getText());
                    checkStmt.setDate(3, sqlDate);

                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);

                    if (count > 0) {
                        // If the record exists,  UPDATE
                        String updateQuery = "UPDATE IncidentDB SET incident_midName = ?, incident_suffix = ?, " +
                                             "incident_type = ?, incident_description = ?, incident_progress = ? " +
                                             "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, incMidName.getText());
                            updateStmt.setString(2, incSufName.getText());
                            updateStmt.setString(3, (String) incType.getSelectedItem());
                            updateStmt.setString(4, incDescrip.getText());
                            updateStmt.setString(5, (String) incProg.getSelectedItem());
                            updateStmt.setString(6, incFName.getText());
                            updateStmt.setString(7, incLName.getText());
                            updateStmt.setDate(8, sqlDate);

                            int rowsUpdated = updateStmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(frame, "Incident Report Saved Successfully!");
                            } else {
                                JOptionPane.showMessageDialog(frame, "No matching record found to update.");
                            }
                        }
                    } else {
                        // If the record doesn't exist,  INSERT
                        String insertQuery = "INSERT INTO IncidentDB (incident_firstName, incident_midName, " +
                                             "incident_lastName, incident_suffix, incident_date, incident_type, " +
                                             "incident_description, incident_progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, incFName.getText());
                            insertStmt.setString(2, incMidName.getText());
                            insertStmt.setString(3, incLName.getText());
                            insertStmt.setString(4, incSufName.getText());
                            insertStmt.setDate(5, sqlDate); // Use the parsed SQL Date here
                            insertStmt.setString(6, (String) incType.getSelectedItem());
                            insertStmt.setString(7, incDescrip.getText());
                            insertStmt.setString(8, (String) incProg.getSelectedItem());

                            insertStmt.executeUpdate();
                            JOptionPane.showMessageDialog(frame, "Incident Report Submitted Successfully!");
                        }
                    }

                    // Clear the form fields
                    clearFormFields();
                    
                    //for a new entry
                    incFName.requestFocus();

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error submitting incident report: " + e.getMessage());
            }
        } catch (Exception e) {
            // Log unexpected errors
            JOptionPane.showMessageDialog(frame, "Unexpected error: " + e.getMessage());
        }
    }
    private void clearFormFields() {
        // reset form for a new entry
        incFName.setText("");
        incMidName.setText("");
        incLName.setText("");
        incSufName.setText("");
        incDate.setText("");
        incType.setSelectedIndex(0); 
        incProg.setSelectedIndex(0); 
        incDescrip.setText("Enter a message...");
        incDescrip.setForeground(Color.LIGHT_GRAY);
    }

    private void addDashboardPanel(JLayeredPane layeredPane) {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(new Color(102, 77, 77, 178));
        dashboardPanel.setBounds(0, 0, 430, 1024); 
        layeredPane.add(dashboardPanel, JLayeredPane.PALETTE_LAYER);


        addLogoToDashboard(dashboardPanel);


        addTextToDashboard(dashboardPanel);


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
                
                // Open the LoginForm
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
    
    public void fillData(String firstName, String middleName, String lastName, String suffix, String date, String progress, String description, String typeOfIncident) {
        // Set the fields in CrimeForm with the passed data
        incFName.setText(firstName);       
        incMidName.setText(middleName);     
        incLName.setText(lastName);        
        incSufName.setText(suffix);         
        incDate.setText(date);             
        incType.setSelectedItem(typeOfIncident); 
        incDescrip.setText(description);   
        incProg.setSelectedItem(progress); 
    }
    
}
