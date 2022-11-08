package com.shopper.walnut.walnut.model.input;


import com.shopper.walnut.walnut.model.status.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInput {
    private String userName; // 회원 이름
    private OrderStatus orderStatus; // 주문 상태 [ORDER, CANCEL]


}
