package course.concurrency.exams.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuctionStoppableOptimistic implements AuctionStoppable {
    private static final Logger log = LoggerFactory.getLogger(AuctionOptimistic.class);

    private final Notifier notifier;
    private volatile boolean stopped = false;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private AtomicReference<Bid> latestBidRef = new AtomicReference<>(
        new Bid(null, null, 0L)
    );

    public boolean propose(Bid bid) {
        while(!stopped) {
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
        return false;
    }

    public Bid getLatestBid() {
        return latestBidRef.get();
    }

    public Bid stopAuction() {
        stopped = true;
        return getLatestBid();
    }
}
