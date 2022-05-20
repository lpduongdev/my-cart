package hanu.a2_1801040048.utils.constants;

import android.os.Handler;
import android.os.Looper;

public class HandlerConstants {
    private static Handler instance;
    private HandlerConstants(){}

    public static Handler getInstance(){
        if(instance == null)
            synchronized(HandlerConstants.class) {
                if (instance == null)
                    instance = new Handler(Looper.getMainLooper());
            }
        return instance;
    }
}
