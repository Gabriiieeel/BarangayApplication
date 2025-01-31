import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class SummaryForm {

    public JFrame frame;
    private JTable incident_table;
    private static final String DB_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";
    private JTextField searchtf;
    
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
    public SummaryForm() {
        initialize();
        loadIncidentData();
    }

    private void initialize() {
        frame = new JFrame("BarrioSeguro-Summary Reports");
        frame.setBounds(100, 100, 1440, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Use a JLayeredPane for managing layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1440, 1024);
        frame.setContentPane(layeredPane);

        addBackgroundImage(layeredPane);

        addDashboardPanel(layeredPane);

        addWelcomePanel(layeredPane);
    }

    private void addBackgroundImage(JLayeredPane layeredPane) {
        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("Visuals/bg.png");

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

        incident_table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing for all cells
            }
        };
        incident_table.setBounds(36, 38, 826, 581);
        welcomePanel.add(incident_table);

        String[] columnNames = {
            "First Name", "Last Name", "Date", "Progress"
        };

        // 4 columns
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        incident_table.setModel(model);

        // Add the table to a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(incident_table);
        scrollPane.setBounds(36, 80, 826, 561); // Set size same as table
        welcomePanel.add(scrollPane); // Add the scrollPane

        // adjust column widths
        incident_table.getColumnModel().getColumn(0).setPreferredWidth(150); // Set width for First Name column
        incident_table.getColumnModel().getColumn(1).setPreferredWidth(150); // Set width for Last Name column
        incident_table.getColumnModel().getColumn(2).setPreferredWidth(150); // Set width for Date column
        incident_table.getColumnModel().getColumn(3).setPreferredWidth(150); // Set width for Progress column

        searchtf = new JTextField("Search"); 
        searchtf.setToolTipText("");
        searchtf.setHorizontalAlignment(SwingConstants.LEFT);
        searchtf.setForeground(Color.LIGHT_GRAY);
        searchtf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchtf.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchtf.setBounds(36, 34, 216, 37);
        
        searchtf.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchtf.getText().equals("Search")) { 
                    searchtf.setText(""); 
                    searchtf.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchtf.getText().isEmpty()) {
                    searchtf.setText("Search"); 
                    searchtf.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        searchtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchQuery = searchtf.getText().trim().toLowerCase(); // Get the search query and convert to lowercase
                filterTable(searchQuery); // Call the method to filter the table
            }
        });
        welcomePanel.add(searchtf);
        
        JButton viewbtn = new JButton("VIEW");
        viewbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row from the table
                int selectedRow = incident_table.getSelectedRow();
                if (selectedRow != -1) {
                    // Retrieve only the visible data (columns 0 to 3)
                    String firstName = (String) incident_table.getValueAt(selectedRow, 0);
                    String lastName = (String) incident_table.getValueAt(selectedRow, 1);
                    String dateString = (String) incident_table.getValueAt(selectedRow, 2); 
                    String progress = (String) incident_table.getValueAt(selectedRow, 3);

                    // Convert the date string into java.sql.Date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date date = null;
                    try {
                        java.util.Date utilDate = sdf.parse(dateString);
                        date = new java.sql.Date(utilDate.getTime());  // Convert to java.sql.Date for SQL query
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid date format.");
                        return;
                    }

                    // Now, fetch all the data from the database to pass to CrimeForm
                    String description = "";
                    String typeOfIncident = "";

                    try (Connection conn = DriverManager.getConnection(DB_PATH)) {
                        String query = "SELECT incident_description, incident_type FROM IncidentDB " +
                                       "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, firstName);
                            pstmt.setString(2, lastName);
                            pstmt.setDate(3, date);  // Use the java.sql.Date object here
                            ResultSet rs = pstmt.executeQuery();

                            if (rs.next()) {
                                description = rs.getString("incident_description");
                                typeOfIncident = rs.getString("incident_type");
                            } else {
                                JOptionPane.showMessageDialog(frame, "No matching record found.");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Create an instance of CrimeForm
                    CrimeForm crimeForm = new CrimeForm();

                    // Pass the data to CrimeForm
                    crimeForm.fillData(firstName, "", lastName, "", dateString, progress, description, typeOfIncident);

                    // Show CrimeForm
                    crimeForm.frame.setVisible(true);

                    // Close the current SummaryForm
                    frame.dispose();
                }
            }
        });
        viewbtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        viewbtn.setBounds(326, 664, 160, 37);
        welcomePanel.add(viewbtn);

    }
    
    private void filterTable(String searchQuery) {
        DefaultTableModel model = (DefaultTableModel) incident_table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        incident_table.setRowSorter(sorter);

        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null); // Show all rows if the search query is empty
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery)); // Case-insensitive filtering
        }
    }
    
    private void loadIncidentData() {
        // Query to fetch only the required columns
        String query = "SELECT incident_firstName, incident_lastName, incident_date, incident_progress " +
                       "FROM IncidentDB";
        
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel) incident_table.getModel();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Loop through the result set and add data to the table
            while (rs.next()) {
                // Retrieve only the 4 required columns
                String firstName = rs.getString("incident_firstName");
                String lastName = rs.getString("incident_lastName");
                Date date = rs.getDate("incident_date");
                String progress = rs.getString("incident_progress");

                // Format the date in dd/MM/yyyy
                String formattedDate = sdf.format(date);

                // Add a row with the 4 required fields
                model.addRow(new Object[]{firstName, lastName, formattedDate, progress});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
