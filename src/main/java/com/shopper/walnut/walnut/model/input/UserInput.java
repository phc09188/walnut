package com.shopper.walnut.walnut.model.input;

import lombok.*;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
    String userId;
    String userName;
    String userEmail;
    String userPassword;
    String zipCode;
    String streetAdr;
    String detailAdr;
    String userPhone;
    boolean marketingYn;
    boolean privateYn;
    boolean payYn;

}
