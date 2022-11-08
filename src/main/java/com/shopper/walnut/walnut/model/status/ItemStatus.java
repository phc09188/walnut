package com.shopper.walnut.walnut.model.status;

public interface ItemStatus {

    /**
     * 재고 소진
     */
    String ITEM_STATUS_STOP = "판매불가";
    /**
     * 물건 판매 중
     */
    String ITEM_STATUS_ING = "판매중";

    String ITEM_STATUS_WAREHOUSE = "입고 준비중";
    String ITEM_STATUS_SOLDOUT = "일시품절";
}
