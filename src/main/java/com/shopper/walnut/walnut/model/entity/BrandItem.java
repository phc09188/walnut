package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.status.ItemStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "brandItem")
public class BrandItem implements ItemStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long brandItemId;

    @ManyToOne()
    @JoinColumn(name="brandId", nullable = false)
    public Brand brand;
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
}
