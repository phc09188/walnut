package com.shopper.walnut.walnut.model.input;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandInput {
    private Integer brandId;
    private String brandName;
    private String brandPhone;
    private LocalDateTime brandRegDt;
    private LocalDateTime brandOkDt;
    private String brandStatus;

    String fileName;
    String urlFileName;
}
