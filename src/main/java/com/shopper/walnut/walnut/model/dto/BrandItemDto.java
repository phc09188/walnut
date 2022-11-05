package com.shopper.walnut.walnut.model.dto;

import com.shopper.walnut.walnut.model.entity.BrandItem;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BrandItemDto {
    private long brandItemId;
    private String itemName;
    private String fileName;
    private String urlFileName;
    private long price;
    private long salePrice;
    private String itemInfo;
    private String saleStatus;
    private String subCategoryName;
    private long cnt;

    public static BrandItemDto  of(BrandItem brandItem) {
        return BrandItemDto.builder()
                .brandItemId(brandItem.getBrandItemId())
                .itemName(brandItem.getItemName())
                .urlFileName(brandItem.getUrlFileName())
                .price(brandItem.getPrice())
                .salePrice(brandItem.getSalePrice())
                .itemInfo(brandItem.getItemInfo())
                .saleStatus(brandItem.getSaleStatus())
                .cnt(brandItem.getCnt())
                .build();
    }
}
