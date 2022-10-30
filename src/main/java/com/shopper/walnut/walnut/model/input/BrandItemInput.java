package com.shopper.walnut.walnut.model.input;


import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import lombok.*;

import java.time.LocalDate;

@Setter @Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class BrandItemInput {
    private String itemName;
    private long price;
    private String itemInfo;
    private String saleStatus;
    private String subCategoryName;
    private long cnt;
    String fileName;
    String fileUrl;

    public static BrandItem of(BrandItemInput input) {
        BrandItem item = BrandItem.builder()
                .itemName(input.itemName)
                .fileName(input.fileName)
                .urlFileName(input.fileUrl)
                .addDt(LocalDate.now())
                .price(input.price)
                .salePrice(input.price)
                .itemInfo(input.itemInfo)
                .viewCount(0)
                .reviewCount(0)
                .reviewScore(0)
                .payAmount(0)
                .totalTake(0)
                .subCategoryName(input.subCategoryName)
                .saleStatus(input.saleStatus)
                .cnt(input.cnt)
                .build();
        return item;
    }
}
