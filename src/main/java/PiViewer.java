import pireader.PiFromDiskReader;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PiViewer {
    public static final String NAME = "PiViewer";
    private static final int VERSION_MAJOR = 0;
    private static final int VERSION_MINOR = 1;
    private static final int VERSION_PATCH = 0;

    private static final String PI_FILE_NAME = "pi1000000.txt";

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
    private final ImageSizePanel myImageSizePanel = new ImageSizePanel();

//    private final pireader.PiInMemoryReader piReader;
    private final PiFromDiskReader piReader;

    public PiViewer() {
//        piReader = new pireader.PiInMemoryReader(getClass().getResource(PI_FILE_NAME));
        piReader = new PiFromDiskReader(getClass().getResource(PI_FILE_NAME));
        createAndShowGUI();
    }

    public static String getVersion() {
        StringBuilder sb = new StringBuilder();
        final java.util.List<Integer> versionList = Arrays.asList(VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);
        for(Integer i : versionList) {
            sb.append(i);
            sb.append('.');
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    private void createAndShowGUI() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Image panel
        myImagePanel.setPreferredSize(new Dimension(320, 320));

        // Bottom panel
        myBottomPanel.setLayout(new GridBagLayout());
        myBottomPanel.add(myStartFromDigitLabel);
        myStartFromDigitTextField.setHorizontalAlignment(JTextField.RIGHT);
        myStartFromDigitTextField.setText("0");
        myBottomPanel.add(myStartFromDigitTextField);

        SpinnerModel model = new SpinnerNumberModel(0, 0, 100, 1);
        mySpinner = new JSpinner(model);
        mySpinner.setEditor(new JSpinner.NumberEditor(mySpinner));
        JFormattedTextField txt = ((JSpinner.NumberEditor) mySpinner.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.BOTH;

//        myBottomPanel.add(mySpinner, c);
        myBottomPanel.add(myImageSizePanel, c);
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

        myShowImageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redrawImage();
            }
        });

        myStartFromDigitTextField.addKeyListener(new KeyAdapter() {
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
        if (!isMacOS()) {
            JMenuItem aboutMenuItem = new JMenuItem("About");
            myHelpMenu.add(aboutMenuItem);
        } else {
            if (System.getProperty("os.name").equals("Mac OS X")) {
                try {
                    Method m = Class.forName("MacOsAboutMenuInitializer").getMethod("initializeAboutMenu");
                    m.invoke(null);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException
                        | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isMacOS()) {
            Desktop.getDesktop().setDefaultMenuBar(myMenuBar);
        } else {
            myFrame.setJMenuBar(myMenuBar);
        }

        // Add status bar
        myStatus = new StatusLabel<>(myImagePanel);
        myImagePanel.attach(myStatus);
        myFrame.add(myStatus, BorderLayout.NORTH);

        // Pack frame and show
        myFrame.pack();
//        myFrame.setMaximumSize(myFrame.getSize());
        myFrame.setVisible(true);
    }

    private void redrawImage() {
        int offset;
        try {
            offset = Integer.parseInt(myStartFromDigitTextField.getText());
        }
        catch (NumberFormatException e) {
            String msg = "Value \"" + myStartFromDigitTextField.getText() + "\" is invalid.";
            JOptionPane.showMessageDialog(new JFrame(), msg, null, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int[] data;
        try {
            data = piReader.getData(offset, myImageSizePanel.getImageWidth() * myImageSizePanel.getImageHeight());
        }
        catch (IndexOutOfBoundsException | IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(myImageSizePanel.getImageWidth(),
                myImageSizePanel.getImageHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < myImageSizePanel.getImageHeight(); i++)
            for (int j = 0; j < myImageSizePanel.getImageWidth(); j++)
                bufferedImage.setRGB(j, i, data[i * j]);
        Graphics g = myImagePanel.getGraphics();
        Image img = bufferedImage.getScaledInstance(myImagePanel.getWidth(), myImagePanel.getHeight(),
                Image.SCALE_FAST);
        g.drawImage(img, 0, 0, null);
        myImagePanel.setImage(img);
    }

    private static boolean isMacOS() {
        return System.getProperty("os.name").equals("Mac OS X");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PiViewer::new);
    }
}
