package system.BarrioSeguro;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;

import javax.swing.plaf.basic.BasicButtonUI;

public abstract class BaseForm extends JFrame {

    protected static final String DATABASE_PATH = "jdbc:ucanaccess://Database/BarrioSeguroDB.accdb";

    protected BarrioSeguro appController;

    protected final Color DEFAULT_COLOR = new Color(220, 20, 60);
    protected final Color CLICK_COLOR = new Color(180, 0, 40);
    protected final Color HOVER_COLOR = new Color(200, 0, 50);

    public BaseForm(BarrioSeguro accessAppControl) {
        appController = accessAppControl;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1440, 1024);
        setResizable(false);
        setLocationRelativeTo(null);
        
        setApplicationIcon();
    }

    private void setApplicationIcon() {
        File iconFile = new File("Visuals/logoIcon.png");
        if (iconFile.exists()) {
            ImageIcon appIcon = new ImageIcon(iconFile.getAbsolutePath());
            setIconImage(appIcon.getImage());
        } else {
            System.err.println("Error: Window icon not found at " + iconFile.getAbsolutePath());
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_PATH);
    }

    protected void addBackgroundImage(JLayeredPane mainPane) {
        ImageIcon backgroundImage = new ImageIcon("Visuals/backgroundImage.png");
        JLabel backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics paintGraphics) {
                super.paintComponent(paintGraphics);
                Graphics2D paintGraphicsWith2D = (Graphics2D) paintGraphics;
                paintGraphicsWith2D.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                paintGraphicsWith2D.setColor(new Color(0, 0, 0, 160));
                paintGraphicsWith2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        backgroundLabel.setBounds(0, 0, 1440, 1024);
        mainPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
    }

    protected void addDashboardPanel(JLayeredPane mainPane) {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(new Color(102, 77, 77, 178));
        dashboardPanel.setBounds(0, 0, 430, 1024);
        mainPane.add(dashboardPanel, JLayeredPane.PALETTE_LAYER);

        addLogoToDashboard(dashboardPanel);
        addTextToDashboard(dashboardPanel);
        addDashboardButtons(dashboardPanel);
    }

    private void addLogoToDashboard(JPanel dashboardPanel) {
        ImageIcon logoImage = new ImageIcon("Visuals/logoIcon.png");
        JLabel logoLabel = new JLabel(logoImage);
        logoLabel.setBounds(18, 33, 150, 150); 
        dashboardPanel.setLayout(null);
        dashboardPanel.add(logoLabel);
    }

    private void addTextToDashboard(JPanel dashboardPanel) {
        JLabel logonamelabel = new JLabel("BarrioSeguro");
        logonamelabel.setForeground(Color.WHITE);
        logonamelabel.setFont(new Font("Times New Roman", Font.BOLD, 39)); 
        logonamelabel.setBounds(178, 69, 237, 78);
        dashboardPanel.add(logonamelabel);
    }

    private void addDashboardButtons(JPanel dashboardPanel) {
        JButton announceBtn = new JButton("Announcement");
        styleButton(announceBtn);
        announceBtn.setBounds(0, 250, 430, 78);
        announceBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForAnnounceBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openAnnouncementForm();
                        } catch (Exception handleAnnounceException) {
                            handleAnnounceException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(announceBtn);


        JButton residentBtn = new JButton("Resident Database");
        styleButton(residentBtn);
        residentBtn.setBounds(0, 371, 430, 78);
        residentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForResidentBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openResidentForm();
                        } catch (Exception handleResidentException) {
                            handleResidentException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(residentBtn);


        JButton incidentBtn = new JButton("Incident Reports");
        styleButton(incidentBtn);
        incidentBtn.setBounds(0, 489, 430, 78);
        incidentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForIncidentBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openIncidentForm();
                        } catch (Exception handleIncidentException) {
                            handleIncidentException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(incidentBtn);


        JButton summaryBtn = new JButton("Summary Reports");
        styleButton(summaryBtn);
        summaryBtn.setBounds(0, 611, 430, 78);
        summaryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSummaryBtn) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            appController.openSummaryForm();
                        } catch (Exception handleSummaryException) {
                            handleSummaryException.printStackTrace();
                        }
                        dispose();
                    }
                });
            }
        });
        dashboardPanel.add(summaryBtn);


        JButton logoutBtn = new JButton("Log Out");
        styleRoundedButton(logoutBtn);
        logoutBtn.setBounds(135, 805, 150, 55);
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent eventForSummaryBtn) {
                dispose();
                
                EventQueue.invokeLater(() -> {
                    logoutAndGoToLogin();
                });
            }
        });
        dashboardPanel.add(logoutBtn);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setBackground(DEFAULT_COLOR);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent eventButtonPress) {
                button.setBackground(CLICK_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent eventButtonRelease) {
                button.setBackground(DEFAULT_COLOR);
            }

            @Override
            public void mouseEntered(MouseEvent eventButtonEnter) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent eventButtonExit) {
                button.setBackground(DEFAULT_COLOR);
            }
        });
    }

    protected void styleRoundedButton(JButton button) {
        styleButton(button);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent eventButtonPress) {
                button.setBackground(CLICK_COLOR);
            }
    
            @Override
            public void mouseReleased(MouseEvent eventButtonRelease) {
                button.setBackground(DEFAULT_COLOR);
            }
        });
    
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics paintGraphics, JComponent paintComponent) {
                Graphics2D paintGraphics2D = (Graphics2D) paintGraphics.create();
                paintGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintGraphics2D.setColor(button.getBackground());
                paintGraphics2D.fillRoundRect(0, 0, paintComponent.getWidth(), paintComponent.getHeight(), 30, 30);
                paintGraphics2D.dispose();
                super.paint(paintGraphics, paintComponent);
            }
        });
    }

    private void logoutAndGoToLogin() {
        dispose();
        LoginForm login = new LoginForm(appController);
        login.setVisible(true);
    }

    protected JTextField createRoundedTextField(String text, int cornerRadius) {
        JTextField textField = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); // Background color
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY); // Border color
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
                g2.dispose();
            }
        };
        textField.setOpaque(false);
        textField.setBorder(new EmptyBorder(10, 20, 10, 20));
        return textField;
    }

    public JTextArea createRoundedTextArea(String placeholder, int width, int height) {
        JTextArea textArea = new JTextArea(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded rectangle
                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        textArea.setOpaque(false);
        textArea.setBackground(new Color(255, 244, 244));
        textArea.setForeground(new Color(0,0,0,50));
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        textArea.setBounds(51, 136, width, height);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(20, 20, 20, 20));

        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        return textArea;
    }

}
