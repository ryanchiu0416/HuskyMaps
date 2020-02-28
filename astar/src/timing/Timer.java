package timing;

import java.time.Duration;

/**
 * A timer class for measuring elapsed wall-clock time with nanosecond precision.
 *
 * When instantiated or when {@link #setTimer(Duration)} is called, the timer saves the starting
 * time (current time) and end time (current time + timer duration). Later, it can be queried to
 * check whether the timer's duration has exceeded, or to get the elapsed time since the timer
 * was started.
 *
 * Inspired by algss4's Stopwatch.
 */
public class Timer {
    private long start;
    private long end;

    /**
     * Initializes and starts a timer with the given duration.
     */
    public Timer(Duration duration) {
        setTimer(duration);
    }

    /**
     * Returns the elapsed time since the timer was set.
     *
     * @return elapsed time since the timer was set, in nanoseconds
     */
    public long elapsedNanos() {
        long now = System.nanoTime();
        return now - this.start;
    }

    /**
     * Returns the elapsed time since the timer was set.
     *
     * @return elapsed time since the timer was set
     */
    public Duration elapsedDuration() {
        long now = System.nanoTime();
        return Duration.ofNanos(now - this.start);
    }

    /**
     * Resets the timer with the given duration.
     *
     * @param duration the new timer duration
     */
    public void setTimer(Duration duration) {
        this.start = System.nanoTime();
        this.end = this.start + duration.toNanos();
    }

    /**
     * Returns whether or not the timer's duration has been exceeded.
     *
     * @return true if the elapsed time since the timer was set is greater than or equal to the
     * timer's duration; false otherwise
     */
    public boolean isTimeUp() {
        long now = System.nanoTime();
        return now >= this.end;
    }
}
