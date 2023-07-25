package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {

    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    private long nextId() {
        return nextId.getAndIncrement();
    }

    public final long createOrder(List<Item> items) {
        long id = nextId();
        Order order = Order.createNew(id, items);
        currentOrders.put(id, order);
        return id;
    }

    public final void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        Order order = currentOrders.computeIfPresent(
            orderId,
            (k, v) -> v.withPaymentInfo(paymentInfo)
        );
        if (order.checkStatus()) {
            deliver(currentOrders.get(orderId));
        }
    }

    public final void setPacked(long orderId) {
        Order order = currentOrders.computeIfPresent(
            orderId,
            (k, v) -> v.withPacked(true)
        );
        if (order.checkStatus()) {
            deliver(currentOrders.get(orderId));
        }
    }

    private void deliver(Order order) {
        /* ... */
        currentOrders.computeIfPresent(
            order.getId(),
            (k, v) -> v.withStatus(Order.Status.DELIVERED)
        );
    }

    public final boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
}
