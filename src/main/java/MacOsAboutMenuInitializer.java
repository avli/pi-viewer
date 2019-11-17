import com.apple.eawt.*;

public class MacOsAboutMenuInitializer {
    public static void initalizeAboutMenu() {
        Application macApplication = Application.getApplication();
        macApplication.setAboutHandler(new AboutHandler() {
            @Override
            public void handleAbout(AppEvent.AboutEvent aboutEvent) {
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.setVisible(true);
            }
        });
    }
}
