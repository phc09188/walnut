package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.CategoryName;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity(name = "category")
public class Category implements CategoryName {
    // 상의 -> 긴팔 반팔 니트 등등~
    @Id
    @NotNull
    private String subCategoryName;
    //상의 하의 아우터
    private String categoryName;


}
