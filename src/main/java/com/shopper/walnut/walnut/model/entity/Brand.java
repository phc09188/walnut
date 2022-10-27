package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.BrandStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "brand")
public class Brand implements BrandStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandId;
    private String brandName;
    private String brandPhone;
    private String brandLoginId;
    private String brandPassword;
    private LocalDateTime brandRegDt;
    private LocalDateTime brandOkDt;
    private String brandStatus;
    @Embedded
    private Address address;

    private String fileName;
    private String urlFileName;
}
