import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class RailgunViewer {

    private final static String TITLE = "RailgunViewer";
    private final URL myPathToImage;

    private JFrame myFrame = new JFrame(TITLE);

    public RailgunViewer() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myPathToImage = getClass().getResource("railgun.png");

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(myPathToImage.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalBufferedImage = bufferedImage;
        class MyPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image scaledImage = finalBufferedImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST);
                g.drawImage(scaledImage, 0, 0, null);
            }

            public void drawRandomCircle() {
                Graphics g = getGraphics();
                Random random = new Random();
                int x = random.nextInt(getWidth());
                int y = random.nextInt(getHeight());
                int radius = random.nextInt(getWidth());
                float h = random.nextFloat() * 60;
                float s = random.nextFloat() * 100;
                float b = random.nextFloat() * 100;
                g.setColor(Color.getHSBColor(h, s, b));
                g.drawOval(x, y, radius, radius);
            }

            public void drawCircle(int x, int y) {
                Graphics g = getGraphics();
                g.drawOval(x - 15, y - 15, 30, 30);
            }
        };

        MyPanel pane = new MyPanel();

        pane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pane.drawCircle(e.getX(), e.getY());
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

        pane.setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

        JPanel bottomPanel = new JPanel();
        JButton btn = new JButton("Yo!");
        bottomPanel.add(btn);
        LayoutManager l = bottomPanel.getLayout();

        btn.addActionListener(e -> {
            pane.drawRandomCircle();
        });

        JButton setOriginalSizeBtn = new JButton("Reset size");
        setOriginalSizeBtn.addActionListener(e -> {
            myFrame.setSize(myFrame.getMaximumSize());
        });

        bottomPanel.add(setOriginalSizeBtn);

        myFrame.add(bottomPanel);
        myFrame.add(pane);

        myFrame.add(bottomPanel, BorderLayout.SOUTH);
        myFrame.add(new JPanel(), BorderLayout.LINE_START);
        myFrame.add(new JPanel(), BorderLayout.LINE_END);
        myFrame.add(pane, BorderLayout.CENTER);

        myFrame.pack();
        myFrame.setMaximumSize(myFrame.getSize());
        myFrame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(RailgunViewer::new);
    }
}
