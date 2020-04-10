import java.util.Timer;
import java.util.TimerTask;

public class TimeManagement {
    Timer timer;
    CntTimerTask timerTask;


    public TimeManagement(long startTime) {
        timer = new Timer(true);
        timerTask = new CntTimerTask(startTime);

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public long getTimerCnt() {
        return timerTask.getTimerCounter();
    }

    public void setTimerCnt(long val) {
        timerTask.setTimerCounter(val);
    }


    public static void main(String[] args) {
        Timer timer = new Timer(true);
        TimerTask timerTask = new CntTimerTask(0);

        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        System.out.println("Timer started");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
    }

}
