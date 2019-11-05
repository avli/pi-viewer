import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImagePanel extends JPanel implements Observable {

    final List<Observer> observers = new ArrayList<>();

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
