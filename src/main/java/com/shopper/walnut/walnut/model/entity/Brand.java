package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.BrandStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private LocalDateTime brandRegDt;
    private LocalDateTime brandOkDt;
    private String brandStatus;

    private String fileName;
    private String urlFileName;
}
