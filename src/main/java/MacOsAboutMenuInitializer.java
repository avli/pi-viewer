import java.awt.*;

public class MacOsAboutMenuInitializer {
    public static void initalizeAboutMenu() {
        // See: https://alvinalexander.com/source-code/java-macos-about-preferences-quit-handlers-desktop-awt/
        Desktop desktop = Desktop.getDesktop();
        desktop.setAboutHandler(aboutEvent -> {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.setVisible(true);
        });
    }
}
