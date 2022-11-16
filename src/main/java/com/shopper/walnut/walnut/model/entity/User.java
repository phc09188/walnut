package com.shopper.walnut.walnut.model.entity;


import com.shopper.walnut.walnut.controller.BrandController;
import com.shopper.walnut.walnut.model.input.UserClassification;
import com.shopper.walnut.walnut.model.input.UserInput;
import com.shopper.walnut.walnut.model.status.MemberShip;
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
public class User {
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
    @Enumerated(EnumType.STRING)
    private MemberShip memberShip;
    private long payAmount;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<QnA> qnAList = new ArrayList<>();


    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean emailAuthYn;
    private String emailAuthKey;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private UserClassification userClassification;



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
                .memberShip(MemberShip.BRONZE)
                .payAmount(0)
                .emailAuthYn(true)
                .userStatus(UserStatus.ING)
                .userClassification(UserClassification.SELLER)
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
                .memberShip(MemberShip.BRONZE)
                .payAmount(0)
                .address(new Address(parameter.getZipCode(),parameter.getStreetAdr(),parameter.getDetailAdr()))
                .userPhone(parameter.getUserPhone())
                .userStatus(UserStatus.REQ)
                .userRegDt(LocalDate.now())
                .userCache(0)
                .userPoint(0)
                .emailAuthKey(uuid)
                .privateYn(parameter.isPrivateYn())
                .payYn(parameter.isPayYn())
                .userClassification(UserClassification.USER)
                .build();
    }

}
