package system.BarrioSeguro; // This specifies the code belongs to the "BarrioSeguro" group 

import java.awt.Color; // This is used to add colors
import java.awt.Component; // For creating and managing different parts of the interface
import java.awt.Font; // This is used to add different text styles
import java.awt.Graphics; // This is used to draw things in the window
import java.awt.Graphics2D; // This is used for more advanced drawing tools
import java.awt.RenderingHints; // This helps make drawn things look better

import java.awt.event.ActionEvent; // This is used to detect button clicks
import java.awt.event.ActionListener; // This is used to handle button click events
import java.awt.event.FocusEvent; // This is used to detect when we click or leave a text box
import java.awt.event.FocusListener; // This is used to know when we interact with the text box
import java.awt.event.KeyAdapter; // This is used to handle keyboard events
import java.awt.event.KeyEvent; // This is used to detect key presses

import java.sql.Connection; // This is used to connect to the database
import java.sql.PreparedStatement; // This is used to run database commands
import java.sql.ResultSet; // This is used to store data from the database
import java.sql.Statement; // This is used to run simple database commands
import java.sql.SQLException; // This is used to handle database errors

import java.text.SimpleDateFormat; // This is used to format dates

import java.util.Date; // This is used to work with dates

import javax.swing.BorderFactory; // This is used to create borders around components
import javax.swing.JButton; // This is used to add buttons
import javax.swing.JLabel; // This is used to add text labels
import javax.swing.JLayeredPane; // This is used to layer components over each other
import javax.swing.JOptionPane; // This is used to show messages to the user
import javax.swing.JPanel; // This will hold different parts of the layout
import javax.swing.JScrollPane; // This is used to make scrollable components
import javax.swing.JTable; // This is used to display tables
import javax.swing.JTextField; // This is used to add text boxes
import javax.swing.RowFilter; // This is used to filter table rows based on conditions
import javax.swing.SwingConstants; // This is used to align text

import javax.swing.border.EmptyBorder; // This adds space around the text fields
import javax.swing.table.DefaultTableCellRenderer; // This is used to control how table cells look
import javax.swing.table.DefaultTableModel; // This is used to manage table data
import javax.swing.table.TableRowSorter; // This is used to sort and filter table rows

public class SummaryForm extends BaseForm { // This line defines our main Summary form

    private JTable incidentTable; // Table to display incident records
    private JTextField searchTextField; // Text field to search in the table

    public SummaryForm(BarrioSeguro appController) { // Constructor to set up the form
        super(appController); // Calling the parent class constructor
        setTitle("BarrioSeguro - Summary Reports"); // Setting the window title
        initialize(); // Calling the method to set up everything
        loadIncidentData(); // Loading incident data from the database
    }

    // Method to set up the form layout and components
    private void initialize() {
        JLayeredPane summaryPane = new JLayeredPane(); // Creating a layer to add components
        setContentPane(summaryPane); // Making summaryPane the main content layer

        addBackgroundImage(summaryPane); // Adding background image to the layer
        addDashboardPanel(summaryPane); // Adding dashboard panel to the layer
        addSummaryPanel(summaryPane); // Adding the panel to display summary details
    }

    // Method to add the panel for summary details
    private void addSummaryPanel(JLayeredPane summaryPane) {
        JPanel summaryPanel = new JPanel() { // Creating a new panel for the form
            @Override
            protected void paintComponent(Graphics paintGraphics) { // Customizing how the panel looks
                super.paintComponent(paintGraphics); // Calling the default painting method
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics; // Using advanced graphics
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Making it look smooth
                paintGraphicsWith2D.setColor(getBackground()); // Setting background color
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75); // Drawing a rounded rectangle
            }
        };
        summaryPanel.setBounds(480, 185, 895, 722); // Setting the position and size of the panel
        summaryPanel.setBackground(new Color(102, 77, 77, 178)); // Setting background color with transparency
        summaryPanel.setLayout(null); // Using absolute positioning
        summaryPanel.setOpaque(false); // Making the panel background transparent

        summaryPane.add(summaryPanel, JLayeredPane.PALETTE_LAYER); // Adding panel to the main layer

        incidentTable = new JTable() { // Creating a new table for displaying data
            @Override
            public boolean isCellEditable(int row, int column) { // Making table cells not editable editable
                return false;
            }
        };
        incidentTable.setBounds(36, 38, 826, 581); // Setting the position and size of the table
        summaryPanel.add(incidentTable); // Adding the table to the panel

        String[] columnNames = { // Defining column names for the table
            "First Name", "Last Name", "Date", "Progress"
        };

        DefaultTableModel summaryTableModel = new DefaultTableModel(null, columnNames); // Creating a table model with column names

        incidentTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() { // Customizing table header
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel headerLabel = new JLabel(value.toString()); // Creating a label for header cell
                headerLabel.setOpaque(true); // Making the label background show
                headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Setting font for header text
                headerLabel.setBackground(new Color(255, 104, 101)); // Setting background color of header cell
                headerLabel.setForeground(Color.BLACK); // Setting text color of header cell
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Aligning header text to center
                headerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Adding border around header cell
                return headerLabel;
            }
        });

        incidentTable.setModel(summaryTableModel); // Assigning the table model to the table

        JScrollPane scrollSummaryTable = new JScrollPane(incidentTable); // Making the table scrollable
        scrollSummaryTable.setBounds(36, 80, 826, 561); // Setting the position and size of the scroll pane
        summaryPanel.add(scrollSummaryTable); // Adding the scroll pane to the panel

        // Setting the preferred width for each table column
        incidentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        // Setting up the text field for searching in the table
        searchTextField = createRoundedTextField("Search", 25); // Custom method to create a text field
        searchTextField.setToolTipText(""); // No tooltip text
        searchTextField.setHorizontalAlignment(SwingConstants.LEFT); // Aligning text to the left
        searchTextField.setForeground(Color.LIGHT_GRAY); // Initial text color
        searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Text font and size
        searchTextField.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around text field
        searchTextField.setBounds(36, 34, 216, 37); // Position and size of the text field
        
        // Adding a focus listener to handle focus events on the text field
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForSearchField) { // When the text field is clicked
                if (searchTextField.getText().equals("Search")) { 
                    searchTextField.setText(""); // Clear the default text
                    searchTextField.setForeground(Color.BLACK); // Set text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent eventForSearchField) { // When the text field loses focus
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search"); // Reset to default text
                    searchTextField.setForeground(Color.LIGHT_GRAY); // Set text color to gray
                }
            }
        });

        // Adding a key listener to handle key events on the text field
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent eventKeyForSearchField) { // When a key is released in the text field
                String searchQuery = searchTextField.getText().trim().toLowerCase(); // Get the text from the field and convert to lowercase
                filterTable(searchQuery); // Filter the table rows based on the text
            }
        });
        summaryPanel.add(searchTextField); // Add the text field to the panel

        // Setting up the button to view selected row details
        JButton viewBtn = new JButton("VIEW");
        styleRoundedButton(viewBtn); // Custom method to style the button
        viewBtn.addActionListener(new ActionListener() { // Adding an action listener for the button
            public void actionPerformed(ActionEvent eventForViewBtn) { // When the button is clicked
                int selectedRow = incidentTable.getSelectedRow(); // Get the selected row in the table

                if (selectedRow == -1) { // If no row is selected
                    JOptionPane.showMessageDialog(null, "Please select an incident row to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return; // Show a message and return
                }

                if (selectedRow != -1) { // If a row is selected
                    // Get details from the selected row
                    String firstName = (String) incidentTable.getValueAt(selectedRow, 0);
                    String lastName = (String) incidentTable.getValueAt(selectedRow, 1);
                    String dateString = (String) incidentTable.getValueAt(selectedRow, 2); 
                    String progress = (String) incidentTable.getValueAt(selectedRow, 3);

                    SimpleDateFormat formatDateConverter = new SimpleDateFormat("dd/MM/yyyy"); // Date format for conversion
                    java.sql.Date giveDate = null;
                    try {
                        java.util.Date convertedDate = formatDateConverter.parse(dateString);
                        giveDate = new java.sql.Date(convertedDate.getTime()); // Convert text to date
                    } catch (Exception handleDateException) {
                        JOptionPane.showMessageDialog(null, "Invalid date format."); // Show error if date is wrong
                        return;
                    }

                    String description = ""; // Initialize description variable
                    String typeOfIncident = ""; // Initialize incident type variable

                    try (Connection connectSummary = getConnection()) { // Establish database connection
                        // SQL query to get the incident description and type
                        String query = "SELECT incident_description, incident_type FROM IncidentDB " +
                                        "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        // Fill the SQL query with row details
                        try (PreparedStatement statementSummary = connectSummary.prepareStatement(query)) {
                            statementSummary.setString(1, firstName);
                            statementSummary.setString(2, lastName);
                            statementSummary.setDate(3, giveDate);
                            ResultSet resultSummary = statementSummary.executeQuery(); // Execute the query

                            if (resultSummary.next()) { // If result found
                                description = resultSummary.getString("incident_description"); // Get the description
                                typeOfIncident = resultSummary.getString("incident_type"); // Get the type of incident
                            } else {
                                JOptionPane.showMessageDialog(null, "No matching record found."); // Show error if no result
                            }
                        }
                    } catch (SQLException handleViewDatabaseException) {
                        handleViewDatabaseException.printStackTrace(); // Print any database errors
                    }

                    IncidentForm createIncidentForm = new IncidentForm(appController); // Create the incident form

                    // Fill the incident form with data from the selected row
                    createIncidentForm.fillData(firstName, "", lastName, "", dateString, progress, description, typeOfIncident);

                    createIncidentForm.setVisible(true); // Show the incident form

                    dispose(); // Close the summary form
                }
            }
        });
        viewBtn.setFont(new Font("Times New Roman", Font.BOLD, 14)); // Setting the font for the button
        viewBtn.setBounds(361, 664, 160, 37); // Setting the position and size of the button
        summaryPanel.add(viewBtn); // Adding the button to the panel
    }
    
    // Method to filter the table rows based on the search query
    private void filterTable(String searchQuery) { // function for search
        DefaultTableModel filterTableModel = (DefaultTableModel) incidentTable.getModel(); // Get the table model
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(filterTableModel); // Create a row sorter
        incidentTable.setRowSorter(sorter); // Set the sorter for the table

        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null); // If query is empty, show all rows
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery)); // Filter rows based on the query
        }
    }
    
    // Method to load incident data from the database and display it in the table
    private void loadIncidentData() {
        String query = "SELECT incident_firstName, incident_lastName, incident_date, incident_progress " +
                        "FROM IncidentDB"; // SQL query to get incident data
        
        try (Connection connectLoadIncident = getConnection()) { // Establish database connection
            Statement statementLoadIncident = connectLoadIncident.createStatement(); // Create a statement
            ResultSet resultLoacIncident = statementLoadIncident.executeQuery(query); // Execute the query

            DefaultTableModel loadTableModel = (DefaultTableModel) incidentTable.getModel(); // Get the table model
            SimpleDateFormat formatDateConverter = new SimpleDateFormat("dd/MM/yyyy"); // Date format for conversion

            while (resultLoacIncident.next()) { // Iterate over the results
                // Get data from the result set
                String firstName = resultLoacIncident.getString("incident_firstName");
                String lastName = resultLoacIncident.getString("incident_lastName");
                Date incidentDate = resultLoacIncident.getDate("incident_date");
                String progress = resultLoacIncident.getString("incident_progress");

                String formattedDate = formatDateConverter.format(incidentDate); // Format the date

                // Add a row to the table with the data
                loadTableModel.addRow(new Object[]{firstName, lastName, formattedDate, progress});
            }
        } catch (SQLException handleDatabaseException) {
            handleDatabaseException.printStackTrace(); // Print any database errors
        }
    }
}