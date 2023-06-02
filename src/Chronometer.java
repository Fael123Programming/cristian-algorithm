import java.util.Timer;
import java.util.TimerTask;

public class Chronometer {
    private static final int PERIOD = 1000;
    private long current;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            current += PERIOD;
            System.out.printf("\nElapsed: %02d:%02d.%03d", current / 60000, current / 1000 % 60, current % 1000);
        }
    };

    public void start() {
        new Timer(true).scheduleAtFixedRate(timerTask, 0, PERIOD);
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        System.out.printf("Chronometer reset from %d to %d (diff %d)", this.current, current, current - this.current);
        this.current = current;
    }
}
