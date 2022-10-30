package com.shopper.walnut.walnut.model.input;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryInput {
    private String subCategoryName;
    private String categoryName;
}
