package course.concurrency.m2_async.cf.report;

public class Main {
    public static volatile String a = "0";

    public static String b = "0";

    public static void main(String... args) {
        for(int i = 0; i < 100; i++) {
            setA(String.valueOf(i));
            //System.out.println(a);
            setB(String.valueOf(i));
            //synchronized (Main.class) {
            //    System.out.println(b);
            //}
        }
    }

    public static String setA(String v) {
        try {
            Thread thread = new Thread(() -> {
                long t = System.nanoTime();
                a = v;
                System.out.println("volatile write " + (System.nanoTime() - t));
            });
            thread.start();
            thread.join();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return a;
    }

    public static String setB(String v) {
        try {
            Thread thread = new Thread(() -> {
                long t = System.nanoTime();
                synchronized (Main.class) {
                    b = v;
                }
                System.out.println("sync write " + (System.nanoTime() - t));
            });
            thread.start();
            thread.join();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return b;
    }
}
