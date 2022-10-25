package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "brandItem")
public class BrandItem implements ItemStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandItemId;

    @OneToOne()
    @JoinColumn(name="brandId", nullable = false)
    public Brand brand;
    private String itemName;
    private String itemImgUrl;
    private LocalDate addDt;
    private long price;
    private long salePrice;
    private String itemInfo;
    private long viewCount;
    private String saleStatus;
    private double reviewScore;
    private long reviewCount;

    private String fileName;
    private String urlFileName;

}
