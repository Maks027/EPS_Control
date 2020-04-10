import java.util.TimerTask;

public class CntTimerTask extends TimerTask {
    private long timerCounter = 0;

    public CntTimerTask(long timerCounter) {
        this.timerCounter = timerCounter;
    }

    public long getTimerCounter() {
        return timerCounter;
    }

    public void setTimerCounter(long timerCounter) {
        this.timerCounter = timerCounter;
    }
    @Override
    public void run() {
        timerCounter++;
        System.out.println(timerCounter);
    }
}
