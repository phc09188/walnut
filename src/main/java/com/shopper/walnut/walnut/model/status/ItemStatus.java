package com.shopper.walnut.walnut.model.status;

public interface ItemStatus {

    /**
     * 재고 소진
     */
    String ITEM_STATUS_STOP = "SellNo";
    /**
     * 물건 판매 중
     */
    String ITEM_STATUS_ING = "Sell";

    String ITEM_STATUS_WAREHOUSE = "WareHousing";
    String ITEM_STATUS_SOLDOUT = "SoldOut";
}
