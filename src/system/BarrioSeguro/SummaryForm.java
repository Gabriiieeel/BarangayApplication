package system.BarrioSeguro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Displays summary of incidents or other overall reports.
 * Inherits from BaseForm to reduce repeated code.
 */
public class SummaryForm extends BaseForm {

    private JTable incident_table;
    private JTextField searchtf;

    public SummaryForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Summary Reports");
        initialize();
        loadIncidentData();
    }

    private void initialize() {
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

         addBackgroundImage(layeredPane);

         addDashboardPanel(layeredPane);

         addWelcomePanel(layeredPane);
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
                      JOptionPane.showMessageDialog(null, "Invalid date format.");
                      return;
                  }

                  // Now, fetch all the data from the database to pass to CrimeForm
                  String description = "";
                  String typeOfIncident = "";

                  try (Connection conn = getConnection()) {
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
                              JOptionPane.showMessageDialog(null, "No matching record found.");
                          }
                      }
                  } catch (SQLException ex) {
                      ex.printStackTrace();
                  }

                  // Create an instance of CrimeForm
                  CrimeForm crimeForm = new CrimeForm(appController);

                  // Pass the data to CrimeForm
                  crimeForm.fillData(firstName, "", lastName, "", dateString, progress, description, typeOfIncident);

                  // Show CrimeForm
                  crimeForm.setVisible(true);

                  // Close the current SummaryForm
                  dispose();
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
      
      try (Connection conn = getConnection()) {
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
}