package com.shopper.walnut.walnut.model.input;


import com.shopper.walnut.walnut.model.entity.BrandItem;
import lombok.*;

import java.time.LocalDate;

@Setter @Getter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class BrandItemInput {
    private long brandItemId;
    private String itemName;
    private long price;
    private long salePrice;
    private String itemInfo;
    private String saleStatus;

    private String categoryName;
    private String subCategoryName;
    private long cnt;
    String fileName;
    String fileUrl;

    public static BrandItem of(BrandItemInput input) {
        return BrandItem.builder()
                .itemName(input.itemName)
                .fileName(input.fileName)
                .urlFileName(input.fileUrl)
                .addDt(LocalDate.now())
                .price(input.price)
                .salePrice(input.salePrice)
                .itemInfo(input.itemInfo)
                .viewCount(0)
                .reviewCount(0)
                .reviewScore(0)
                .payAmount(0)
                .totalTake(0)
                .categoryName(input.categoryName)
                .subCategoryName(input.subCategoryName)
                .saleStatus(input.saleStatus)
                .cnt(input.cnt)
                .build();
    }
}
