package com.shopper.walnut.walnut.model.dto;

import com.shopper.walnut.walnut.model.entity.Brand;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .zipCode(brand.getAddress().getZipCode())
                .streetAdr(brand.getAddress().getStreetAdr())
                .detailAdr(brand.getAddress().getDetailAdr())
                .urlFileName(brand.getUrlFileName())
                .brandOkDt(brand.getBrandOkDt())
                .brandStatus(brand.getBrandStatus())
                .build();
    }

    public static List<BrandDto> of(List<Brand> allBrand) {
        if(allBrand != null){
            List<BrandDto> brandDtoList = new ArrayList<>();
            for(Brand x : allBrand){
                brandDtoList.add(of(x));
            }
            return brandDtoList;
        }
        return null;
    }
}
