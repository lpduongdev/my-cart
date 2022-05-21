package hanu.a2_1801040048.utils.constants;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorConstants {
    private static Executor instance;
    private ExecutorConstants(){}

    public static Executor getInstance(){
        if(instance == null)
            synchronized(ExecutorConstants.class) {
                if (instance == null)
                    instance = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
            }
        return instance;
    }
}
