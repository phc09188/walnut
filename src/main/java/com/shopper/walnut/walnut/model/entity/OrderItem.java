package com.shopper.walnut.walnut.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "orderItemId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    private long orderPrice;

    private long count;

    public static OrderItem createOrderItem(Item item, long orderPrice, long count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(orderPrice);
        item.removeStock(orderItem.count);
        return orderItem;
    }

    /**주문취소**/
    public void cancel() {
        getItem().addStock(count);
    }

    /**총 주문 비용**/
    public long getTotalPrice() {
        return getCount() * getOrderPrice();
    }
}
