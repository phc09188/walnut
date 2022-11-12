package com.shopper.walnut.walnut.model.input;


import com.shopper.walnut.walnut.model.type.QnaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QnaInput {
    private String userId;

    private String subject;
    private String content;
    private QnaType type;
}
