package banana.digital.crypto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public enum Executor {

    EXECUTOR;

    private ScheduledExecutorService mService;

    public void start() {
        final int poolSize = Runtime.getRuntime().availableProcessors() / 2 + 2; // число потоков
        mService = Executors.newScheduledThreadPool(poolSize);
    }

    public void execute(Runnable runnable) {
        mService.execute(runnable);
    }

    public void execute(Runnable runnable, long timeout) {
        mService.schedule(runnable, timeout, MILLISECONDS);
    }

}
