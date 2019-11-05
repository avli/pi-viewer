import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImagePanel extends JPanel implements Observable {

    private final List<Observer> observers = new ArrayList<>();
    private Image myImage;

    public void setImage(Image myImage) {
        this.myImage = myImage;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer o : observers)
            o.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (myImage != null)
            g.drawImage(myImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0,
                    null);
        notifyAllObservers();
    }



    //    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(0, 0, getWidth(), getHeight());
//        drawCross(g);
//    }
//
//    private void drawCross(Graphics g) {
//        g.setColor(Color.BLACK);
//        g.drawLine(0, 0, getWidth(), getHeight());
//        g.drawLine(0, getHeight(), getWidth(), 0);
//    }
}
