public class SimulationTimer {
    long startTime = 0;
    long stopTime = 0;
    boolean timerOn = false;
    int nspers = 1000000000;
    String timerName = null;
    public SimulationTimer(String name) {
        timerName = name;
    }
    public void startTimer() {
        startTime = System.nanoTime();
        timerOn = true;
    }
    public void stopTimer() {
        if (timerOn) {
            stopTime = System.nanoTime();
            timerOn = false;
        }
    }

    public long getSeconds() {
        long elapsedTime = stopTime - startTime;
        return (elapsedTime / nspers);
    }

    public long getCurrentSeconds() {
        long elapsedTime = System.nanoTime() - startTime;
        return (elapsedTime / nspers);
    }

    public boolean secondPassed() {
        long elapsedTime = (System.nanoTime() - startTime);
        return elapsedTime % 1000000000 == 0;
    }
 }