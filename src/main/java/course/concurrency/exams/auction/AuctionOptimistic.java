package course.concurrency.exams.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionOptimistic implements Auction {
    private final Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private AtomicReference<Bid> latestBidRef = new AtomicReference<>(
        new Bid(null, null, 0L)
    );

    public boolean propose(Bid bid) {
        Bid latestBid;
        do {
            latestBid = latestBidRef.get();
            if (bid.getPrice() <= latestBid.getPrice()) {
                return false;
            }
        } while(latestBidRef.compareAndSet(latestBid, bid));
        notifier.sendOutdatedMessage(latestBid);
        return true;
    }

    public Bid getLatestBid() {
        return latestBidRef.get();
    }
}
