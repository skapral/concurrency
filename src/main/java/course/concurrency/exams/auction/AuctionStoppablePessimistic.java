package course.concurrency.exams.auction;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean stopped = false;
    private final Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private Bid latestBid = new Bid(null, null, 0L);

    public boolean propose(Bid bid) {
        if(stopped) return false;
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

    public Bid stopAuction() {
        stopped = true;
        return getLatestBid();
    }
}
