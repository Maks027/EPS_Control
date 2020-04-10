import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

enum TimeMultipliers{
    X1,
    X10,
    X100,
    X1000,
    X2000,
    X5000,
    X10000
}

public class TimeSim {
    mainForm mf;

    private JPanel mainTimePanel;
    private JTabbedPane tabbedPane1;
    private JPanel statePanel;
    private JPanel timeStatePanel;
    private JPanel paramPreviewPanel;
    private JTextField textField_startTimeDate;
    private JTextField textField_currentTimeDate;
    private JTextField textField_elapsedTimeDate;
    private JPanel timeContrPanel;
    private JButton button_fastBackward;
    private JButton button_StartPause;
    private JButton button_fastForward;
    private JTextField textField_startTimeHour;
    private JTextField textField_currentTimeHour;
    private JTextField textField_elapsedTimeHour;
    private JButton button4;
    private JButton button_stopTimer;
    private JLabel multiplierLabel;

    private Timer timer;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter elapsedTimeFormatter;

    private long currentTimerCnt = 0;
    private long timerCnt = 0;
    private int cntMult = 1;
    private int pressCnt = 0;
    private int timerState = 0;

    TimeMultipliers currentMultiplier = TimeMultipliers.X1;

    public TimeSim() {
        LocalDateTime localDateTime = LocalDateTime.now();
        currentTimerCnt =  localDateTime.toEpochSecond(ZoneOffset.UTC);

        timer = new Timer(1000, event -> timerAction());
        cntMult = 1;
        timer.setInitialDelay(100);

        button_fastBackward.addActionListener(e -> FastBackwardAction());
        button_fastForward.addActionListener(e -> FastForwardAction());
        button_StartPause.addActionListener(e -> StartPauseAction());
        button_stopTimer.addActionListener(e -> StopAction());
    }

    public void FastForwardAction() {
        if (++pressCnt >= 6)
            pressCnt = 6;

        SetMultiplier(pressCnt);
    }
    public void FastBackwardAction() {
        if (--pressCnt <= 0)
            pressCnt = 0;

        SetMultiplier(pressCnt);
    }
    public void StartPauseAction() {
        if (timerState == 0){
            timer.start();
        } else {
            timer.stop();
        }
        timerState ^= 1;
    }
    public void StopAction() {
        timer.stop();
        timer.restart();
    }

    public void SetMultiplier(int multiplier) {
        switch (multiplier){
            case 0:
                timer.setDelay(1000);
                cntMult = 1;
                multiplierLabel.setText("X1");
                break;
            case 1:
                timer.setDelay(100);
                cntMult = 1;
                multiplierLabel.setText("X10");
                break;
            case 2:
                timer.setDelay(10);
                cntMult = 1;
                multiplierLabel.setText("X100");
                break;
            case 3:
                timer.setDelay(10);
                cntMult = 10;
                multiplierLabel.setText("X1000");
                break;
            case 4:
                timer.setDelay(10);
                cntMult = 20;
                multiplierLabel.setText("X2000");
                break;

            case 5:
                timer.setDelay(10);
                cntMult = 50;
                multiplierLabel.setText("X5000");
                break;

            case 6:
                timer.setDelay(10);
                cntMult = 100;
                multiplierLabel.setText("X10000");
                break;
        }
    }

    public void timerAction(){
        currentTimerCnt += cntMult;
        timerCnt += cntMult;
        LocalDateTime d = LocalDateTime.ofEpochSecond(currentTimerCnt, 0, ZoneOffset.UTC);
        textField_currentTimeDate.setText(dateFormatter.format(d));
        textField_currentTimeHour.setText(timeFormatter.format(d));
        LocalDateTime eTime = LocalDateTime.ofEpochSecond(timerCnt, 0, ZoneOffset.UTC);

    }

    public JPanel getMainTimePanel() {
        return mainTimePanel;
    }

    public void setMainTimePanel(JPanel mainTimePanel) {
        this.mainTimePanel = mainTimePanel;
    }

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("D/N");
        jFrame.setSize(800, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(new TimeSim().getMainTimePanel());

        jFrame.setVisible(true);
    }
}
