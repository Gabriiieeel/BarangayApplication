import net.ucanaccess.jdbc.UcanaccessDriver; // Import the UCanAccess driver
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date; // java.sql.Date for SQL queries
import java.util.Locale;
public class ResidentForm {

    public JFrame frame;
    private JTable res_tbl;
    private JTextField searchtf;
    private static final String DB_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";

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
    public ResidentForm() {
        initialize();
        loadResidentData();
    }

    private void initialize() {
        frame = new JFrame("BarrioSeguro-Resident Database");
        frame.setBounds(100, 100, 1440, 1024); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

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
    
		private void addResidentToDatabase(String firstName, String middleName, String lastName, String suffix,
			            String address, String dob, String contact, String email) {
			String query = "INSERT INTO ResidentDB (resident_firstName, resident_midName, resident_lastName, resident_suffix, " +
			"resident_address, resident_DoB, resident_contactNo, resident_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			try (Connection connection = DriverManager.getConnection(DB_PATH);
			PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
			// Convert DOB to a java.sql.Date object
			java.sql.Date sqlDate = convertToDate(dob);
			
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, middleName.isEmpty() ? null : middleName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setString(4, suffix.isEmpty() ? null : suffix);
			preparedStatement.setString(5, address);
			preparedStatement.setDate(6, sqlDate); // Pass the formatted Date object
			preparedStatement.setString(7, contact);
			preparedStatement.setString(8, email);
			
			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
			JOptionPane.showMessageDialog(frame, "Resident added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			loadResidentData(); // Reload the table data
			}
			} catch (ParseException e) {
			JOptionPane.showMessageDialog(frame, "Invalid Date of Birth format. Please use dd/MM/yyyy.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error adding resident: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			}
			
			//Helper method to convert a String to java.sql.Date
			private java.sql.Date convertToDate(String dob) throws ParseException {
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
			java.util.Date parsedDate = inputFormat.parse(dob);
			return new java.sql.Date(parsedDate.getTime()); // Convert to java.sql.Date
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
        
        res_tbl = new JTable();
        JScrollPane scrollPane = new JScrollPane(res_tbl);
        scrollPane.setBounds(22, 93, 848, 483);
        welcomePanel.add(scrollPane);;
        
        searchtf = new JTextField("Search"); 
        searchtf.setToolTipText("");
        searchtf.setHorizontalAlignment(SwingConstants.LEFT);
        searchtf.setForeground(Color.LIGHT_GRAY);
        searchtf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchtf.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchtf.setBounds(22, 45, 216, 37);
        
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
        
        welcomePanel.add(searchtf);
        
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a custom dialog for input
                JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
                panel.add(new JLabel("First Name:"));
                JTextField firstNameField = new JTextField();
                panel.add(firstNameField);

                panel.add(new JLabel("Middle Name (Optional):"));
                JTextField middleNameField = new JTextField();
                panel.add(middleNameField);

                panel.add(new JLabel("Last Name:"));
                JTextField lastNameField = new JTextField();
                panel.add(lastNameField);

                panel.add(new JLabel("Suffix (Optional):"));
                JTextField suffixField = new JTextField();
                panel.add(suffixField);

                panel.add(new JLabel("Address:"));
                JTextField addressField = new JTextField();
                panel.add(addressField);

                panel.add(new JLabel("Date of Birth (dd/mm/yyyy):"));
                JTextField dobField = new JTextField();
                panel.add(dobField);

                panel.add(new JLabel("Contact Number (11 digits):"));
                JTextField contactField = new JTextField();
                panel.add(contactField);

                panel.add(new JLabel("Email:"));
                JTextField emailField = new JTextField();
                panel.add(emailField);

                // Show the dialog
                int result = JOptionPane.showConfirmDialog(frame, panel, "Add Resident",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String firstName = firstNameField.getText().trim();
                    String middleName = middleNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String suffix = suffixField.getText().trim();
                    String address = addressField.getText().trim();
                    String dob = dobField.getText().trim();
                    String contact = contactField.getText().trim();
                    String email = emailField.getText().trim();

                    // Validate required fields
                    if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || dob.isEmpty() || contact.isEmpty() || email.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate contact number
                    if (!contact.matches("\\d{11}")) {
                        JOptionPane.showMessageDialog(frame, "Contact number must be 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate date of birth format
                    if (!dob.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        JOptionPane.showMessageDialog(frame, "Date of Birth must be in the format dd/mm/yyyy.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Insert data into the database
                    addResidentToDatabase(firstName, middleName, lastName, suffix, address, dob, contact, email);
                }
            }
        });
        btnAdd.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnAdd.setBounds(91, 612, 138, 45);
        welcomePanel.add(btnAdd);
     // Button for Update
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = res_tbl.getSelectedRow();  // Get selected row index

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a resident to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Retrieve the resident_id as Integer
                int residentId = (Integer) res_tbl.getValueAt(selectedRow, 0);
                String firstName = (String) res_tbl.getValueAt(selectedRow, 1);
                String middleName = (String) res_tbl.getValueAt(selectedRow, 2);
                String lastName = (String) res_tbl.getValueAt(selectedRow, 3);
                String suffix = (String) res_tbl.getValueAt(selectedRow, 4);
                String address = (String) res_tbl.getValueAt(selectedRow, 5);
                String dob = res_tbl.getValueAt(selectedRow, 6).toString();
                String contact = (String) res_tbl.getValueAt(selectedRow, 7);
                String email = (String) res_tbl.getValueAt(selectedRow, 8);

                // Show the data in a dialog for editing
                JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
                JTextField firstNameField = new JTextField(firstName);
                JTextField middleNameField = new JTextField(middleName);
                JTextField lastNameField = new JTextField(lastName);
                JTextField suffixField = new JTextField(suffix);
                JTextField addressField = new JTextField(address);
                JTextField dobField = new JTextField(dob);
                JTextField contactField = new JTextField(contact);
                JTextField emailField = new JTextField(email);

                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Middle Name (Optional):"));
                panel.add(middleNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Suffix (Optional):"));
                panel.add(suffixField);
                panel.add(new JLabel("Address:"));
                panel.add(addressField);
                panel.add(new JLabel("Date of Birth (dd/MM/yyyy):"));
                panel.add(dobField);
                panel.add(new JLabel("Contact Number (11 digits):"));
                panel.add(contactField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);

                int result = JOptionPane.showConfirmDialog(frame, panel, "Update Resident", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    // Get the updated values
                    String updatedFirstName = firstNameField.getText().trim();
                    String updatedMiddleName = middleNameField.getText().trim();
                    String updatedLastName = lastNameField.getText().trim();
                    String updatedSuffix = suffixField.getText().trim();
                    String updatedAddress = addressField.getText().trim();
                    String updatedDob = dobField.getText().trim();
                    String updatedContact = contactField.getText().trim();
                    String updatedEmail = emailField.getText().trim();

                    // Validate and update the resident in the database
                    try {
                        // Call the update method with updated values and residentId
                        updateResidentInDatabase(residentId, updatedFirstName, updatedMiddleName, updatedLastName, updatedSuffix,
                                                  updatedAddress, updatedDob, updatedContact, updatedEmail);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error updating resident: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnUpdate.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnUpdate.setBounds(283, 612, 138, 45);
        welcomePanel.add(btnUpdate);

        
        JButton btnDel = new JButton("Delete");
        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = res_tbl.getSelectedRow();

                // Check if a row is selected
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a resident to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int residentId = (int) res_tbl.getValueAt(selectedRow, 0);

                // Confirm the deletion with the user
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this resident?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Call the delete method
                    deleteResidentFromDatabase(residentId);
                }
            }
        });

        btnDel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnDel.setBounds(477, 612, 138, 45);
        welcomePanel.add(btnDel);

        
        JButton btnPrint = new JButton("Print ID");
        btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnPrint.setBounds(670, 612, 138, 45);
        welcomePanel.add(btnPrint);
    }
    
    private void filterTable(String searchQuery) {
        DefaultTableModel model = (DefaultTableModel) res_tbl.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        res_tbl.setRowSorter(sorter);

        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null); // Show all rows if the search query is empty
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery)); // Case-insensitive filtering
        }
    }


    private void deleteResidentFromDatabase(int residentId) {
        String query = "DELETE FROM ResidentDB WHERE resident_id = ?"; // Use resident_id as the unique identifier

        try (Connection connection = DriverManager.getConnection(DB_PATH);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter for the DELETE query
            preparedStatement.setInt(1, residentId);

            int rowsDeleted = preparedStatement.executeUpdate();  // Execute the DELETE query
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(frame, "Resident deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();  // Reload the table data after deletion
            } else {
                JOptionPane.showMessageDialog(frame, "No resident found to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting resident: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


		    
    private void updateResidentInDatabase(int residentId, String firstName, String middleName, String lastName, String suffix,
            String address, String dob, String contact, String email) {
        String query = "UPDATE ResidentDB SET resident_firstName = ?, resident_midName = ?, resident_lastName = ?, " +
                       "resident_suffix = ?, resident_address = ?, resident_DoB = ?, resident_contactNo = ?, resident_email = ? " +
                       "WHERE resident_id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_PATH);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Normalize the date string and convert it to java.sql.Date
            java.sql.Date sqlDate = normalizeDate(dob);

            // Set all parameters in the query
            preparedStatement.setString(1, firstName.trim());
            preparedStatement.setString(2, middleName.isEmpty() ? null : middleName.trim());
            preparedStatement.setString(3, lastName.trim());
            preparedStatement.setString(4, suffix.isEmpty() ? null : suffix.trim());
            preparedStatement.setString(5, address.trim());
            preparedStatement.setDate(6, sqlDate); 
            preparedStatement.setString(7, contact.trim());
            preparedStatement.setString(8, email.trim());
            preparedStatement.setInt(9, residentId);

            int rowsUpdated = preparedStatement.executeUpdate();  // Execute the update query
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "Resident updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadResidentData();  // Reload the data to reflect the changes in the table
            } else {
                JOptionPane.showMessageDialog(frame, "No resident found to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating resident: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Invalid Date format. Please use dd/MM/yyyy.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private java.sql.Date normalizeDate(String dob) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);  // Ensure strict parsing
        java.util.Date parsedDate = dateFormat.parse(dob);  // Parse the date string
        return new java.sql.Date(parsedDate.getTime());  // Convert to java.sql.Date
    }


    private void loadResidentData() {
    String query = "SELECT * FROM ResidentDB"; // Query to fetch all columns including resident_id

    try (Connection connection = DriverManager.getConnection(DB_PATH);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

        // Update table model with your specific columns
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };

        // Add columns
        model.addColumn("Resident ID");
        model.addColumn("First Name");
        model.addColumn("Middle Name");
        model.addColumn("Last Name");
        model.addColumn("Suffix");
        model.addColumn("Address");
        model.addColumn("Date of Birth");
        model.addColumn("Contact Number");
        model.addColumn("Email");

        while (resultSet.next()) {
        	model.addRow(new Object[]{
        		    resultSet.getInt("resident_id"),
        		    resultSet.getString("resident_firstName"),
        		    resultSet.getString("resident_midName"),
        		    resultSet.getString("resident_lastName"),
        		    resultSet.getString("resident_suffix"),
        		    resultSet.getString("resident_address"),
        		    new SimpleDateFormat("dd/MM/yyyy").format(resultSet.getDate("resident_DoB")), // Format the date
        		    resultSet.getString("resident_contactNo"),
        		    resultSet.getString("resident_email")
        		});

        }

        res_tbl.setModel(model); 
        res_tbl.getColumnModel().getColumn(0).setMinWidth(0);
        res_tbl.getColumnModel().getColumn(0).setMaxWidth(0);
        res_tbl.getColumnModel().getColumn(0).setWidth(0);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
