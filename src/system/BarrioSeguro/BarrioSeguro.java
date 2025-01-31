package system.BarrioSeguro;

public class BarrioSeguro {

    public BarrioSeguro() {}

    protected void startApplication() {
        LoginForm loginScreen = new LoginForm(BarrioSeguro.this);
        loginScreen.setVisible(true);
    }

    protected void openHomepageForm() {
        HomepageForm homepageScreen = new HomepageForm(BarrioSeguro.this);
        homepageScreen.setVisible(true);
    }

    protected void openAnnouncementForm() {
        AnnouncementForm announcementBoardScreen = new AnnouncementForm(BarrioSeguro.this);
        announcementBoardScreen.setVisible(true);
    }

    protected void openResidentForm() {
        ResidentForm residentManagementScreen = new ResidentForm(BarrioSeguro.this);
        residentManagementScreen.setVisible(true);
    }

    protected void openIncidentForm() {
        IncidentForm incidentReportScreen = new IncidentForm(BarrioSeguro.this);
        incidentReportScreen.setVisible(true);
    }

    protected void openSummaryForm() {
        SummaryForm incidentSummaryScreen = new SummaryForm(BarrioSeguro.this);
        incidentSummaryScreen.setVisible(true);
    }
}
