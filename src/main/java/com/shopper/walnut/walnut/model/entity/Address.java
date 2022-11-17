package com.shopper.walnut.walnut.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {
    private String zipCode;
    private String streetAdr;
    private String detailAdr;
    protected Address(){

    }
    public Address(String zipCode, String streetAdr, String detailAdr) {
        this.zipCode = zipCode;
        this.streetAdr = streetAdr;
        this.detailAdr = detailAdr;
    }
    public String totalAddress(){
        return "우편번호: " + this.zipCode + " 도로명주소: " + this.streetAdr + " 상세주소: " + this.detailAdr;
    }
}
