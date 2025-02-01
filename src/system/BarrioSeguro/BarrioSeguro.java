// This line says we put this code in a "package" named "system.BarrioSeguro"  
// Think of a package like a folder that keeps our code nice and organized
package system.BarrioSeguro;

// We declare a public class named "BarrioSeguro"  
// A class is like a special box where we store related instructions
public class BarrioSeguro {

    // This is the constructor for the "BarrioSeguro" class  
    // A constructor is a small intro that sets up our new "BarrioSeguro" object
    public BarrioSeguro() {
        
    }

    // This method is named "startApplication" and is marked 'protected'  
    // It does the job of launching the program's main screen so we can start using it
    protected void startApplication() {
        // We create a "LoginForm" and pass "this" (the current class) to it  
        // "LoginForm" is like the sign-in window that appears when the app starts
        LoginForm loginScreen = new LoginForm(BarrioSeguro.this);

        // We make the login screen visible so people can see and use it  
        // 'setVisible(true)' just means: "Show this window on the screen now"
        loginScreen.setVisible(true);
    }

    // This method is named "openHomepageForm"  
    // It's responsible for opening the homepage window after we log in
    protected void openHomepageForm() {
        // Creating a "HomepageForm" and passing our current "BarrioSeguro" to it  
        // "HomepageForm" is the primary page people see after they sign in
        HomepageForm homepageScreen = new HomepageForm(BarrioSeguro.this);

        // Show the homepage window so people can use it
        homepageScreen.setVisible(true);
    }

    // This method is called "openAnnouncementForm"  
    // It displays a place for neighborhood announcements or messages
    protected void openAnnouncementForm() {
        // "AnnouncementForm" is a separate window for posting news or updates  
        AnnouncementForm announcementBoardScreen = new AnnouncementForm(BarrioSeguro.this);

        // Show the announcement window to share information
        announcementBoardScreen.setVisible(true);
    }

    // This method is called "openResidentForm"  
    // It opens a page for managing people’s information, like residents’ names
    protected void openResidentForm() {
        // "ResidentForm" is a window where we keep track of all residents  
        ResidentForm residentManagementScreen = new ResidentForm(BarrioSeguro.this);

        // Display the resident management window on the screen
        residentManagementScreen.setVisible(true);
    }

    // This method is named "openIncidentForm"  
    // It’s for reporting events or problems happening in our area
    protected void openIncidentForm() {
        // "IncidentForm" is a window to record details of things that went wrong  
        IncidentForm incidentReportScreen = new IncidentForm(BarrioSeguro.this);

        // Show the incident report window so people can file reports
        incidentReportScreen.setVisible(true);
    }

    // This method is called "openSummaryForm"  
    // It shows a summary of all incidents or other data
    protected void openSummaryForm() {
        // "SummaryForm" is where we gather and display an overview of information  
        SummaryForm incidentSummaryScreen = new SummaryForm(BarrioSeguro.this);

        // Show the summary window so we can view a quick recap
        incidentSummaryScreen.setVisible(true);
    }
}
