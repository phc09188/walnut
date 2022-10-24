package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "userReceptionStatus")
public class UserReceptionStatus {
    @Id
    private String userId;
    private String userName;
    private String userEmail;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")

    private boolean marketingYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean privateYn;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean payYn;
}
