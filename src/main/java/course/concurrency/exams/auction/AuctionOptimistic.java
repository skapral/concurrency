package course.concurrency.exams.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionOptimistic implements Auction {
    private static final Logger log = LoggerFactory.getLogger(AuctionOptimistic.class);
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private Bid latestBid = new Bid(null, null, 0L);

    public boolean propose(Bid bid) {
        if(lock.writeLock().tryLock()) {
            try {
                if (bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        try {
            lock.readLock().lock();
            return latestBid;
        } finally {
            lock.readLock().unlock();
        }
    }
}
