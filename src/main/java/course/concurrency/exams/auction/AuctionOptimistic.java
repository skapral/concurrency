package course.concurrency.exams.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionOptimistic implements Auction {
    private static final Logger log = LoggerFactory.getLogger(AuctionOptimistic.class);

    private final Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private AtomicReference<Bid> latestBidRef = new AtomicReference<>(
        new Bid(null, null, 0L)
    );

    public boolean propose(Bid bid) {
        while(true) {
            Bid latestBid = latestBidRef.get();
            if (bid.getPrice() > latestBid.getPrice()) {
                if (latestBidRef.compareAndSet(latestBid, bid)) {
                    notifier.sendOutdatedMessage(latestBid);
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        }
    }

    public Bid getLatestBid() {
        return latestBidRef.get();
    }
}
