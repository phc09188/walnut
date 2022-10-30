package com.shopper.walnut.walnut.model.dto;

import com.shopper.walnut.walnut.model.input.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BrandItemDto {
    private String itemName;
    private String fileName;
    private String urlFileName;
    private long price;
    private String itemInfo;
    private String saleStatus;
    private String subCategoryName;
    private long cnt;

}
