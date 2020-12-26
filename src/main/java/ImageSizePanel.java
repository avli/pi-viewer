import javax.swing.*;
import java.awt.*;

public class ImageSizePanel extends JPanel {

    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 320;

    private final JLabel myWidthLabel = new JLabel("Width: ");
    private final JLabel myHeightLabel = new JLabel("Height: ");
    private final JTextField myWidthTextField = new JTextField(10);
    private final JTextField myHeightTextField = new JTextField(10);

    public ImageSizePanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        add(myWidthLabel);
        add(myWidthTextField);
        add(myHeightLabel);
        add(myHeightTextField);
        myWidthTextField.setText(Integer.toString(DEFAULT_WIDTH));
        myHeightTextField.setText(Integer.toString(DEFAULT_HEIGHT));
    }

    private int getFieldValue(JTextField field) {
        String text = field.getText();
        int value;
        try {
            value = Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            String msg = "Value \"" + text + "\" is invalid.";
            JOptionPane.showMessageDialog(new JFrame(), msg, null, JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        if (value > 0) {
            return value;
        }
        else {
            String msg = "Width and height must be greater than zero.";
            JOptionPane.showMessageDialog(new JFrame(), msg, null, JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public int getImageWidth() {
        return getFieldValue(myWidthTextField);
    }

    public int getImageHeight() {
        return getFieldValue(myHeightTextField);
    }
}
