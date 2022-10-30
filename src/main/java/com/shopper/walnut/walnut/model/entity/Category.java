package com.shopper.walnut.walnut.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity(name = "category")
public class Category {
    @Id
    // 상의 -> 긴팔 반팔 니트 등등~
    private String subCategoryName;
    //상의 하의 아우터
    private String categoryName;


}
