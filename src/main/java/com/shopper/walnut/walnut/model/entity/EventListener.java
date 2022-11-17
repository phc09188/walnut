package com.shopper.walnut.walnut.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "eventListener")
public class EventListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long eventListenerId;

    /** 이벤트 전송을 위한 컬럼들 **/
    private String userEmail;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean marketingYn;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private User user;

    public EventListener(String userEmail, boolean marketingYn){
        this.userEmail = userEmail;
        this.marketingYn = marketingYn;
    }
}
