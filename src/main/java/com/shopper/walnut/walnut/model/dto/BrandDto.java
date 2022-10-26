package com.shopper.walnut.walnut.model.dto;

import com.shopper.walnut.walnut.model.entity.Brand;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private int brandId;
    private String brandName;
    private String brandPhone;
    private String brandLoginId;
    private String brandPassword;
    private LocalDateTime brandRegDt;
    private LocalDateTime brandOkDt;
    private String brandStatus;
    private String fileName;
    private String urlFileName;
    private String zipCode;
    private String streetAdr;
    private String detailAdr;


    public static BrandDto of(Brand brand) {
        return BrandDto.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .brandPhone(brand.getBrandPhone())
                .brandRegDt(brand.getBrandRegDt())
                .brandOkDt(brand.getBrandOkDt())
                .brandStatus(brand.getBrandStatus())
                .build();
    }
}
