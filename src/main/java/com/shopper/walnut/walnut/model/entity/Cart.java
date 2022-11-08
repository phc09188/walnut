package com.shopper.walnut.walnut.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;
    @ManyToOne()
    @JoinColumn(name="userId", nullable = false)
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;
    private long cnt;


    //생성자
    public Cart(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
