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
import java.sql.Statement;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Date;

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

public class SummaryForm extends BaseForm {

    private JTable incidentTable;
    private JTextField searchTextField;

    public SummaryForm(BarrioSeguro appController) {
        super(appController);
        setTitle("BarrioSeguro - Summary Reports");
        initialize();
        loadIncidentData();
    }

    private void initialize() {
        JLayeredPane summaryPane = new JLayeredPane();
        setContentPane(summaryPane);

        addBackgroundImage(summaryPane);
        addDashboardPanel(summaryPane);
        addSummaryPanel(summaryPane);
    }

    private void addSummaryPanel(JLayeredPane summaryPane) {
        JPanel summaryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphicsWith2D.setColor(getBackground());
                paintGraphicsWith2D.fillRoundRect(0, 0, getWidth(), getHeight(), 75, 75);
            }
        };
        summaryPanel.setBounds(480, 185, 895, 722);
        summaryPanel.setBackground(new Color(102, 77, 77, 178));
        summaryPanel.setLayout(null);
        summaryPanel.setOpaque(false);

        summaryPane.add(summaryPanel, JLayeredPane.PALETTE_LAYER);

        incidentTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        incidentTable.setBounds(36, 38, 826, 581);
        summaryPanel.add(incidentTable);

        String[] columnNames = {
            "First Name", "Last Name", "Date", "Progress"
        };

        DefaultTableModel summaryTableModel = new DefaultTableModel(null, columnNames);
        incidentTable.setModel(summaryTableModel);

        JScrollPane scrollSummaryTable = new JScrollPane(incidentTable);
        scrollSummaryTable.setBounds(36, 80, 826, 561);
        summaryPanel.add(scrollSummaryTable);

        incidentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        incidentTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        searchTextField = new JTextField("Search"); 
        searchTextField.setToolTipText("");
        searchTextField.setHorizontalAlignment(SwingConstants.LEFT);
        searchTextField.setForeground(Color.LIGHT_GRAY);
        searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchTextField.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchTextField.setBounds(36, 34, 216, 37);
        
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent eventForSearchField) {
                if (searchTextField.getText().equals("Search")) { 
                    searchTextField.setText(""); 
                    searchTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent eventForSearchField) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search"); 
                    searchTextField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent eventKeyForSearchField) {
                String searchQuery = searchTextField.getText().trim().toLowerCase();
                filterTable(searchQuery);
            }
        });
        summaryPanel.add(searchTextField);
        
        JButton viewBtn = new JButton("VIEW");
        styleRoundedButton(viewBtn);
        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForViewBtn) {
                int selectedRow = incidentTable.getSelectedRow();
                if (selectedRow != -1) {
                    String firstName = (String) incidentTable.getValueAt(selectedRow, 0);
                    String lastName = (String) incidentTable.getValueAt(selectedRow, 1);
                    String dateString = (String) incidentTable.getValueAt(selectedRow, 2); 
                    String progress = (String) incidentTable.getValueAt(selectedRow, 3);

                    SimpleDateFormat formatDateConverter = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date giveDate = null;
                    try {
                        java.util.Date convertedDate = formatDateConverter.parse(dateString);
                        giveDate = new java.sql.Date(convertedDate.getTime());
                    } catch (Exception handleDateException) {
                        JOptionPane.showMessageDialog(null, "Invalid date format.");
                        return;
                    }

                    String description = "";
                    String typeOfIncident = "";

                    try (Connection connectSummary = getConnection()) {
                        String query = "SELECT incident_description, incident_type FROM IncidentDB " +
                                        "WHERE incident_firstName = ? AND incident_lastName = ? AND incident_date = ?";

                        try (PreparedStatement statementSummary = connectSummary.prepareStatement(query)) {
                            statementSummary.setString(1, firstName);
                            statementSummary.setString(2, lastName);
                            statementSummary.setDate(3, giveDate);
                            ResultSet resultSummary = statementSummary.executeQuery();

                            if (resultSummary.next()) {
                                description = resultSummary.getString("incident_description");
                                typeOfIncident = resultSummary.getString("incident_type");
                            } else {
                                JOptionPane.showMessageDialog(null, "No matching record found.");
                            }
                        }
                    } catch (SQLException handleViewDatabaseException) {
                        handleViewDatabaseException.printStackTrace();
                    }

                    IncidentForm createIncidentForm = new IncidentForm(appController);

                    createIncidentForm.fillData(firstName, "", lastName, "", dateString, progress, description, typeOfIncident);

                    createIncidentForm.setVisible(true);

                    dispose();
                }
            }
        });
        viewBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        viewBtn.setBounds(326, 664, 160, 37);
        summaryPanel.add(viewBtn);
    }
    
    private void filterTable(String searchQuery) {
        DefaultTableModel filterTableModel = (DefaultTableModel) incidentTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(filterTableModel);
        incidentTable.setRowSorter(sorter);

        if (searchQuery.isEmpty() || searchQuery.equalsIgnoreCase("search")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery));
        }
    }
    
    private void loadIncidentData() {
        String query = "SELECT incident_firstName, incident_lastName, incident_date, incident_progress " +
                        "FROM IncidentDB";
        
        try (Connection connectLoadIncident = getConnection()) {
            Statement statementLoadIncident = connectLoadIncident.createStatement();
            ResultSet resultLoacIncident = statementLoadIncident.executeQuery(query);

            DefaultTableModel loadTableModel = (DefaultTableModel) incidentTable.getModel();
            SimpleDateFormat formatDateConverter = new SimpleDateFormat("dd/MM/yyyy");

            while (resultLoacIncident.next()) {
                String firstName = resultLoacIncident.getString("incident_firstName");
                String lastName = resultLoacIncident.getString("incident_lastName");
                Date incidentDate = resultLoacIncident.getDate("incident_date");
                String progress = resultLoacIncident.getString("incident_progress");

                String formattedDate = formatDateConverter.format(incidentDate);

                loadTableModel.addRow(new Object[]{firstName, lastName, formattedDate, progress});
            }
        } catch (SQLException handleDatabaseException) {
            handleDatabaseException.printStackTrace();
        }
    }
}