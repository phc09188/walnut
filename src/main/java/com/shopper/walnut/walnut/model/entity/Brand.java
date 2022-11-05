package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.BrandInput;
import com.shopper.walnut.walnut.model.status.BrandStatus;
import com.shopper.walnut.walnut.model.input.UserClassification;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "brand")
public class Brand implements BrandStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long brandId;
    private String brandName;
    private String brandPhone;
    private String brandEmail;
    private String brandLoginId;
    private String brandPassword;
    private LocalDateTime brandRegDt;
    private LocalDateTime brandOkDt;
    private String brandStatus;
    private UserClassification userClassification;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "brand")
    private List<BrandItem> list = new ArrayList<>();

    private String fileName;
    private String urlFileName;

    public static Brand of(BrandInput parameter, String encPassword, Address address){
        return Brand.builder()
                .brandName(parameter.getBrandName())
                .brandPhone(parameter.getBrandPhone())
                .brandEmail(parameter.getBrandEmail())
                .brandLoginId(parameter.getBrandLoginId())
                .brandPassword(encPassword)
                .address(address)
                .brandRegDt(LocalDateTime.now())
                .brandStatus(BrandStatus.MEMBER_STATUS_REQ)
                .userClassification(UserClassification.SELLER)
                .fileName(parameter.getFileName())
                .urlFileName(parameter.getUrlFileName())
                .build();
    }


}
