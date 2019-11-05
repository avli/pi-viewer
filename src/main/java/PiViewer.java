import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import com.apple.eawt.*;

public class PiViewer {
    private static int DEFAULT_WIDTH = 320;
    private static int DEFAULT_HEIGHT = 320;
    private static String PI_FILE_NAME = "pi1000000.txt";

    private final JFrame myFrame = new JFrame("PiViewer");
    private final ImagePanel myImagePanel = new ImagePanel();
    private final JPanel myBottomPanel = new JPanel();
    private final JLabel myStartFromDigitLabel = new JLabel("Start from digit: ");
    private final JTextField myStartFromDigitTextField = new JTextField(10);
    private JSpinner mySpinner;
    private final JButton myShowImageButton = new JButton("Show image");
    private final JMenuBar myMenuBar = new JMenuBar();
    private final JMenu myHelpMenu = new JMenu("Help");
//    private final JPanel myStatusPanel = new JPanel();
    private StatusLabel<ImagePanel> myStatus;

    private final PiReader piReader;

    public PiViewer() {
        piReader = new PiReader(getClass().getResource(PI_FILE_NAME));
        piReader.loadPi();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Image panel
        myImagePanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        // Bottom panel
        myBottomPanel.setLayout(new FlowLayout());
        myBottomPanel.add(myStartFromDigitLabel);
        myStartFromDigitTextField.setHorizontalAlignment(JTextField.RIGHT);
        myBottomPanel.add(myStartFromDigitTextField);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 100, 1);
        mySpinner = new JSpinner(model);
        mySpinner.setEditor(new JSpinner.NumberEditor(mySpinner));
        JFormattedTextField txt = ((JSpinner.NumberEditor) mySpinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

        myBottomPanel.add(mySpinner);
        mySpinner.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n')
                    redrawImage();
            }
        });

        myShowImageButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redrawImage();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        myStartFromDigitTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') redrawImage();
            }
        });

        myBottomPanel.add(myShowImageButton);

        // Add panels to frame
        myFrame.add(new JPanel(), BorderLayout.LINE_START);
        myFrame.add(myImagePanel, BorderLayout.CENTER);
        myFrame.add(new JPanel(), BorderLayout.LINE_END);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);

        // Add menu bar
        myMenuBar.add(Box.createHorizontalGlue());
        myMenuBar.add(myHelpMenu);
        myMenuBar.setAlignmentX(JMenuBar.RIGHT_ALIGNMENT);
        JMenuItem aboutMenuItem = new JMenuItem("About");
        myHelpMenu.add(aboutMenuItem);
        myFrame.setJMenuBar(myMenuBar);

        // Add status bar
        myStatus = new StatusLabel<ImagePanel>(myImagePanel);
        myImagePanel.attach(myStatus);
        myFrame.add(myStatus, BorderLayout.NORTH);

        // Pack frame and show
        myFrame.pack();
//        myFrame.setMaximumSize(myFrame.getSize());
        myFrame.setVisible(true);
    }

    private void redrawImage() {
        int offset = Integer.parseInt(myStartFromDigitTextField.getText());
        int[] data = piReader.getData(offset, DEFAULT_WIDTH * DEFAULT_HEIGHT);
        BufferedImage bufferedImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < DEFAULT_HEIGHT; i++)
            for (int j = 0; j < DEFAULT_WIDTH; j++)
                bufferedImage.setRGB(i, j, data[i * j]);
        Graphics g = myImagePanel.getGraphics();
        Image img = bufferedImage.getScaledInstance(myImagePanel.getWidth(), myImagePanel.getHeight(),
                Image.SCALE_FAST);
        g.drawImage(img, 0, 0, null);
        myImagePanel.setImage(img);
    }

    public static void main(String[] args) {
        Application macApplication = Application.getApplication();
        macApplication.setAboutHandler(new AboutHandler() {
            @Override
            public void handleAbout(AppEvent.AboutEvent aboutEvent) {
                JDialog aboutDialog = new JDialog();
                aboutDialog.setTitle("About");
                aboutDialog.add(new JLabel("PiViewer"), BorderLayout.CENTER);
                aboutDialog.setSize(150, 150);
                aboutDialog.setResizable(false);
                aboutDialog.setVisible(true);
            }
        });
        SwingUtilities.invokeLater(PiViewer::new);
    }
}
