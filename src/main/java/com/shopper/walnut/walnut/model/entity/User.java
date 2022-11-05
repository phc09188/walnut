package com.shopper.walnut.walnut.model.entity;


import com.shopper.walnut.walnut.controller.BrandController;
import com.shopper.walnut.walnut.model.input.UserClassification;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.status.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private long userCache;
    private long userPoint;
    @Embedded
    private Address address;
    private String userPhone;
    private LocalDate userRegDt;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cart> cartList = new ArrayList<>();


//    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean emailAuthYn;
    private String emailAuthKey;

    private String userStatus;
    private UserClassification userClassification;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean marketingYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean privateYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean payYn;

    public static User toSign(Brand brand) {
        return User.builder()
                .userId(brand.getBrandLoginId())
                .userName(brand.getBrandName())
                .userPassword(brand.getBrandPassword())
                .address(brand.getAddress())
                .userPhone(brand.getBrandPhone())
                .userRegDt(LocalDate.now())
                .userCache(0)
                .userPoint(0)
                .emailAuthYn(true)
                .userStatus(UserStatus.MEMBER_STATUS_ING)
                .userClassification(UserClassification.SELLER)
                .marketingYn(true)
                .privateYn(true)
                .payYn(true)
                .build();
    }

    public static User of(UserInput parameter,String encPassword, String uuid) {
        return User.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .userEmail(parameter.getUserEmail())
                .userPassword(encPassword)
                .address(new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr()))
                .userPhone(parameter.getUserPhone())
                .userStatus(UserStatus.MEMBER_STATUS_REQ)
                .userRegDt(LocalDate.now())
                .userCache(0)
                .userPoint(0)
                .emailAuthKey(uuid)
                .marketingYn(parameter.isMarketingYn())
                .privateYn(parameter.isPrivateYn())
                .payYn(parameter.isPayYn())
                .userClassification(UserClassification.USER)
                .build();
    }

}
