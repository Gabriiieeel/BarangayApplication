package system.BarrioSeguro;
/**
 * Acts as the central “controller” that decides
 * which form appears and manages overall workflow.
 */
public class BarrioSeguro {

    public void startApplication() {
        // The first form to appear is the login form:
        LoginForm loginForm = new LoginForm(this);
        loginForm.setVisible(true);
    }

    // Open the homepage after login
    public void openHomepageForm() {
        HomepageForm homepage = new HomepageForm(this);
        homepage.setVisible(true);
    }

    public void openCrimeForm() {
        CrimeForm crimeForm = new CrimeForm(this);
        crimeForm.setVisible(true);
    }

    public void openResidentForm() {
        ResidentForm residentForm = new ResidentForm(this);
        residentForm.setVisible(true);
    }

    public void openSummaryForm() {
        SummaryForm summaryForm = new SummaryForm(this);
        summaryForm.setVisible(true);
    }

    public void openAnnouncementForm() {
        AnnouncementForm announcementForm = new AnnouncementForm(this);
        announcementForm.setVisible(true);
    }

    // Optionally: central DB or utility access (if you prefer).
    // For example, you could unify repeated code or queries here.
}
