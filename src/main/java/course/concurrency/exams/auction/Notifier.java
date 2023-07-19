package course.concurrency.exams.auction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifier {
    private static final ExecutorService es = Executors.newFixedThreadPool(32000);

    public void sendOutdatedMessage(Bid bid) {
        imitateSending();
    }

    private void imitateSending() {
        es.submit(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        });
    }

    public void shutdown() {}
}
