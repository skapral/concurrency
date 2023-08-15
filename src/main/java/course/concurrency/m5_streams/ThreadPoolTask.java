package course.concurrency.m5_streams;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ThreadPoolTask {

    // Task #1
    public ThreadPoolExecutor getLifoExecutor() {
        return new ThreadPoolExecutor(
            0,
            8,
            1,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>() {
                @Override
                public Runnable pollFirst() {
                    return super.pollLast();
                }

                @Override
                public Runnable takeFirst() throws InterruptedException {
                    return super.takeLast();
                }

                @Override
                public Runnable removeFirst() {
                    return super.removeLast();
                }
            }
        );
    }

    // Task #2
    public ThreadPoolExecutor getRejectExecutor() {
        return new ThreadPoolExecutor(
            0,
            8,
            1,
            TimeUnit.MINUTES,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.DiscardPolicy()
        );
    }
}
