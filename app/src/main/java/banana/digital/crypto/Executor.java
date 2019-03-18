package banana.digital.crypto;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Executor {
    public static class SingletonHolder {
        public static final Executor HOLDER_INSTANCE = new Executor();
    }

    public static Executor getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public static ScheduledExecutorService mService;

    public void start() {
        int poolsize = 3;
        mService = Executors.newScheduledThreadPool(poolsize);
    }

    public static void execute(Runnable runnable) {
        mService.execute(runnable);
    }
}