public interface Observable {
    void attach(Observer observer);
    void notifyAllObservers();
}
