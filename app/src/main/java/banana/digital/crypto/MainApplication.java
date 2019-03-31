package banana.digital.crypto;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import banana.digital.crypto.repository.RxTransactionRepository;

import static banana.digital.crypto.Executor.EXECUTOR;

public class MainApplication extends Application {

    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HEIGHT_PX;
    public static float density;


    @Override
    public void onCreate() {
        super.onCreate();


        Executor.EXECUTOR.start();
        RxTransactionRepository.getInstance().initialize(this);


        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        density = getResources().getDisplayMetrics().density;
        SCREEN_WIDTH_PX = dm.widthPixels;
        SCREEN_HEIGHT_PX = dm.heightPixels;
    }
}
