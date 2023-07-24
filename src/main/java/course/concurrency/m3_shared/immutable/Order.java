package course.concurrency.m3_shared.immutable;

import java.util.List;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

public class Order {

    public enum Status { NEW, IN_PROGRESS, DELIVERED }

    private final Long id;
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final boolean isPacked;
    private final Status status;

    public Order(Long id, List<Item> items, PaymentInfo paymentInfo, boolean isPacked, Status status) {
        this.id = id;
        this.items = items;
        this.paymentInfo = paymentInfo;
        this.isPacked = isPacked;
        this.status = status;
    }

    public static Order createNew(Long id, List<Item> items) {
        return new Order(
            id,
            items,
            null,
            false,
            NEW
        );
    }

    public final boolean checkStatus() {
        if (items != null && !items.isEmpty() && paymentInfo != null && isPacked) {
            return true;
        }
        return false;
    }

    public final Long getId() {
        return id;
    }

    public final Order withId(Long id) {
        return new Order(id, items, paymentInfo, isPacked, status);
    }

    public final List<Item> getItems() {
        return items;
    }

    public final PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public final Order withPaymentInfo(PaymentInfo paymentInfo) {
        return new Order(
            id,
            items,
            paymentInfo,
            isPacked,
            Status.IN_PROGRESS
        );
    }

    public final boolean isPacked() {
        return isPacked;
    }

    public final Order withPacked(boolean packed) {
        return new Order(
            id,
            items,
            paymentInfo,
            packed,
            Status.IN_PROGRESS
        );
    }

    public final Status getStatus() {
        return status;
    }

    public final Order withStatus(Status status) {
        return new Order(
            id,
            items,
            paymentInfo,
            isPacked,
            status
        );
    }
}
