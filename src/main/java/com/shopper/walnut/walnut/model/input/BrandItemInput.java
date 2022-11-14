package com.shopper.walnut.walnut.model.input;


import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Random;

@Setter @Getter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class BrandItemInput {
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


        BrandItem item = BrandItem.builder()
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
        return item;
    }
}
