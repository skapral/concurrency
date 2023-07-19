package course.concurrency.exams.auction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionPessimistic implements Auction {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Notifier notifier;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private Bid latestBid = new Bid(null, null, 0L);

    public boolean propose(Bid bid) {
        lock.writeLock().lock();
        try {
            if (bid.getPrice() > latestBid.getPrice()) {
                notifier.sendOutdatedMessage(latestBid);
                latestBid = bid;
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
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
