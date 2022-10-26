package com.shopper.walnut.walnut.model.entity;


import com.shopper.walnut.walnut.model.input.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User implements UserStatus {
    @Id
    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    @Embedded
    private Address address;
    private String userPhone;
    private LocalDate userRegDt;
//    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean emailAuthYn;
    private String emailAuthKey;
    private String userStatus;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean marketingYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean privateYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean payYn;

}
