package course.concurrency.exams.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionStoppableOptimistic implements AuctionStoppable {
    private static final Logger log = LoggerFactory.getLogger(AuctionStoppableOptimistic.class);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean stopped = false;
    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private Bid latestBid;

    public boolean propose(Bid bid) {
        if(!stopped && lock.writeLock().tryLock()) {
            try {
                if (bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    log.info("latestBid updated to " + latestBid.getPrice());
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

    public Bid stopAuction() {
        stopped = true;
        return getLatestBid();
    }
}
