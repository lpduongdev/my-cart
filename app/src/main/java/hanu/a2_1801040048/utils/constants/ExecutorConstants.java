package hanu.a2_1801040048.utils.constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorConstants {
    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
}
