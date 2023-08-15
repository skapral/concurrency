package course.concurrency.m3_shared.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RestaurantBenchTests {
    @Test
    public void test() throws Exception {
        ExecutorService tp = Executors.newFixedThreadPool(1);
        RestaurantServiceB s = new RestaurantServiceB();
        CountDownLatch l = new CountDownLatch(1);
        String r = "ABCDEFGHIJKLMNOPQRSTUVW";
        //String r = "A";
        ArrayList<Long> agg = new ArrayList<>();
        ArrayList<Long> aggg = new ArrayList<>();
        for(int i = 0; i < 200; i++) {
            var imod = i % r.length();
            var S = r.substring(imod, imod);
            tp.submit(() -> {
                try {
                    l.await();
                    var t = System.nanoTime();
                    s.addToStat(S);
                    t = System.nanoTime() - t;
                    synchronized (r) {
                        agg.add(t);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            tp.submit(() -> {
                try {
                    l.await();
                    var t = System.nanoTime();
                    s.getByName(S);
                    t = System.nanoTime() - t;
                    synchronized (r) {
                        aggg.add(t);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        l.countDown();
        tp.shutdown();
        tp.awaitTermination(5, TimeUnit.MINUTES);
        synchronized (r) {
            agg.sort(Long::compare);
            aggg.sort(Long::compare);
            System.out.println("agg = " + (agg.get(100) + agg.get(101)) / 2);
            System.out.println("aggg = " + (aggg.get(100) + aggg.get(101)) / 2);
        }
    }
}
