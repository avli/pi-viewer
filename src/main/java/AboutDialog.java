import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    private final static int WIDTH = 150;
    private final static int HEIGHT = 150;

    public AboutDialog() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        final GridBagConstraints nameConstrains = new GridBagConstraints();
        nameConstrains.gridx = 0;
        nameConstrains.gridy = 0;
        nameConstrains.ipady = 40;
        nameConstrains.gridwidth = 2;

        JLabel appNameLabel = new JLabel(PiViewer.NAME);
        Font font = appNameLabel.getFont();
        appNameLabel.setFont(new Font(font.getName(), Font.BOLD, 16));
        add(appNameLabel, nameConstrains);

        final GridBagConstraints versionConstrains = new GridBagConstraints();

        versionConstrains.gridx = 0;
        versionConstrains.gridy = 1;

        JLabel appVersionLabel = new JLabel(PiViewer.getVersion());
        appVersionLabel.setFont(new Font(appVersionLabel.getFont().getName(), Font.PLAIN, 12));
        add(appVersionLabel, versionConstrains);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 250, 0, 0);
        add(new JButton("Button 1"), c);


//        setSize(WIDTH, HEIGHT);
        pack();
        setResizable(false);
    }
}
