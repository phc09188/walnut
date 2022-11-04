package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.ItemStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "item")
public class Item implements ItemStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;
    private String brandName;
    private long brandItemId;
    private long brandId;
    private String itemName;
    private String urlFileName;
    private String fileName;
    private LocalDate addDt;
    private long cnt;
    private long price;
    private long salePrice;
    private String itemInfo;
    private long viewCount;
    private String saleStatus;
    private double reviewScore;
    private long reviewCount;
    private long payAmount;
    private long totalTake;

    private String categoryName;
    private String subCategoryName;


    public static Item of(BrandItem item) {
        return Item.builder()
                .brandItemId(item.getBrandItemId())
                .brandName(item.getBrand().getBrandName())
                .brandId(item.getBrand().getBrandId())
                .itemName(item.getItemName())
                .urlFileName(item.getUrlFileName())
                .fileName(item.getFileName())
                .addDt(item.getAddDt())
                .cnt(item.getCnt())
                .price(item.getPrice())
                .salePrice(item.getSalePrice())
                .itemInfo(item.getItemInfo())
                .viewCount(item.getViewCount())
                .saleStatus(item.getSaleStatus())
                .reviewScore(item.getReviewScore())
                .reviewCount(item.getReviewCount())
                .payAmount(item.getPayAmount())
                .totalTake(item.getTotalTake())
                .categoryName(item.getCategoryName())
                .subCategoryName(item.getSubCategoryName())
                .build();
    }
}
