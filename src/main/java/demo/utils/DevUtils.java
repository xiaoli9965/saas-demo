package demo.utils;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author Alex
 */
public class DevUtils {
    @SneakyThrows
    public static void sleep(int m) {
        TimeUnit.SECONDS.sleep(m);

    }
}
