import javax.swing.*;

public class StatusLabel<T extends JPanel & Observable> extends JLabel implements Observer {

    T mySubject;

    StatusLabel(T subject) {
        super();
        mySubject = subject;
    }

    private void updateText(int width, int height) {
        setText("Image size: " + width + " x " + height);
    }

    @Override
    public void update() {
        updateText(mySubject.getWidth(), mySubject.getHeight());
    }
}
